package idwall.desafio.crawler.rpc.client;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcChannel;
import com.google.protobuf.RpcController;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.util.UUID;

public class RabbitMqRpcChannel implements RpcChannel {

    public static final String EXCHANGE_NAME = "rpc-xch";

    public RabbitMqRpcChannel(Channel rabbitMqChannel) {
        this.rabbitMqChannel = rabbitMqChannel;
        monitor = new Object();
    }

    public void callMethod(Descriptors.MethodDescriptor method, RpcController controller,
        Message request, Message responsePrototype, RpcCallback<Message> done) {
        try {
            System.out.println("callMethod 1");
            rabbitMqChannel.exchangeDeclare(EXCHANGE_NAME, "topic");
            String replyQueueName = rabbitMqChannel.queueDeclare("", false, false, true, null)
                .getQueue();

            String correlationId = UUID.randomUUID().toString();
            System.out.println("callMethod 2");

            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(correlationId)
                    .replyTo(replyQueueName)
                    .build();

            String topic = method.getFullName();

            System.out.println("callMethod 3");
            rabbitMqChannel.basicPublish(EXCHANGE_NAME, topic, props, request.toByteArray());
            System.out.println("callMethod 4");
            String ctag = rabbitMqChannel.basicConsume(replyQueueName, true,
                createDeliverCallback(correlationId, responsePrototype, done, monitor),
                consumerTag -> { });

                System.out.println("callMethod 5");
            synchronized(monitor) {
                System.out.println("callMethod 6");
                monitor.wait();
            }
            System.out.println("callMethod 7");

            rabbitMqChannel.basicCancel(ctag);
            System.out.println("callMethod 8");

        } catch (Exception e) {
            System.out.println("callMethod 9");
            e.printStackTrace();
        }
    }

    private DeliverCallback createDeliverCallback(String correlationId, Message responsePrototype,
        RpcCallback<Message> done, Object monitor) {

        return (consumerTag, delivery) -> {
            System.out.println("deliverCallback 1");
            if (delivery.getProperties().getCorrelationId().equals(correlationId)) {
                System.out.println("deliverCallback 2");
                done.run(responsePrototype.toBuilder().mergeFrom(delivery.getBody()).build());
            }

            System.out.println("deliverCallback 3");
            synchronized(monitor) {

            System.out.println("deliverCallback 4");
                monitor.notify();
            }

            System.out.println("deliverCallback 5");
        };
    }

    private Object monitor;
    private Channel rabbitMqChannel;

}

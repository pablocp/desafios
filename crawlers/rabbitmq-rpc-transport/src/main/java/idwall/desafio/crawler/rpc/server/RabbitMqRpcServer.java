package idwall.desafio.crawler.rpc.server;

import java.io.IOException;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.RpcController;
import com.google.protobuf.Service;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.util.concurrent.atomic.AtomicReference;

public class RabbitMqRpcServer extends RpcServer {

    public static final String EXCHANGE_NAME = "rpc-xch";

    public RabbitMqRpcServer(Service service, Channel rabbitMqChannel) {
        super(service);
        this.rabbitMqChannel = rabbitMqChannel;
    }

    public void run() {
        try {
            rabbitMqChannel.basicQos(1);
            rabbitMqChannel.exchangeDeclare(EXCHANGE_NAME, "topic");
            String queueName = this.getService().getDescriptorForType().getFullName();

            rabbitMqChannel.queueDeclare(queueName, false, false, true, null);
            rabbitMqChannel.queueBind(queueName, EXCHANGE_NAME, queueName+".*");

            Object monitor = new Object();
            rabbitMqChannel.basicConsume(queueName, false, createDeliverCallback(monitor),
                (consumerTag -> { }));

            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DeliverCallback createDeliverCallback(Object monitor) {
        return (consumerTag, delivery) -> {

            AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();


            AtomicReference<Message> output = new AtomicReference<>();
            try {
                Service service = this.getService();

                String methodFullName = delivery.getEnvelope().getRoutingKey();
                String methodName = methodFullName.substring(methodFullName.lastIndexOf('.') + 1);
                Descriptors.MethodDescriptor method = service.getDescriptorForType()
                    .findMethodByName(methodName);

                Message input = service.getRequestPrototype(method)
                    .toBuilder()
                    .mergeFrom(delivery.getBody())
                    .build();
                RpcController rpcController = null;

                service.callMethod(
                    method,
                    rpcController,
                    input,
                    output::set
                );
            } catch (RuntimeException re) {
                re.printStackTrace();
            } finally {
                if (output.get() != null) {
                    boolean mandatoryPublish = true;
                    rabbitMqChannel.basicPublish("",
                        delivery.getProperties().getReplyTo(), mandatoryPublish, replyProps,
                        output.get().toByteArray());
                }

                rabbitMqChannel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                synchronized (monitor) {
                    monitor.notify();
                }
            }
        };
    }

    private Channel rabbitMqChannel;

}

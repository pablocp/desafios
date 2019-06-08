package idwall.desafio.crawler.bot;

import com.google.protobuf.RpcChannel;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import idwall.desafio.crawler.rpc.client.RabbitMqRpcChannel;
import idwall.desafio.crawler.reddit.RedditCrawlerProtos.RedditCrawlerService;
import java.util.Collections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

  @Bean
  public Connection createRabbitmqConnection() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(System.getenv("RABBITMQ_HOST"));
    return factory.newConnection();
  }

  @Bean
  public Channel createRabbitmqChannel(Connection connection) throws Exception {
    return connection.createChannel();
  }

  @Bean
  public RpcChannel createRpcChannel(Channel channel) {
    return new RabbitMqRpcChannel(channel);
  }

  @Bean
  public RedditCrawlerService createRedditRpcServerStub(RpcChannel channel) {
    return RedditCrawlerService.newStub(channel);
  }
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
        app.setDefaultProperties(Collections
          .singletonMap("server.port", System.getenv("PORT")));
        app.run(args);
	}

}

package idwall.desafio.crawler.rpc;

import com.google.protobuf.Service;
import idwall.desafio.crawler.reddit.JSoupSubredditThreadIteratorFactory;
import idwall.desafio.crawler.reddit.RedditCrawlerImpl;
import idwall.desafio.crawler.reddit.RedditCrawlerProtos.RedditCrawlerService;
import idwall.desafio.crawler.rpc.server.RabbitMqRpcServer;
import idwall.desafio.crawler.rpc.server.RpcServer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RedditCrawlerServerApplication {

	public static void main(String[] args) throws Exception {
		Service redditCrawler =
			RedditCrawlerService.newReflectiveService(new RedditCrawlerImpl(
				new JSoupSubredditThreadIteratorFactory()
			));

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(System.getenv("RABBITMQ_HOST"));

		try (Connection connection = factory.newConnection();
			Channel channel = connection.createChannel()) {

			RpcServer server = new RabbitMqRpcServer(redditCrawler, channel);
			server.run();
		}
	}

}

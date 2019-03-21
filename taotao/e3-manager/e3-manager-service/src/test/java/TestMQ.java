/*import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class TestMQ {
	
	
	 
	@Test
	public void testMqSend() throws Exception{
		// 创建一个连接工厂对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://112.74.166.230:61616");
		// 使用工厂对象创建connection对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 创建session 参数1:是否开启事务(如果开启参数2无意义)  参数2:应答模式(一般自动应答)
		Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
		// session创建Destination对象
		Topic topic = session.createTopic("spring-queue");
		// session创建Producer对象
		MessageProducer messageProducer = session.createProducer(topic);
		// 创建消息对象
		TextMessage textMessage = session.createTextMessage("topic message");
		// 发送消息
		messageProducer.send(textMessage);
		// 关闭资源
		connection.close();
		session.close();
		messageProducer.close();
		
	}
	
	@Test
	public void testQueueConsumer() throws Exception{
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://112.74.166.230:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("spring-queue");
		MessageConsumer messageConsumer = session.createConsumer(queue);
		messageConsumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				// TODO Auto-generated method stub
				TextMessage textMessage = (TextMessage)message;
				try {
					System.out.println(textMessage.getText());
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		// 等待接受消息
		System.in.read();
		// 关闭资源
		connection.close();
		session.close();
		messageConsumer.close();
	}
	
	@Test
	public void testTopicProducer() throws Exception{
		// 创建一个连接工厂对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://112.74.166.230:61616");
		// 使用工厂对象创建connection对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 创建session 参数1:是否开启事务(如果开启参数2无意义)  参数2:应答模式(一般自动应答)
		Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
		// session创建Destination对象
		Topic topic = session.createTopic("test-topic");
		// session创建Producer对象
		MessageProducer messageProducer = session.createProducer(topic);
		// 创建消息对象
		TextMessage textMessage = session.createTextMessage("topic message");
		// 发送消息
		messageProducer.send(textMessage);
		// 关闭资源
		connection.close();
		session.close();
		messageProducer.close();
	}
}
*/
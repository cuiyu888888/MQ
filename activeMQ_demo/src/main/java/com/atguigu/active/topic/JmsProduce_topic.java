package com.atguigu.active.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 发布订阅
 *
 * 消息的生产者
 */
public class JmsProduce_topic {

    private static final String ACTIVEMQ_URL = "tcp://192.168.45.128:61616";
    private static final String TOPIC_NAEM = "topic-atguigu";

    public static void main(String[] args) throws Exception {
        // 1：获取对应的ConnectionFactory
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("admin", "admin", ACTIVEMQ_URL);

        // 2：通过连接工厂，获取连接
        Connection connection = activeMQConnectionFactory.createConnection();

        // 3：启动访问
        connection.start();

        // 4：创建会话session
        // 两个参数：第一个叫事务，第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 5：创建目的地（具体是队列queue还是主题topic）
        Topic topic = session.createTopic(TOPIC_NAEM); // 类比Collection collection = new ArrayList();

        // 6：创建消息的生产者
        MessageProducer producer = session.createProducer(topic);

        // 7：通过使用消息的生产者producer生产3条消息发送到MQ的队列里面
        for (int i = 1; i <= 3; i++) {

            // 8：创建消息
            TextMessage textMessage = session.createTextMessage("msg---" + i);

            // 9：生产者发送消息，这时消息就发送到了队列中
            producer.send(textMessage);
        }

        // 10：关闭资源
        producer.close();
        session.close();
        connection.close();

        System.out.println("TOPIC消息发送完成");
    }

}

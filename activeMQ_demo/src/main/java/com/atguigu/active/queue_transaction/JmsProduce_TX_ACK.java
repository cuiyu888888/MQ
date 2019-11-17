package com.atguigu.active.queue_transaction;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消息的生产者
 *
 * 事务特性：
 *      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 *      当是false时：
 *         只要生产者执行producer.send(Message message)方发时，就会将消息加入到队列中
 *
 *      Session session = connection.createSession(ture, Session.AUTO_ACKNOWLEDGE);
 *      当是ture时：
 *         只有生产者执行session.commit()方法时，才会将消息真正的提交到队列中去
 *
 */
public class JmsProduce_TX_ACK {

    private static final String ACTIVEMQ_URL = "tcp://192.168.45.128:61616";
    private static final String QUEUE_NAEM = "queue01";

    public static void main(String[] args) throws Exception {
        // 1：获取对应的ConnectionFactory
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("admin", "admin", ACTIVEMQ_URL);

        // 2：通过连接工厂，获取连接
        Connection connection = activeMQConnectionFactory.createConnection();

        // 3：启动访问
        connection.start();

        /**
         * 4：创建会话session
         *
         * 两个参数：第一个叫事务，第二个叫签收
         *
         * 事务特性：
         *      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
         *      当是false时：
         *         只要生产者执行producer.send(Message message)方发时，就会将消息加入到队列中
         *
         *      Session session = connection.createSession(ture, Session.AUTO_ACKNOWLEDGE);
         *      当是ture时：
         *         只有生产者执行session.commit()方法时，才会将消息真正的提交到队列中去
         *
         */
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

        // 5：创建目的地（具体是队列queue还是主题topic）
        Queue queue = session.createQueue(QUEUE_NAEM); // 类比Collection collection = new ArrayList();

        // 6：创建消息的生产者
        MessageProducer producer = session.createProducer(queue);

        // 7：通过使用消息的生产者producer生产3条消息发送到MQ的队列里面
        for (int i = 1; i <= 3; i++) {

            // 8：创建消息
            TextMessage textMessage = session.createTextMessage("msg---" + i);

            // 9：生产者发送消息，这时消息就发送到了队列中
            producer.send(textMessage);
        }

        /**
         * 添加事务时的代码逻辑
         */
        try {
            //.........

            // 提交事务，将消息放入到队列中去
            session.commit();
        } catch (Exception e){
            // 回滚消息
            session.rollback();
        } finally {
            // 关闭session
            session.close();
        }

        // 10：关闭资源
        producer.close();
        session.close();
        connection.close();

        System.out.println("消息发送完成");


    }

}

package com.atguigu.active.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 发布订阅
 *
 * 消息的消费者
 */
public class JmsCousumer_topic {

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

        // 6：创建消息的消费者
        MessageConsumer consumer = session.createConsumer(topic);

//
//        /**
//         * 第一种方式
//         * 7：通过使用消息的消费者consumer的receive方法从MQ的队列里面获取消息
//         *
//         * 同步阻塞方法：receive()
//         * 订阅者或者接受者调用consumer的receive()方法来接收消息，receive()方法能够在接受到消息之前（或超时之前）将一直等待
//         */
//        while (true) {
//            /**
//             * 8：获取消息
//             *
//             * consumer.receive()
//             * 一致等待消息
//             *
//             * consumer.receive(4000)
//             * 等待4000ms后就不在等待消息
//             */
//            TextMessage textMessage = (TextMessage) consumer.receive();
//            if (textMessage != null) {
//
//                // 9：获取消息中的具体内容
//                System.out.println("消费者接收到topic的消息为：" + textMessage.getText());
//            } else {
//                break;
//            }
//        }
//
//        // 10：关闭资源
//        consumer.close();
//        session.close();
//        connection.close();

        /**
         * 第二种方式
         *
         * 7：通过监听的方式来消费消息
         * 异步非阻塞方式（监听器onMessage）
         * 订阅者或者接受者通过consumer.setMessageListener(MessageListener messageListener)注册一个消息监听器
         * 当消息到达以后，系统会自动调用监听器MessageListener的onMessage（Message message）方法，获取消息
         */
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message != null && message instanceof TextMessage) {
                    // 8：获取消息中的具体内容
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("消费者接收到topic的消息为：" + textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        /**
         * 9：当使用第二种方式的方式进行时，需要加上下面这句话
         * 因为监听器连接Linux主机需要时间，这时候入伙没有这句话，就直接关闭资源了
         * 这样将会造成读不到消息，System.in.read();是为了保证消费者不关闭
         */
        System.in.read();

        // 10：关闭资源
        consumer.close();
        session.close();
        connection.close();

    }

}

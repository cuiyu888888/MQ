package com.atguigu.active.queue_transaction;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消息的消费者
 *
 * 事务特性：
 *      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 *     当是false时：
 *         只要消费者从队列中获取到消息，该消息就表明已经消费，消息会从队列中移除
 *
 *     Session session = connection.createSession(ture, Session.AUTO_ACKNOWLEDGE);
 *     当是ture时：
 *         只有消费者执行session.commit()方法之后，才代表真正的消费，
 *         如果没有执行session.commit()，就会导致重复消费的情况
 *
 *
 * 签收特性：
 *      非事务签收：
 *          1：自动签收
 *          Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 *
 *          2：手动签收
 *          Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
 *          客户端需要调用acknowledge()方法手动签收
 *          如果不签收，会造成重复消费的情况
 *
 *          3：允许重复消费
 *          Session session = connection.createSession(true, Session.DUPS_OK_ACKNOWLEDGE);
 *          可能会造成重复消费，就相当于你和你女朋友同时签收一个快递
 *
 *
 *      带事务：
 *          1：当消费者一方开启事务，消费完成后调用session.conmit（）方法，
 *          消费方默认自动签收的，消费方调不调用acknowledge()方法，都是默认自动签收
 *
 *          2：当消费者一方开启事务，消费完成后未调用session.conmit（）方法，
 *          消费方调不调用acknowledge()方法，都是未签收的，这样会造成重复消费
 *
 *
 * 签收和事务的关系：
 *      1：在消费方，当处于事务性会话中，当一个事务被成功提交，则消息被自动签收。如果事务回滚，则消息会被再次传送
 *      2：在消费方，当处于非事务性会话中，消息何时签收取决于创建会话时的应答模式（acknowledgement）

 *
 */
public class JmsCousumer_TX_ACK {

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
         * 事务：
         *      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
         *     当是false时：
         *         只要消费者从队列中获取到消息，该消息就表明已经消费，消息会从队列中移除
         *
         *     Session session = connection.createSession(ture, Session.AUTO_ACKNOWLEDGE);
         *     当是ture时：
         *         只有消费者执行session.commit()方法之后，才代表真正的消费，
         *         如果没有执行session.commit()，就会导致重复消费的情况
         *
         *
         * 签收：
         *      非事务签收：
         *          1：自动签收
         *          Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
         *
         *          2：手动签收
         *          Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
         *          客户端需要调用acknowledge()方法手动签收
         *          如果不签收，会造成重复消费的情况
         *
         *          3：允许重复消费
         *          Session session = connection.createSession(true, Session.DUPS_OK_ACKNOWLEDGE);
         *          可能会造成重复消费，就相当于你和你女朋友同时签收一个快递
         *
         *
         *      带事务：
         *          1：当消费者一方开启事务，消费完成后调用session.conmit（）方法，
         *          消费方默认自动签收的，消费方调不调用acknowledge()方法，都是默认自动签收
         *
         *          2：当消费者一方开启事务，消费完成后未调用session.conmit（）方法，
         *          消费方调不调用acknowledge()方法，都是未签收的，这样会造成重复消费
         *
         * 签收和事务关系：
         *      1：在消费方，当处于事务性会话中，当一个事务被成功提交，则消息被自动签收。如果事务回滚，则消息会被再次传送
         *      2：在消费方，当处于非事务性会话中，消息何时签收取决于创建会话时的应答模式（acknowledgement）
         *
         */
        Session session = connection.createSession(true, Session.DUPS_OK_ACKNOWLEDGE);

        // 5：创建目的地（具体是队列queue还是主题topic）
        Queue queue = session.createQueue(QUEUE_NAEM); // 类比Collection collection = new ArrayList();

        // 6：创建消息的消费者
        MessageConsumer consumer = session.createConsumer(queue);

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
                        System.out.println("消费者接收到的消息为：" + textMessage.getText());

                        /**
                         * 签收方式
                         *      当为手动签收时：
                         *          消费者一方，需要手动的调用textMessage.acknowledge()方法手动签收
                         *          如果不签收，会造成重复消费的情况
                         */
                        textMessage.acknowledge();

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

    }

}

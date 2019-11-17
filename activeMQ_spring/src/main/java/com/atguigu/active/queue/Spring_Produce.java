package com.atguigu.active.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * 生产者
 */
@Service
public class Spring_Produce {

    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        Spring_Produce produce = (Spring_Produce) applicationContext.getBean("spring_Produce");

        produce.jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("Spring和ActiveMQ的整合case");
            }
        });

        System.out.println("消息发送完毕");
    }

}

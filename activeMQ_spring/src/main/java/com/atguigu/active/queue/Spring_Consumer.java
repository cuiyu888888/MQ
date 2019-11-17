package com.atguigu.active.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * 消费者
 */
@Service
public class Spring_Consumer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        Spring_Consumer consumer = (Spring_Consumer) applicationContext.getBean("spring_Consumer");

        String msg = (String) consumer.jmsTemplate.receiveAndConvert();

        System.out.println("消息接收完成：" + msg);
    }

}

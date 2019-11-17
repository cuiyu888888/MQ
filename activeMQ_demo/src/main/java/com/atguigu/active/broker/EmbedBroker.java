package com.atguigu.active.broker;


import org.apache.activemq.broker.BrokerService;

/**
 * 嵌入式的Broker
 *
 * ActiveMQ也支持在虚拟机vm中通信基于嵌入式的Broker
 */
public class EmbedBroker {

    public static void main(String[] args) throws Exception{

        // ActiveMQ也支持在虚拟机vm中通信基于嵌入式的Broker
        BrokerService brokerService = new BrokerService();
        brokerService.setUseJmx(true);
        brokerService.addConnector("tcp://localhost:61616");
        brokerService.start();
    }

}

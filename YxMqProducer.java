package com.yxkj.platform.event;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * rocketMq的生产者
 * Created by bruce on 2018/3/29.
 */
public class YxMqProducer {
    private final Logger logger = LoggerFactory.getLogger(YxMqProducer.class);

    private DefaultMQProducer defaultMQProducerMember;
    private DefaultMQProducer defaultMQProducerSaas;
    private DefaultMQProducer defaultMQProducerVc;
    private String producerGroup;
    private String namesrvAddr;

    /**
     * Spring bean init-method
     */
    public void init() throws MQClientException {
        // 参数信息
        logger.debug("DefaultMQProducer initialize!");
        logger.debug(producerGroup);
        logger.debug(namesrvAddr);

        // 初始化  会员组生产者  服务地址  实例名为时间
        defaultMQProducerMember = new DefaultMQProducer("groupMember");
        defaultMQProducerMember.setNamesrvAddr(namesrvAddr);
        defaultMQProducerMember.setInstanceName(String.valueOf(System.currentTimeMillis()));
        //启动生产者
        defaultMQProducerMember.start();
        // 初始化  多园区生产者  服务地址  实例名为时间
        defaultMQProducerSaas = new DefaultMQProducer("groupSaas");
        defaultMQProducerSaas.setNamesrvAddr(namesrvAddr);
        defaultMQProducerSaas.setInstanceName(String.valueOf(System.currentTimeMillis()));
        //启动生产者
        defaultMQProducerSaas.start();
        // 初始化  运营区生产者  服务地址  实例名为时间
        defaultMQProducerVc = new DefaultMQProducer("groupVc");
        defaultMQProducerVc.setNamesrvAddr(namesrvAddr);
        defaultMQProducerVc.setInstanceName(String.valueOf(System.currentTimeMillis()));
        //启动生产者
        defaultMQProducerVc.start();

        logger.debug("DefaultMQProudcer start success!");

    }

    /**
     * Spring bean destroy-method
     */
    public void destroy() {
        defaultMQProducerMember.shutdown();
        defaultMQProducerSaas.shutdown();
        defaultMQProducerVc.shutdown();
    }

    public DefaultMQProducer getDefaultMQProducerMember() {
        return defaultMQProducerMember;
    }

    public DefaultMQProducer getDefaultMQProducerSaas() {
        return defaultMQProducerSaas;
    }

    public DefaultMQProducer getDefaultMQProducerVc() {
        return defaultMQProducerVc;
    }

    // ---------------setter -----------------

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }
}

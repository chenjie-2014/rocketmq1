package com.yxkj.platform.event;

import com.alibaba.rocketmq.common.message.Message;
import com.yxkj.platform.common.mapper.JsonMapper;
import com.yxkj.platform.common.utils.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 事件模型调用入口
 * Created by bruce on 2017/9/6.
 */
public class YxEventCenter {

    private static final Logger logger = LoggerFactory.getLogger(YxEventCenter.class);

    /**
     * 发出事件
     * @param event
     * @param entityId
     */
    public static void notifyEvent(String event,String entityId){
        notifyEventOnEntityId(event,entityId,null);
    }

    /**
     * 发出事件
     * @param event
     * @param entityId
     */
    public static void notifyEvent(String event,String entityId,Object extra){
        notifyEventOnEntityId(event,entityId,extra);
    }

    /**
     * 根据对象id和转换后的对象发出事件
     * @param event
     * @param entityId
     * @param extra
     */
    private static void notifyEventOnEntityId(String event,String entityId,Object extra){
        //获取生产者实例
        YxMqProducer producer = SpringContextHolder.getBean(YxMqProducer.class);
        //创建一个事件对象
        YxEvent e = new YxEvent();
        e.setEntityId(entityId);
        if(extra!=null){
            //事件不为空，则转换为json串
            e.setExtraJson(JsonMapper.toJsonString(extra));
        }
        //将这个事件对象转成json串
        String msg = JsonMapper.toJsonString(e);
        /*
        早期使用redis的消息队列，不稳定，替换为mq
        RedissonClient redisTemplate = SpringContextHolder.getBean(RedissonClient.class);
        RTopic<Object> topic = redisTemplate.getTopic(event);
        topic.publish(msg);*/

        logger.debug("member发出消息:"+event+":"+msg);
        //创建三个事件消息
        Message messageToMember = new Message("messageToMember",event,msg.getBytes());
        Message messageToSaas = new Message("messageToSaas",event,msg.getBytes());
        Message messageToVc = new Message("messageToVc",event,msg.getBytes());
        try {
            //生产者发出事件   会员    多园区   vc运营
            producer.getDefaultMQProducerMember().send(messageToMember);
            producer.getDefaultMQProducerSaas().send(messageToSaas);
            producer.getDefaultMQProducerVc().send(messageToVc);
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
    }

}

package top.bughome.monitor.sdk.push.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import top.bughome.monitor.sdk.model.LogMessage;
import top.bughome.monitor.sdk.push.IPush;

/**
 * RabbitMQ 推送实现类
 * Created by MaxWell on 2025/8/15 18:44
 */
public class RabbitPush implements IPush {

    private final String topic;
    private final RabbitTemplate rabbitTemplate;

    public RabbitPush(String topic, RabbitTemplate rabbitTemplate) {
        this.topic = topic;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void send(LogMessage logMessage) {
        try {
            String data = gson.toJson(logMessage);
            rabbitTemplate.convertAndSend(topic, data);
            logger.info("业务行为监控组件，推送日志到RabbitMQ成功，消息内容：{}", data);
        } catch (Exception e) {
            logger.error("警告⚠️：业务行为监控组件，推送日志到RabbitMQ失败，请检查RabbitMQ服务是否正常运行，或网络连接是否正常。", e);
        }
    }
}

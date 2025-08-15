package top.bughome.monitor.sdk.push.impl;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import top.bughome.monitor.sdk.model.LogMessage;
import top.bughome.monitor.sdk.push.IPush;

import java.util.concurrent.CompletableFuture;

/**
 * Created by MaxWell on 2025/8/14 20:54
 */
public class KafkaPush implements IPush {

    private final String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaPush(String topic, KafkaTemplate<String, String> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(LogMessage logMessage) {
        try {
            String jsonStr = gson.toJson(logMessage);
            CompletableFuture<SendResult<String, String>> send = kafkaTemplate.send(topic, jsonStr);
            logger.info("业务行为监控组件，推送日志到Kafka成功，消息内容：{}", jsonStr);
        } catch (Exception e) {
            logger.error("警告⚠️：业务行为监控组件，推送日志到Kafka失败，请检查Kafka服务是否正常运行，或网络连接是否正常。", e);
        }
    }


}

package top.bughome.monitor.sdk.push.impl;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import top.bughome.monitor.sdk.model.LogMessage;
import top.bughome.monitor.sdk.push.IPush;

/**
 * Created by MaxWell on 2025/8/14 20:54
 */
public class KafkaPush implements IPush {

    private final String topic;

    private final KafkaProducer<String, String> kafkaProducer;

    public KafkaPush(String topic, KafkaProducer<String, String> kafkaProducer) {
        this.topic = topic;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void send(LogMessage logMessage) {
        try {
            String jsonStr = gson.toJson(logMessage);
            kafkaProducer.send(new ProducerRecord<>(topic, jsonStr), (metadata, exception) -> {
                if (exception != null) {
                    logger.error("警告⚠️：业务行为监控组件，推送日志到Kafka失败，请检查Kafka服务是否正常运行，或网络连接是否正常。", exception);
                } else {
                    logger.debug("业务行为监控组件，推送日志到Kafka成功，消息内容：{}", jsonStr);
                }
            });
        } catch (Exception e) {
            logger.error("业务行为监控组件，序列化消息失败", e);
        }
    }
}

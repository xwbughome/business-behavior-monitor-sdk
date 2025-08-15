package top.bughome.monitor.sdk.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import top.bughome.monitor.sdk.properties.KafkaProperties;
import top.bughome.monitor.sdk.push.IPush;
import top.bughome.monitor.sdk.push.impl.KafkaPush;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka配置类
 * Created by MaxWell on 2025/8/15 20:23
 */
public class KafkaConfig {
    public static IPush createKafkaPush(KafkaProperties kafkaProperties) {
        if (kafkaProperties == null) {
            throw new IllegalStateException("KafkaConfig配置有误");
        }
        if (kafkaProperties.getServers() == null || kafkaProperties.getServers().trim().isEmpty()) {
            throw new IllegalStateException("KafkaConfig 中的 servers 配置不能为空");
        }

        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(createKafkaProducerFactory(kafkaProperties));
        return new KafkaPush(kafkaProperties.getTopic(), kafkaTemplate);
    }

    private static ProducerFactory<String, String> createKafkaProducerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getServers());
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProperties.getRetries());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProperties.getBatchSize());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProperties.getLinger());
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProperties.getBufferMemory());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }
}

package top.bughome.monitor.sdk.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import top.bughome.monitor.sdk.properties.KafkaProperties;
import top.bughome.monitor.sdk.push.IPush;
import top.bughome.monitor.sdk.push.impl.KafkaPush;

import java.util.Properties;

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
        KafkaProducer<String, String> kafkaProducer = createKafkaProducerFactory(kafkaProperties);
        return new KafkaPush(kafkaProperties.getTopic(), kafkaProducer);
    }

    private static KafkaProducer<String, String> createKafkaProducerFactory(KafkaProperties kafkaProperties) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getServers());
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProperties.getRetries());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProperties.getBatchSize());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProperties.getLinger());
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProperties.getBufferMemory());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        return new KafkaProducer<String, String>(props);
    }
}

package top.bughome.monitor.sdk.factory;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import top.bughome.monitor.sdk.config.ChannelConfig;
import top.bughome.monitor.sdk.config.KafkaConfig;
import top.bughome.monitor.sdk.config.RedisConfig;
import top.bughome.monitor.sdk.push.IPush;
import top.bughome.monitor.sdk.push.impl.KafkaPush;
import top.bughome.monitor.sdk.push.impl.RedisPush;

import java.util.HashMap;
import java.util.Map;

/**
 * 推送工厂类，负责创建和初始化推送实例
 * Created by MaxWell on 2025/8/14 22:13
 */
public class PushFactory {

    public static IPush createKafkaPush(ChannelConfig channelConfig) {
        KafkaConfig kafkaConfig = channelConfig.getKafkaConfig();
        if (kafkaConfig == null) {
            throw new IllegalStateException("KafkaConfig配置有误");
        }
        if (kafkaConfig.getServers() == null || kafkaConfig.getServers().trim().isEmpty()) {
            throw new IllegalStateException("KafkaConfig 中的 servers 配置不能为空");
        }

        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(createKafkaProducerFactory(kafkaConfig));
        return new KafkaPush(kafkaConfig.getTopic(), kafkaTemplate);
    }

    public static IPush createRedisPush(ChannelConfig channelConfig) {
        RedisConfig redisConfig = channelConfig.getRedisConfig();
        if (redisConfig == null) {
            throw new IllegalStateException("RedisConfig配置有误");
        }
        if (redisConfig.getHost() == null || redisConfig.getHost().trim().isEmpty()) {
            throw new IllegalStateException("RedisConfig 中的 host 配置不能为空");
        }
        if (redisConfig.getPort() <= 0) {
            throw new IllegalStateException("RedisConfig 中的 port 配置必须大于 0");
        }

        RedissonClient redissonClient = createRedissonClient(redisConfig);
        return new RedisPush(redisConfig.getTopic(), redissonClient);
    }


    private static ProducerFactory<String, String> createKafkaProducerFactory(KafkaConfig kafkaConfig) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getServers());
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaConfig.getRetries());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaConfig.getBatchSize());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaConfig.getLinger());
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaConfig.getBufferMemory());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    private static RedissonClient createRedissonClient(RedisConfig redisConfig) {
        Config config = new Config();
        config.setCodec(JsonJacksonCodec.INSTANCE);
        config.useSingleServer()
                .setAddress("redis://" + redisConfig.getHost() + ":" + redisConfig.getPort())
                .setPassword(redisConfig.getPassword())
                .setConnectionPoolSize(64)
                .setConnectionMinimumIdleSize(10)
                .setIdleConnectionTimeout(1000)
                .setConnectTimeout(1000)
                .setRetryAttempts(3)
                .setRetryInterval(1000)
                .setPingConnectionInterval(0)
                .setKeepAlive(true);
        return Redisson.create(config);
    }
}

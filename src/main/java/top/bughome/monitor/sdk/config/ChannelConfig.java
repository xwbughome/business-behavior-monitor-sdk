package top.bughome.monitor.sdk.config;

/**
 * Created by MaxWell on 2025/8/14 21:30
 */
public class ChannelConfig {
    private KafkaConfig kafkaConfig;
    private RedisConfig redisConfig;

    public ChannelConfig(KafkaConfig kafkaConfig, RedisConfig redisConfig) {
        this.kafkaConfig = kafkaConfig;
        this.redisConfig = redisConfig;
    }

    public KafkaConfig getKafkaConfig() {
        return kafkaConfig;
    }

    public void setKafkaConfig(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }

    public void setRedisConfig(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }
}

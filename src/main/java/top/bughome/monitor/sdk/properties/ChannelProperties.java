package top.bughome.monitor.sdk.properties;

/**
 * Created by MaxWell on 2025/8/14 21:30
 */
public class ChannelProperties {
    private KafkaProperties kafkaProperties;
    private RedisProperties redisProperties;
    private RabbitMqProperties rabbitMqProperties;

    public ChannelProperties(KafkaProperties kafkaProperties, RedisProperties redisProperties, RabbitMqProperties rabbitMqProperties) {
        this.kafkaProperties = kafkaProperties;
        this.redisProperties = redisProperties;
        this.rabbitMqProperties = rabbitMqProperties;
    }

    public KafkaProperties getKafkaConfig() {
        return kafkaProperties;
    }

    public void setKafkaConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    public RedisProperties getRedisConfig() {
        return redisProperties;
    }

    public void setRedisConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    public RabbitMqProperties getRabbitMqConfig() {
        return rabbitMqProperties;
    }

    public void setRabbitMqConfig(RabbitMqProperties rabbitMqProperties) {
        this.rabbitMqProperties = rabbitMqProperties;
    }
}

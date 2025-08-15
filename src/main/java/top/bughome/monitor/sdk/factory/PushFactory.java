package top.bughome.monitor.sdk.factory;

import top.bughome.monitor.sdk.config.KafkaConfig;
import top.bughome.monitor.sdk.config.RabbitMqConfig;
import top.bughome.monitor.sdk.config.RedisConfig;
import top.bughome.monitor.sdk.properties.ChannelProperties;
import top.bughome.monitor.sdk.push.IPush;

/**
 * 推送工厂类，负责创建和初始化推送实例
 * Created by MaxWell on 2025/8/14 22:13
 */
public class PushFactory {

    public static IPush createKafkaPush(ChannelProperties channelProperties) {
        return KafkaConfig.createKafkaPush(channelProperties.getKafkaConfig());
    }

    public static IPush createRedisPush(ChannelProperties channelProperties) {
        return RedisConfig.createRedisPush(channelProperties.getRedisConfig());
    }

    public static IPush createRabbit(ChannelProperties channelProperties) {
        return RabbitMqConfig.createRabbitPush(channelProperties.getRabbitMqConfig());
    }
}

package top.bughome.monitor.sdk.push;

import top.bughome.monitor.sdk.properties.ChannelProperties;
import top.bughome.monitor.sdk.factory.PushFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by MaxWell on 2025/8/14 21:15
 */
public class PushConfig {
    /**
     * 推送配置
     */
    protected static Map<String, IPush> pushMap = new ConcurrentHashMap<>();

    /**
     * 获取推送实例
     */
    public static IPush getPush(String channel, ChannelProperties channelProperties) {
        return pushMap.computeIfAbsent(channel, k -> switch (channel.toLowerCase()) {
            case "redis" -> PushFactory.createRedisPush(channelProperties);
            case "kafka" -> PushFactory.createKafkaPush(channelProperties);
            case "rabbitmq", "rabbit" -> PushFactory.createRabbit(channelProperties);
            default -> throw new IllegalArgumentException("不支持的推送渠道类型: " + channel);
        });
    }
}

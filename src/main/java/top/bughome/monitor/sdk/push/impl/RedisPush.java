package top.bughome.monitor.sdk.push.impl;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import top.bughome.monitor.sdk.model.LogMessage;
import top.bughome.monitor.sdk.push.IPush;

/**
 * Redis 发布订阅方式进行消息推送
 * Created by MaxWell on 2025/8/12 16:38
 */
public class RedisPush implements IPush {

    private final String topic;

    private final RedissonClient redissonClient;

    public RedisPush(String topic, RedissonClient redissonClient) {
        this.topic = topic;
        this.redissonClient = redissonClient;
    }

    @Override
    public void send(LogMessage logMessage) {
        try {
            RTopic redisTopic = redissonClient.getTopic(topic);
            long publish = redisTopic.publish(logMessage);
            logger.info("业务行为监控组件，推送日志到Redis成功，发布消息ID：{}", publish);
        } catch (Exception e) {
            logger.error("警告⚠️：业务行为监控组件，推送日志到Redis失败，请检查Redis服务是否正常运行，或网络连接是否正常。", e);
        }
    }
}

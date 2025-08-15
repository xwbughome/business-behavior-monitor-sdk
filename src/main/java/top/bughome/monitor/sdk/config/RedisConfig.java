package top.bughome.monitor.sdk.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import top.bughome.monitor.sdk.properties.RedisProperties;
import top.bughome.monitor.sdk.push.IPush;
import top.bughome.monitor.sdk.push.impl.RedisPush;

/**
 * Redis配置类
 * Created by MaxWell on 2025/8/15 20:21
 */
public class RedisConfig {

    public static IPush createRedisPush(RedisProperties redisProperties) {
        if (redisProperties == null) {
            throw new IllegalStateException("RedisConfig配置有误");
        }
        if (redisProperties.getHost() == null || redisProperties.getHost().trim().isEmpty()) {
            throw new IllegalStateException("RedisConfig 中的 host 配置不能为空");
        }
        if (redisProperties.getPort() <= 0) {
            throw new IllegalStateException("RedisConfig 中的 port 配置必须大于 0");
        }

        RedissonClient redissonClient = createRedissonClient(redisProperties);
        return new RedisPush(redisProperties.getTopic(), redissonClient);
    }

    private static RedissonClient createRedissonClient(RedisProperties redisProperties) {
        Config config = new Config();
        config.setCodec(JsonJacksonCodec.INSTANCE);
        config.useSingleServer()
                .setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
                .setPassword(redisProperties.getPassword())
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

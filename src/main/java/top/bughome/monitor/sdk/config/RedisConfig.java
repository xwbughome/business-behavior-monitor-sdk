package top.bughome.monitor.sdk.config;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
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

        JedisPool jedisPool = createJedisPool(redisProperties);
        return new RedisPush(redisProperties.getTopic(), jedisPool);
    }

    private static JedisPool createJedisPool(RedisProperties redisProperties) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //连接池的最小连接数
        poolConfig.setMinIdle(redisProperties.getRedisPoolMinIdle());
        //连接池的最大空闲连接数
        poolConfig.setMaxIdle(redisProperties.getRedisPoolMaxIdle());
        //连接池的最大连接数
        poolConfig.setMaxTotal(redisProperties.getRedisPollMaxTotal());
        //连接池耗尽后是否需要等待，默认true表示等待。当值为true时，setMaxWait才会生效
        poolConfig.setBlockWhenExhausted(true);
        //获取连接时校验有效性(ping)，默认false，业务量大时建议设置为false减少开销
        poolConfig.setTestOnBorrow(true);
        //是否开启空闲连接检测，如为false，则不剔除空闲连接
        poolConfig.setTestWhileIdle(true);
        return new JedisPool(poolConfig, redisProperties.getHost(), redisProperties.getPort(),
                redisProperties.getTimeout(), redisProperties.getPassword(), redisProperties.getDatabase());
    }
}

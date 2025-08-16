package top.bughome.monitor.sdk.properties;

/**
 * Redis配置文件
 * 该类用于定义Redis的连接配置，包括主机地址、端口和密码
 * Created by MaxWell on 2025/8/14 20:12
 */
public class RedisProperties {

    private String host = "localhost";
    private int port = 6379;
    private String password;
    private String topic = "business-behavior-monitor-sdk-topic";
    private int timeout = 3000; // 默认超时时间为3000毫秒
    private int database = 0; // 默认使用数据库0
    private int redisPoolMinIdle = 50; // 连接池最小连接数
    private int redisPoolMaxIdle = 200; // 连接池最大连接数
    private int redisPollMaxTotal = 200; //  连接池最大连接数

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public int getRedisPoolMinIdle() {
        return redisPoolMinIdle;
    }

    public void setRedisPoolMinIdle(int redisPoolMinIdle) {
        this.redisPoolMinIdle = redisPoolMinIdle;
    }

    public int getRedisPoolMaxIdle() {
        return redisPoolMaxIdle;
    }

    public void setRedisPoolMaxIdle(int redisPoolMaxIdle) {
        this.redisPoolMaxIdle = redisPoolMaxIdle;
    }

    public int getRedisPollMaxTotal() {
        return redisPollMaxTotal;
    }

    public void setRedisPollMaxTotal(int redisPollMaxTotal) {
        this.redisPollMaxTotal = redisPollMaxTotal;
    }
}

package top.bughome.monitor.sdk.config;

/**
 * Redis配置文件
 * 该类用于定义Redis的连接配置，包括主机地址、端口和密码
 * Created by MaxWell on 2025/8/14 20:12
 */
public class RedisConfig {

    private String host = "localhost";
    private int port = 6379;
    private String password;
    private String topic = "business-behavior-monitor-sdk-topic";

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
}

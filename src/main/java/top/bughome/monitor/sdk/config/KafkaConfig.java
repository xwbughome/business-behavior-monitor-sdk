package top.bughome.monitor.sdk.config;

/**
 * kafka配置文件
 * 该类用于定义Kafka的连接配置，包括主机地址、端口等
 * Created by MaxWell on 2025/8/14 20:50
 */
public class KafkaConfig {

    private String servers = "localhost:9092";
    private String topic = "business-behavior-monitor-sdk-topic";
    private int retries = 0;
    private int batchSize = 4096;
    private int linger = 1;
    private int bufferMemory = 40960;

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getLinger() {
        return linger;
    }

    public void setLinger(int linger) {
        this.linger = linger;
    }

    public int getBufferMemory() {
        return bufferMemory;
    }

    public void setBufferMemory(int bufferMemory) {
        this.bufferMemory = bufferMemory;
    }
}

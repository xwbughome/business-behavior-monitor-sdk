package top.bughome.monitor.sdk.push.impl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import top.bughome.monitor.sdk.model.LogMessage;
import top.bughome.monitor.sdk.push.IPush;

import java.nio.charset.StandardCharsets;

/**
 * RabbitMQ 推送实现类
 * Created by MaxWell on 2025/8/15 18:44
 */
public class RabbitPush implements IPush {

    private final String routingKey;
    private final Channel channel;
    private final String exchangeName;
    private final Connection connection; // 添加连接引用

    public RabbitPush(String routingKey, Channel channel, String exchangeName, Connection connection) {
        this.routingKey = routingKey;
        this.channel = channel;
        this.exchangeName = exchangeName;
        this.connection = connection; // 保存连接引用
    }

    @Override
    public void send(LogMessage logMessage) {
        try {
            String data = gson.toJson(logMessage);

            // 设置消息属性（包含优先级）
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .priority(5) // 设置消息优先级
                    .deliveryMode(2) // 消息持久化
                    .build();

            // 发送消息到 Topic 交换机
            channel.basicPublish(
                    exchangeName,    // 交换机名称
                    routingKey,      // 路由键
                    properties,      // 消息属性
                    data.getBytes(StandardCharsets.UTF_8) // 消息体
            );
            logger.debug("业务行为监控组件，推送日志到RabbitMQ成功，消息内容：{}", data);
        } catch (Exception e) {
            logger.error("警告⚠️：业务行为监控组件，推送日志到RabbitMQ失败，请检查RabbitMQ服务是否正常运行，或网络连接是否正常。", e);
        }
    }

    public void close() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        } catch (Exception e) {
            logger.error("关闭RabbitMQ连接失败", e);
        }
    }
}

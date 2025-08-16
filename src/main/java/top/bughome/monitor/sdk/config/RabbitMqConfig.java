package top.bughome.monitor.sdk.config;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import top.bughome.monitor.sdk.properties.RabbitMqProperties;
import top.bughome.monitor.sdk.push.IPush;
import top.bughome.monitor.sdk.push.impl.RabbitPush;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by MaxWell on 2025/8/15 20:23
 */
public class RabbitMqConfig {

    private static final String EXCHANGE_NAME = "business-behavior-monitor-sdk-exchange";


    public static IPush createRabbitPush(RabbitMqProperties rabbitMqProperties) {
        if (rabbitMqProperties == null) {
            throw new IllegalStateException("RabbitMqConfig配置有误");
        }
        if (rabbitMqProperties.getHost() == null || rabbitMqProperties.getHost().trim().isEmpty()) {
            throw new IllegalStateException("RabbitMqConfig 中的 host 配置不能为空");
        }
        if (rabbitMqProperties.getPort() <= 0) {
            throw new IllegalStateException("RabbitMqConfig 中的 port 配置必须大于 0");
        }
        if (rabbitMqProperties.getUsername() == null || rabbitMqProperties.getUsername().trim().isEmpty()) {
            throw new IllegalStateException("RabbitMqConfig 中的 username 配置不能为空");
        }
        if (rabbitMqProperties.getPassword() == null || rabbitMqProperties.getPassword().trim().isEmpty()) {
            throw new IllegalStateException("RabbitMqConfig 中的 password 配置不能为空");
        }

        try {
            ConnectionFactory connectionFactory = createConnectionFactory(rabbitMqProperties);
            Connection connection = connectionFactory.newConnection(); // 不使用 try-with-resources
            Channel channel = connection.createChannel();

            // 创建交换机
            createTopicExchange(channel);
            // 创建队列
            createQueue(channel, rabbitMqProperties.getTopic());
            // 创建绑定关系
            createBinding(channel, rabbitMqProperties.getTopic());

            return new RabbitPush(rabbitMqProperties.getTopic(), channel, EXCHANGE_NAME, connection);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException("创建RabbitMQ连接失败", e);
        }
    }

    private static ConnectionFactory createConnectionFactory(RabbitMqProperties rabbitMqProperties) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitMqProperties.getHost());
        connectionFactory.setPort(rabbitMqProperties.getPort());
        connectionFactory.setUsername(rabbitMqProperties.getUsername());
        connectionFactory.setPassword(rabbitMqProperties.getPassword());
        String virtualHost = rabbitMqProperties.getVirtualHost();
        if (virtualHost != null && !virtualHost.trim().isEmpty()) {
            connectionFactory.setVirtualHost(virtualHost);
        }

        return connectionFactory;
    }

    private static void createQueue(Channel channel, String queueName) throws IOException {
        Map<String, Object> argsMap = new HashMap<>();
        // 声明队列：队列名, 持久化, 独占, 自动删除, 参数
        channel.queueDeclare(queueName, true, false, false, argsMap);
    }

    private static void createTopicExchange(Channel channel) throws IOException {
        // 声明交换机：交换机名, 类型, 持久化, 自动删除, 内部使用, 参数
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC, true, false, false, null);
    }

    private static void createBinding(Channel channel, String queueName) throws IOException {
        // 绑定队列到交换机：队列名, 交换机名, 路由键, 参数
        channel.queueBind(queueName, EXCHANGE_NAME, queueName, null);
    }
}

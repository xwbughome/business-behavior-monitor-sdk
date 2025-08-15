package top.bughome.monitor.sdk.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import top.bughome.monitor.sdk.properties.RabbitMqProperties;
import top.bughome.monitor.sdk.push.IPush;
import top.bughome.monitor.sdk.push.impl.RabbitPush;

import java.util.HashMap;
import java.util.Map;

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

        ConnectionFactory connectionFactory = createConnectionFactory(rabbitMqProperties);
        RabbitAdmin rabbitAdmin = createRabbitAdmin(connectionFactory);

        // 创建交换机
        TopicExchange exchange = createTopicExchange(rabbitAdmin);
        // 创建队列
        Queue queue = createQueue(rabbitAdmin, rabbitMqProperties.getTopic());
        // 创建绑定关系
        createBinding(rabbitAdmin, queue, exchange, rabbitMqProperties.getTopic());

        RabbitTemplate rabbitTemplate = createRabbitTemplate(connectionFactory);
        return new RabbitPush(rabbitMqProperties.getTopic(), rabbitTemplate);
    }

    private static ConnectionFactory createConnectionFactory(RabbitMqProperties rabbitMqProperties) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitMqProperties.getHost());
        connectionFactory.setPort(rabbitMqProperties.getPort());
        connectionFactory.setUsername(rabbitMqProperties.getUsername());
        connectionFactory.setPassword(rabbitMqProperties.getPassword());
        connectionFactory.setVirtualHost(rabbitMqProperties.getVirtualHost());
        return connectionFactory;
    }

    private static RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    private static RabbitAdmin createRabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    private static Queue createQueue(RabbitAdmin rabbitAdmin, String queueName) {
        Map<String, Object> argsMap = new HashMap<>();
        Queue queue = new Queue(queueName, true, false, false, argsMap);
        // 声明队列
        rabbitAdmin.declareQueue(queue);
        return queue;
    }

    private static TopicExchange createTopicExchange(RabbitAdmin rabbitAdmin) {
        TopicExchange exchange = new TopicExchange(EXCHANGE_NAME, true, false);
        // 声明交换机
        rabbitAdmin.declareExchange(exchange);
        return exchange;
    }

    private static Binding createBinding(RabbitAdmin rabbitAdmin, Queue queue, TopicExchange exchange, String routingKey) {
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
        // 声明绑定关系
        rabbitAdmin.declareBinding(binding);
        return binding;
    }
}

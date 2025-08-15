package top.bughome.monitor.sdk.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import top.bughome.monitor.sdk.config.ChannelConfig;
import top.bughome.monitor.sdk.config.KafkaConfig;
import top.bughome.monitor.sdk.config.RedisConfig;
import top.bughome.monitor.sdk.model.LogMessage;
import top.bughome.monitor.sdk.push.IPush;
import top.bughome.monitor.sdk.push.PushConfig;

import java.util.Arrays;

/**
 * 自定义日志采集
 * Created by MaxWell on 2025/8/12 16:15
 */
public class BehaviorMonitorAppender<E> extends UnsynchronizedAppenderBase<E> {

    /**
     * 系统名称
     */
    private String systemName;
    /**
     * 只采集制定范围的日志
     */
    private String groupId;
    /**
     *
     */
    private String channel;
    /**
     * kafka配置
     */
    private KafkaConfig kafkaConfig;
    /**
     * redis配置
     */
    private RedisConfig redisConfig;

    private IPush push;

    @Override
    public void start() {
        try {
            // 验证必要配置
            if (channel == null || channel.trim().isEmpty()) {
                addError("推送渠道 channel 不能为空");
                return;
            }

            // 验证渠道与配置的匹配性
            validateChannelConfig();

            // 创建渠道配置
            ChannelConfig channelConfig = new ChannelConfig(kafkaConfig, redisConfig);
            // 通过工厂获取推送实例
            this.push = PushConfig.getPush(channel, channelConfig);

            addInfo("业务行为监控组件启动成功，推送渠道：" + channel);
            super.start();
        } catch (Exception e) {
            addError("业务行为监控组件启动失败：" + e.getMessage(), e);
        }
    }

    @Override
    protected void append(E eventObject) {
        // 获取日志
        if (eventObject instanceof ILoggingEvent event) {

            String methodName = "unknown";
            String className = "unknown";

            StackTraceElement[] callerData = event.getCallerData();
            if (callerData != null && callerData.length > 0) {
                StackTraceElement element = callerData[0];
                methodName = element.getMethodName();
                className = element.getClassName();
            }

            if (!className.startsWith(groupId)) return;

            addInfo("**************准备开始执行参数封装******************");
            // 构建日志消息对象
            LogMessage logMessage = new LogMessage(systemName, className, methodName, Arrays.asList(event.getFormattedMessage().split(" ")));
            // 推送日志
            push.send(logMessage);
        }
    }

    /**
     * 验证渠道与配置的匹配性
     */
    private void validateChannelConfig() {
        switch (channel.toLowerCase()) {
            case "kafka":
                if (kafkaConfig == null) {
                    throw new IllegalStateException("kafkaConfig配置有误");
                }
                break;
            case "redis":
                if (redisConfig == null) {
                    throw new IllegalStateException("redisConfig配置有误");
                }
                break;
            default:
                throw new IllegalArgumentException("不支持的推送渠道类型: " + channel);
        }
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public KafkaConfig getKafkaConfig() {
        return kafkaConfig;
    }

    public void setKafkaConfig(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }

    public void setRedisConfig(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }
}

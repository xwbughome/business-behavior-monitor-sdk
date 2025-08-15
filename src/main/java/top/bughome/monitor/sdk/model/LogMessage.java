package top.bughome.monitor.sdk.model;

import java.util.List;

/**
 * 日志消息
 * Created by MaxWell on 2025/8/12 16:31
 */
public class LogMessage {
    /**
     * 系统名称
     */
    private String systemName;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 日志列表
     */
    private List<String> logList;

    public LogMessage() {
    }

    public LogMessage(String systemName, String className, String methodName, List<String> logList) {
        this.systemName = systemName;
        this.className = className;
        this.methodName = methodName;
        this.logList = logList;
    }

    public String getSystemName() {
        return systemName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<String> getLogList() {
        return logList;
    }
}

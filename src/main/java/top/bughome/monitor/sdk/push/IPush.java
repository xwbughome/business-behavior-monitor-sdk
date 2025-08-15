package top.bughome.monitor.sdk.push;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.bughome.monitor.sdk.model.LogMessage;

/**
 * 发布接口
 * Created by MaxWell on 2025/8/12 16:37
 */
public interface IPush {

    Logger logger = LoggerFactory.getLogger(IPush.class);

    Gson gson = new Gson();

    void send(LogMessage logMessage);
}

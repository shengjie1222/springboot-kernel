package com.ethereal.kernel.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Jie Jie
 * date 2024-03-21
 */
@Data
@Component
@ConfigurationProperties(prefix = "netty.socket")
public class NettySocketProperties {
    /**
     * Websocket服务端口
     */
    private int port;

    /**
     * 消息帧最大体积
     */
    private int maxFrameSize;

}

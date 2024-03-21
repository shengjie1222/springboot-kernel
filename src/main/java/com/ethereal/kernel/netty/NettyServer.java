package com.ethereal.kernel.netty;

import com.ethereal.kernel.netty.handler.PrimevalDataHandler;
import com.ethereal.kernel.properties.NettySocketProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * 终端服务
 * @author JieJie
 * @time  2024/03/21 14:11:11
 */
@Slf4j
@Component
public class NettyServer implements ApplicationRunner {

    @Autowired
    private NettySocketProperties nettySocketProperties;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workGroup = new NioEventLoopGroup();


    @Override
    public void run(ApplicationArguments args) throws Exception {
        start();
    }

    private void start(){
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,256)
                    .option(ChannelOption.SO_REUSEADDR,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //定义分割字符，防止粘包，拆包
                            ByteBuf delimiter= Unpooled.copiedBuffer("$".getBytes());
                            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(nettySocketProperties.getMaxFrameSize(),delimiter));
                            socketChannel.pipeline().addLast(new PrimevalDataHandler());
                        }
                    });
            log.info("netty started. port: {} ,max frame size :{}",nettySocketProperties.getPort(),nettySocketProperties.getMaxFrameSize());
            ChannelFuture sync = bootstrap.bind(nettySocketProperties.getPort()).sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("netty interrupted.",e);
        }
    }

    @PreDestroy
    public void destroy() {
        this.bossGroup.shutdownGracefully();
        this.workGroup.shutdownGracefully();
        log.info("netty destroy.");
    }

}

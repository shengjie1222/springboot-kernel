package com.ethereal.kernel.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ethereal
 * @Title: PrimevalDataHandler原始数据处理程序
 * @ProjectName temperatures
 * @Description: 打印原始数据
 * @date 2018/11/2610:02
 */
public class PrimevalDataHandler extends ChannelInboundHandlerAdapter {
    private static Logger log= LoggerFactory.getLogger(PrimevalDataHandler.class);
    /**
     * 获取数据
     * @param ctx 上下文
     * @param msg 获取的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf readMessage= (ByteBuf) msg;
        log.info("pass through data: {}",readMessage.toString(CharsetUtil.UTF_8));
        super.channelRead(ctx,msg);
    }
}

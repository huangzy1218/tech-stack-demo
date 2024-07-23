package org.example.netty.client;

/**
 * @author Huang Z.Y.
 */

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty 客户端示例
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class NettyClient {
    private Bootstrap bootstrap;
    private EventLoopGroup group;
    /**
     * 保存已建立的管道
     */
    private Channel channel;

    public NettyClient() {
        // 初始化 Netty 组件
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        // 添加解码器和编码器
                        ch.pipeline()
                                .addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
                                .addLast("stringDecoder", new StringDecoder())
                                .addLast("frameEncoder", new LengthFieldPrepender(2))
                                .addLast("stringEncoder", new StringEncoder())
                                .addLast("handler", new NettyClientHandler());
                    }
                });
    }

    /**
     * 连接到指定的服务器地址和端口
     *
     * @param host 服务器主机名或 IP 地址
     * @param port 服务器端口号
     */
    public void connect(String host, int port) {
        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();
            // 存储频道以供以后使用
            this.channel = future.channel();
            future.channel().closeFuture().addListener(f -> group.shutdownGracefully());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("连接过程中被中断", e);
        }
    }

    /**
     * 发送消息到服务器
     *
     * @param message 要发送的消息内容
     */
    public void send(String message) {
        if (group.isShuttingDown() || group.isShutdown()) {
            throw new IllegalStateException("客户端已关闭或正在关闭");
        }
        if (channel == null || !channel.isActive()) {
            throw new IllegalStateException("连接未建立或已关闭");
        }
        // 向服务器发送消息
        channel.writeAndFlush(message);
    }
}

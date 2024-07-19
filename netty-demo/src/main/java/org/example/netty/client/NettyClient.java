package org.example.netty.client;

/**
 * @author Huang Z.Y.
 */

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty 客户端示例
 */
@Slf4j
public class NettyClient {

    private Bootstrap bootstrap;
    private EventLoopGroup group;

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
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("连接过程中被中断", e);
        } finally {
            group.shutdownGracefully();
        }
    }

    /**
     * 发送消息到服务器
     *
     * @param message 要发送的消息内容
     */
    public void sendMessage(String message) {
        if (group.isShuttingDown() || group.isShutdown()) {
            throw new IllegalStateException("客户端已关闭或正在关闭");
        }
        // 获取当前连接的 ChannelFuture
        ChannelFuture future = bootstrap.connect().syncUninterruptibly();
        // 向服务器发送消息
        future.channel().writeAndFlush(message);
        log.info("发送消息到服务器: {}", message);
    }

}

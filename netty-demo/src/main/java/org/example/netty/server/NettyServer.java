package org.example.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Huang Z.Y.
 */
@Slf4j
public class NettyServer {

    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public NettyServer() {
        // 初始化 Netty 组件
        bootstrap = new ServerBootstrap();
        // 1 个 boss 线程
        bossGroup = new NioEventLoopGroup(1);
        // 默认的 worker 线程数
        workerGroup = new NioEventLoopGroup();

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<NioServerSocketChannel>() {
                    @Override
                    protected void initChannel(NioServerSocketChannel ch) {
                        ch.pipeline()
                                .addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
                                .addLast("stringDecoder", new StringDecoder())
                                .addLast("frameEncoder", new LengthFieldPrepender(2))
                                .addLast("stringEncoder", new StringEncoder())
                                .addLast("handler", new NettyServerHandler());
                    }
                });
    }

    /**
     * 启动 Netty 服务器并绑定指定端口
     *
     * @param port 要绑定的端口号
     */
    public void start(int port) {
        try {
            bootstrap.bind(port).sync();
            log.info("Netty 服务器启动成功，监听端口: {}", port);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("等待 Netty 服务器启动过程中被中断", e);
        }
    }

    /**
     * 关闭 Netty 服务器，释放资源
     */
    public void shutdown() {
        try {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            log.info("Netty 服务器已优雅关闭");
        } catch (Exception e) {
            log.error("关闭 Netty 服务器时出现异常", e);
        }
    }
}

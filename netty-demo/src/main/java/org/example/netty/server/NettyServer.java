package org.example.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty 服务端
 *
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
                // 表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                .option(ChannelOption.SO_BACKLOG, 128)
                // TCP 默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle
                .childOption(ChannelOption.TCP_NODELAY, true)
                // 是否开启 TCP 底层心跳机制
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
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
            // 关闭所有的 Child Channel
            workerGroup.shutdownGracefully();
            log.info("Netty 服务器已关闭");
        } catch (Exception e) {
            log.error("关闭 Netty 服务器时出现异常", e);
        }
    }
}

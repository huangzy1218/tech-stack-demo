package org.example.netty.server;

/**
 * @author Huang Z.Y.
 */
public class NettyServerApplication {

    public static void main(String[] args) {
        NettyServer server = new NettyServer();
        server.start(8081);
    }

}

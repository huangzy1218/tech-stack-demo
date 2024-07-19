package org.example.netty;

import org.example.netty.client.NettyClient;
import org.example.netty.server.NettyServer;

/**
 * @author Huang Z.Y.
 */
public class NettyApplication {

    public static void main(String[] args) {
        NettyServer server = new NettyServer();
        server.start(8081);
        NettyClient client = new NettyClient();
        client.connect("localhost", 8081);
        client.sendMessage("Hello World");
    }

}

package org.example.netty.client;

/**
 * @author Huang Z.Y.
 */
public class NettyClientApplication {

    public static void main(String[] args) {
        NettyClient client = new NettyClient();
        client.connect("127.0.0.1", 8081);
        client.send("Hello World");
    }

}

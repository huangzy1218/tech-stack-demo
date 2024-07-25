package org.example.concurrent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Huang Z.Y.
 */
public class ThreadLocalDemo {
    // 携带初始化的 ThreadLocalDemo
    public static final ThreadLocal<ThreadLocalDemo> holder = ThreadLocal.withInitial(ThreadLocalDemo::new);
    private List<String> messages = new ArrayList<>();

    public static void add(String message) {
        holder.get().messages.add(message);
        System.out.println("Add a message, size: " + holder.get().messages.size());
    }

    public static List<String> clear() {
        List<String> messages = holder.get().messages;
        holder.remove();

        System.out.println("Clear all messages, size: " + holder.get().messages.size());
        return messages;
    }

    public static void main(String[] args) {
        ThreadLocalDemo.add("Hello World");
        System.out.println(holder.get().messages);
        ThreadLocalDemo.clear();
    }
}

package org.example.concurrent.collection;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Huang Z.Y.
 */
public class ConcurrentHashMapDemo {
    public static void main(String[] args) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("John", "Doe");
        map.put("Jane", "Doe");
        System.out.println(map.get("John"));
    }
}

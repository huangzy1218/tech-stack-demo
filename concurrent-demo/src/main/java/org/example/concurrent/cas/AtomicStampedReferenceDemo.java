package org.example.concurrent.cas;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author Huang Z.Y.
 */
public class AtomicStampedReferenceDemo {
    public static void main(String[] args) {
        // 初始引用值为 "Initial"，初始标记为 0
        AtomicStampedReference<String> atomicStampedRef = new AtomicStampedReference<>("Initial", 0);

        // 获取当前引用值和标记
        int[] stampHolder = new int[1];
        String currentRef = atomicStampedRef.get(stampHolder);
        int currentStamp = stampHolder[0];
        System.out.println("Current reference: " + currentRef + ", stamp: " + currentStamp);

        // 尝试更新引用和标记
        boolean success = atomicStampedRef.compareAndSet("Initial", "New Value", currentStamp, currentStamp + 1);
        if (success) {
            System.out.println("Reference updated successfully.");
        } else {
            System.out.println("Reference update failed.");
        }

        // 获取更新后的引用值和标记
        currentRef = atomicStampedRef.getReference();
        currentStamp = atomicStampedRef.getStamp();
        System.out.println("Updated reference: " + currentRef + ", stamp: " + currentStamp);
    }
}

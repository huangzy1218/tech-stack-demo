package org.example.concurrent.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Huang Z.Y.
 */
public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // thenApply 有返回值
        CompletableFuture<String> future = CompletableFuture.completedFuture("Hello ")
                .thenApply(s -> s + " World!");
        // 被忽略
        future.thenApply(s -> s + " Java!");
        System.out.println(future.get());
        //------------------------------------------------------------------------------

        // thenAccept 无返回值
        CompletableFuture future2 = CompletableFuture.completedFuture("Hello ");
        future2.thenApply(s -> s + " Java!").thenAccept(System.out::println);
        //------------------------------------------------------------------------------
        // thenRun
        CompletableFuture future3 = CompletableFuture.completedFuture("Hello ");
        // 和异步结果无关
        future3.thenApply(s -> s + " Java!").thenRun(() -> System.out.println("Execute successfully"));
        //------------------------------------------------------------------------------

        // whenComplete，可接收 2 个参数进行消费
        CompletableFuture<String> future4 = CompletableFuture.supplyAsync(() -> "Hello Future4")
                .whenComplete((res, ex) -> {
                    // res 代表返回的结果
                    // ex 的类型为 Throwable ，代表抛出的异常
                    System.out.println(res);
                    // 这里没有抛出异常所有为 null
                });
        System.out.println(future4.get());
        //------------------------------------------------------------------------------

        // 异常处理
        CompletableFuture<String> future5
                = CompletableFuture.supplyAsync(() -> {
            if (true) {
                throw new RuntimeException("Computation error");
            }
            return "Hello";
        }).exceptionally(ex -> {
            // CompletionException
            System.out.println(ex.toString());
            return "World";
        });
        System.out.println(future5.get());

        // CompletableFuture 的结果是异常
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        completableFuture.completeExceptionally(
                new RuntimeException("Calculation failed!"));
//        completableFuture.get();

        // 异步任务编排
        CompletableFuture<String> future6
                = CompletableFuture.supplyAsync(() -> "Hello ")
                .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + "Future6"));
        System.out.println(future6.get());

    }
}

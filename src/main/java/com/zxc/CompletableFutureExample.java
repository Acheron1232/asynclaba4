package com.zxc;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CompletableFutureExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();

        CompletableFuture<int[]> generateArray = CompletableFuture.supplyAsync(() -> {
            int[] array = new Random().ints(10, 1, 20).toArray();
            System.out.println("Generated array: " + Arrays.toString(array));
            return array;
        });

        CompletableFuture<int[]> incrementedArray = generateArray.thenApplyAsync(array -> {
            int[] incremented = Arrays.stream(array).map(x -> x + 5).toArray();
            System.out.println("Incremented array: " + Arrays.toString(incremented));
            return incremented;
        });

        CompletableFuture<Void> factorialCalculation = incrementedArray.thenCombineAsync(
            generateArray,
            (incremented, original) -> Arrays.stream(incremented).sum() + Arrays.stream(original).sum()
        ).thenApplyAsync(sum -> {
            System.out.println("Sum of elements: " + sum);
            return calculateFactorialBig(sum);
        }).thenAcceptAsync(factorial -> 
            System.out.println("Factorial: " + factorial)
        );

        factorialCalculation.thenRunAsync(() -> {
            long end = System.currentTimeMillis();
            System.out.println("Time elapsed: " + (end - start) + " ms");
        }).get();
    }



    private static BigInteger calculateFactorialBig(int n) {
        BigInteger factorial = BigInteger.ONE;
        for (int i = 1; i <= n; i++) {
            factorial = factorial.multiply(BigInteger.valueOf(i));
        }
        return factorial;
    }


}

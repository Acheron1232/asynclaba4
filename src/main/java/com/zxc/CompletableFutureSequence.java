package com.zxc;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CompletableFutureSequence {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();

        CompletableFuture<List<Integer>> generateSequence = CompletableFuture.supplyAsync(() -> {
            List<Integer> sequence = new Random().ints(20, 1, 50).boxed().collect(Collectors.toList());
            System.out.println("Generated sequence: " + sequence);
            return sequence;
        });

        CompletableFuture<Integer> sum = generateSequence.thenApplyAsync(sequence -> {
            int totalSum = sequence.stream().mapToInt(Integer::intValue).sum();
            System.out.println("Sum of sequence: " + totalSum);
            return totalSum;
        });

        CompletableFuture<Void> squaredSum = sum.thenApplyAsync(totalSum -> {
            int squared = totalSum * totalSum;
            System.out.println("Squared sum: " + squared);
            return squared;
        }).thenAcceptAsync(result -> System.out.println("Result: " + result));

        squaredSum.thenRunAsync(() -> {
            long end = System.currentTimeMillis();
            System.out.println("Total time elapsed: " + (end - start) + " ms");
        }).get();
    }
}

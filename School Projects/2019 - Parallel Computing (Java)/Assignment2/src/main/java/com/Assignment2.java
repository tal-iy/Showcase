package com;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;

/**
 * Assignment: CSC375 Assignment 2
 * Author: Vitaliy Shydlonok
 * Date: 10/28/2019
 */

@Fork(3)
@State(Scope.Group)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Assignment2 {

    /**
     * Program entry, sets up JMH and runs the benchmarks
     * @param args
     */
    public static void main(String[] args) throws RunnerException {
        org.openjdk.jmh.runner.options.Options opt = new OptionsBuilder()
                .include(Assignment2.class.getSimpleName())
                .shouldDoGC(true)
                .resultFormat(ResultFormatType.CSV)
                .result("result.csv")
                .shouldFailOnError(true)
                .build();

        new Runner(opt).run();
    }

    @State(Scope.Group)
    public static class ListsState {

        @Param({"20", "40"})
        public int CHANCE;

        public List<Item> jdkList; // Synchronized list from JDK
        public List<Item> myList; // My implementation of a concurrent list using a mutex lock

        /**
         * Initializes my concurrent list and the JDK synchronized list for benchmarking
         */
        @Setup
        public void myInit() {
            // Create and populate the lists with 1000 random items
            List<Item> items = new ArrayList<>();
            myList = new ConcurrentList<>();
            for (int i = 0; i < 1000; i++) {
                myList.add(new Item());
                items.add(new Item());
            }
            jdkList = Collections.synchronizedList(new ArrayList<>(items));
        }

        /**
         * Benchmark for my concurrent list implementation, choosing randomly to either
         * add an element or remove an element from the list.
         * Adds 80% of the time, removes 20% of the time.
         */
        public void mySimHelper(Blackhole hole) {
            // Randomly choose to drop or look at an item
            if (ThreadLocalRandom.current().nextInt(0, 100) < CHANCE) {
                // Create a new item and add it to the list (player drops an item)
                Item item = new Item();
                hole.consume(myList.add(item));
            } else {
                // Pick a random item in the list and get it (player looks at an item)
                int index = ThreadLocalRandom.current().nextInt(0, myList.size() - 1);
                // Get the item from the list
                hole.consume(myList.get(index));
            }
        }

        /**
         * Benchmark for the JDK synchronized list, choosing randomly to either
         * add an element or remove an element from the list.
         * Adds 80% of the time, removes 20% of the time.
         */
        public void jdkSimHelper(Blackhole hole) {
            // Randomly choose to drop or look at an item
            if (ThreadLocalRandom.current().nextInt(0, 100) < CHANCE) {
                // Create a new item and add it to the list (player drops an item)
                Item item = new Item();
                hole.consume(jdkList.add(item));
            } else {
                // Pick a random item in the list and get it (player looks at an item)
                int index = ThreadLocalRandom.current().nextInt(0, jdkList.size() - 1);
                // Get the item from the list
                hole.consume(jdkList.get(index));
            }
        }
    }

    /**
     * Benchmark 1 Threads
     */
    @Benchmark
    @Group("myList1")
    @GroupThreads(1)
    public void mySim1(ListsState state, Blackhole hole) {
        state.mySimHelper(hole);
    }

    /**
     * Benchmark 2 Threads
     */
    @Benchmark
    @Group("myList2")
    @GroupThreads(2)
    public void mySim2(ListsState state, Blackhole hole) {
        state.mySimHelper(hole);
    }

    /**
     * Benchmark 4 Threads
     */
    @Benchmark
    @Group("myList4")
    @GroupThreads(4)
    public void mySim4(ListsState state, Blackhole hole) {
        state.mySimHelper(hole);
    }

    /**
     * Benchmark 8 Threads
     */
    @Benchmark
    @Group("myList8")
    @GroupThreads(8)
    public void mySim8(ListsState state, Blackhole hole) {
        state.mySimHelper(hole);
    }

    /**
     * Benchmark 16 Threads
     */
    @Benchmark
    @Group("myList16")
    @GroupThreads(16)
    public void mySim16(ListsState state, Blackhole hole) {
        state.mySimHelper(hole);
    }

    /**
     * Benchmark 1 Threads
     */
    @Benchmark
    @Group("jdkList1")
    @GroupThreads(1)
    public void jdkSim1(ListsState state, Blackhole hole) {
        state.jdkSimHelper(hole);
    }

    /**
     * Benchmark 2 Threads
     */
    @Benchmark
    @Group("jdkList2")
    @GroupThreads(2)
    public void jdkSim2(ListsState state, Blackhole hole) {
        state.jdkSimHelper(hole);
    }

    /**
     * Benchmark 4 Threads
     */
    @Benchmark
    @Group("jdkList4")
    @GroupThreads(4)
    public void jdkSim4(ListsState state, Blackhole hole) {
        state.jdkSimHelper(hole);
    }

    /**
     * Benchmark 8 Threads
     */
    @Benchmark
    @Group("jdkList8")
    @GroupThreads(8)
    public void jdkSim8(ListsState state, Blackhole hole) {
        state.jdkSimHelper(hole);
    }

    /**
     * Benchmark 16 Threads
     */
    @Benchmark
    @Group("jdkList16")
    @GroupThreads(16)
    public void jdkSim16(ListsState state, Blackhole hole) {
        state.jdkSimHelper(hole);
    }
}

package com.generated;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Collection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.runner.InfraControl;
import org.openjdk.jmh.infra.ThreadParams;
import org.openjdk.jmh.results.BenchmarkTaskResult;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.ThroughputResult;
import org.openjdk.jmh.results.AverageTimeResult;
import org.openjdk.jmh.results.SampleTimeResult;
import org.openjdk.jmh.results.SingleShotResult;
import org.openjdk.jmh.util.SampleBuffer;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.results.RawResults;
import org.openjdk.jmh.results.ResultRole;
import java.lang.reflect.Field;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.IterationParams;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.infra.Control;
import org.openjdk.jmh.results.ScalarResult;
import org.openjdk.jmh.results.AggregationPolicy;
import org.openjdk.jmh.runner.FailureAssistException;

import com.generated.Assignment2_jmhType;
import com.generated.Assignment2_ListsState_jmhType;
public final class Assignment2_jdkList1_jmhTest {

    boolean p000, p001, p002, p003, p004, p005, p006, p007, p008, p009, p010, p011, p012, p013, p014, p015;
    boolean p016, p017, p018, p019, p020, p021, p022, p023, p024, p025, p026, p027, p028, p029, p030, p031;
    boolean p032, p033, p034, p035, p036, p037, p038, p039, p040, p041, p042, p043, p044, p045, p046, p047;
    boolean p048, p049, p050, p051, p052, p053, p054, p055, p056, p057, p058, p059, p060, p061, p062, p063;
    boolean p064, p065, p066, p067, p068, p069, p070, p071, p072, p073, p074, p075, p076, p077, p078, p079;
    boolean p080, p081, p082, p083, p084, p085, p086, p087, p088, p089, p090, p091, p092, p093, p094, p095;
    boolean p096, p097, p098, p099, p100, p101, p102, p103, p104, p105, p106, p107, p108, p109, p110, p111;
    boolean p112, p113, p114, p115, p116, p117, p118, p119, p120, p121, p122, p123, p124, p125, p126, p127;
    boolean p128, p129, p130, p131, p132, p133, p134, p135, p136, p137, p138, p139, p140, p141, p142, p143;
    boolean p144, p145, p146, p147, p148, p149, p150, p151, p152, p153, p154, p155, p156, p157, p158, p159;
    boolean p160, p161, p162, p163, p164, p165, p166, p167, p168, p169, p170, p171, p172, p173, p174, p175;
    boolean p176, p177, p178, p179, p180, p181, p182, p183, p184, p185, p186, p187, p188, p189, p190, p191;
    boolean p192, p193, p194, p195, p196, p197, p198, p199, p200, p201, p202, p203, p204, p205, p206, p207;
    boolean p208, p209, p210, p211, p212, p213, p214, p215, p216, p217, p218, p219, p220, p221, p222, p223;
    boolean p224, p225, p226, p227, p228, p229, p230, p231, p232, p233, p234, p235, p236, p237, p238, p239;
    boolean p240, p241, p242, p243, p244, p245, p246, p247, p248, p249, p250, p251, p252, p253, p254, p255;
    int startRndMask;
    BenchmarkParams benchmarkParams;
    IterationParams iterationParams;
    ThreadParams threadParams;
    Blackhole blackhole;
    Control notifyControl;

    public BenchmarkTaskResult jdkList1_Throughput(InfraControl control, ThreadParams threadParams) throws Throwable {
        this.benchmarkParams = control.benchmarkParams;
        this.iterationParams = control.iterationParams;
        this.threadParams    = threadParams;
        this.notifyControl   = control.notifyControl;
        if (this.blackhole == null) {
            this.blackhole = new Blackhole("Today's password is swordfish. I understand instantiating Blackholes directly is dangerous.");
        }
        if (threadParams.getSubgroupIndex() == 0) {
            RawResults res = new RawResults();
            Assignment2_jmhType l_assignment20_G = _jmh_tryInit_f_assignment20_G(control);
            Assignment2_ListsState_jmhType l_listsstate1_G = _jmh_tryInit_f_listsstate1_G(control);

            control.preSetup();


            control.announceWarmupReady();
            while (control.warmupShouldWait) {
                l_assignment20_G.jdkSim1(l_listsstate1_G, blackhole);
                res.allOps++;
            }

            notifyControl.startMeasurement = true;
            jdkSim1_thrpt_jmhStub(control, res, benchmarkParams, iterationParams, threadParams, blackhole, notifyControl, startRndMask, l_listsstate1_G, l_assignment20_G);
            notifyControl.stopMeasurement = true;
            control.announceWarmdownReady();
            try {
                while (control.warmdownShouldWait) {
                    l_assignment20_G.jdkSim1(l_listsstate1_G, blackhole);
                    res.allOps++;
                }
                control.preTearDown();
            } catch (InterruptedException ie) {
                control.preTearDownForce();
            }

            if (control.isLastIteration()) {
                if (Assignment2_ListsState_jmhType.tearTrialMutexUpdater.compareAndSet(l_listsstate1_G, 0, 1)) {
                    try {
                        if (control.isFailing) throw new FailureAssistException();
                        if (l_listsstate1_G.readyTrial) {
                            l_listsstate1_G.readyTrial = false;
                        }
                    } catch (Throwable t) {
                        control.isFailing = true;
                        throw t;
                    } finally {
                        Assignment2_ListsState_jmhType.tearTrialMutexUpdater.set(l_listsstate1_G, 0);
                    }
                } else {
                    long l_listsstate1_G_backoff = 1;
                    while (Assignment2_ListsState_jmhType.tearTrialMutexUpdater.get(l_listsstate1_G) == 1) {
                        TimeUnit.MILLISECONDS.sleep(l_listsstate1_G_backoff);
                        l_listsstate1_G_backoff = Math.max(1024, l_listsstate1_G_backoff * 2);
                        if (control.isFailing) throw new FailureAssistException();
                        if (Thread.interrupted()) throw new InterruptedException();
                    }
                }
                synchronized(this.getClass()) {
                    f_listsstate1_G_map.remove(threadParams.getGroupIndex());
                }
                synchronized(this.getClass()) {
                    f_assignment20_G_map.remove(threadParams.getGroupIndex());
                }
            }
            res.allOps += res.measuredOps;
            int batchSize = iterationParams.getBatchSize();
            int opsPerInv = benchmarkParams.getOpsPerInvocation();
            res.allOps *= opsPerInv;
            res.allOps /= batchSize;
            res.measuredOps *= opsPerInv;
            res.measuredOps /= batchSize;
            BenchmarkTaskResult results = new BenchmarkTaskResult(res.allOps, res.measuredOps);
            results.add(new ThroughputResult(ResultRole.PRIMARY, "jdkSim1", res.measuredOps, res.getTime(), benchmarkParams.getTimeUnit()));
            this.blackhole.evaporate("Yes, I am Stephen Hawking, and know a thing or two about black holes.");
            return results;
        } else
            throw new IllegalStateException("Harness failed to distribute threads among groups properly");
    }

    public static void jdkSim1_thrpt_jmhStub(InfraControl control, RawResults result, BenchmarkParams benchmarkParams, IterationParams iterationParams, ThreadParams threadParams, Blackhole blackhole, Control notifyControl, int startRndMask, Assignment2_ListsState_jmhType l_listsstate1_G, Assignment2_jmhType l_assignment20_G) throws Throwable {
        long operations = 0;
        long realTime = 0;
        result.startTime = System.nanoTime();
        do {
            l_assignment20_G.jdkSim1(l_listsstate1_G, blackhole);
            operations++;
        } while(!control.isDone);
        result.stopTime = System.nanoTime();
        result.realTime = realTime;
        result.measuredOps = operations;
    }


    public BenchmarkTaskResult jdkList1_AverageTime(InfraControl control, ThreadParams threadParams) throws Throwable {
        this.benchmarkParams = control.benchmarkParams;
        this.iterationParams = control.iterationParams;
        this.threadParams    = threadParams;
        this.notifyControl   = control.notifyControl;
        if (this.blackhole == null) {
            this.blackhole = new Blackhole("Today's password is swordfish. I understand instantiating Blackholes directly is dangerous.");
        }
        if (threadParams.getSubgroupIndex() == 0) {
            RawResults res = new RawResults();
            Assignment2_jmhType l_assignment20_G = _jmh_tryInit_f_assignment20_G(control);
            Assignment2_ListsState_jmhType l_listsstate1_G = _jmh_tryInit_f_listsstate1_G(control);

            control.preSetup();


            control.announceWarmupReady();
            while (control.warmupShouldWait) {
                l_assignment20_G.jdkSim1(l_listsstate1_G, blackhole);
                res.allOps++;
            }

            notifyControl.startMeasurement = true;
            jdkSim1_avgt_jmhStub(control, res, benchmarkParams, iterationParams, threadParams, blackhole, notifyControl, startRndMask, l_listsstate1_G, l_assignment20_G);
            notifyControl.stopMeasurement = true;
            control.announceWarmdownReady();
            try {
                while (control.warmdownShouldWait) {
                    l_assignment20_G.jdkSim1(l_listsstate1_G, blackhole);
                    res.allOps++;
                }
                control.preTearDown();
            } catch (InterruptedException ie) {
                control.preTearDownForce();
            }

            if (control.isLastIteration()) {
                if (Assignment2_ListsState_jmhType.tearTrialMutexUpdater.compareAndSet(l_listsstate1_G, 0, 1)) {
                    try {
                        if (control.isFailing) throw new FailureAssistException();
                        if (l_listsstate1_G.readyTrial) {
                            l_listsstate1_G.readyTrial = false;
                        }
                    } catch (Throwable t) {
                        control.isFailing = true;
                        throw t;
                    } finally {
                        Assignment2_ListsState_jmhType.tearTrialMutexUpdater.set(l_listsstate1_G, 0);
                    }
                } else {
                    long l_listsstate1_G_backoff = 1;
                    while (Assignment2_ListsState_jmhType.tearTrialMutexUpdater.get(l_listsstate1_G) == 1) {
                        TimeUnit.MILLISECONDS.sleep(l_listsstate1_G_backoff);
                        l_listsstate1_G_backoff = Math.max(1024, l_listsstate1_G_backoff * 2);
                        if (control.isFailing) throw new FailureAssistException();
                        if (Thread.interrupted()) throw new InterruptedException();
                    }
                }
                synchronized(this.getClass()) {
                    f_listsstate1_G_map.remove(threadParams.getGroupIndex());
                }
                synchronized(this.getClass()) {
                    f_assignment20_G_map.remove(threadParams.getGroupIndex());
                }
            }
            res.allOps += res.measuredOps;
            int batchSize = iterationParams.getBatchSize();
            int opsPerInv = benchmarkParams.getOpsPerInvocation();
            res.allOps *= opsPerInv;
            res.allOps /= batchSize;
            res.measuredOps *= opsPerInv;
            res.measuredOps /= batchSize;
            BenchmarkTaskResult results = new BenchmarkTaskResult(res.allOps, res.measuredOps);
            results.add(new AverageTimeResult(ResultRole.PRIMARY, "jdkSim1", res.measuredOps, res.getTime(), benchmarkParams.getTimeUnit()));
            this.blackhole.evaporate("Yes, I am Stephen Hawking, and know a thing or two about black holes.");
            return results;
        } else
            throw new IllegalStateException("Harness failed to distribute threads among groups properly");
    }

    public static void jdkSim1_avgt_jmhStub(InfraControl control, RawResults result, BenchmarkParams benchmarkParams, IterationParams iterationParams, ThreadParams threadParams, Blackhole blackhole, Control notifyControl, int startRndMask, Assignment2_ListsState_jmhType l_listsstate1_G, Assignment2_jmhType l_assignment20_G) throws Throwable {
        long operations = 0;
        long realTime = 0;
        result.startTime = System.nanoTime();
        do {
            l_assignment20_G.jdkSim1(l_listsstate1_G, blackhole);
            operations++;
        } while(!control.isDone);
        result.stopTime = System.nanoTime();
        result.realTime = realTime;
        result.measuredOps = operations;
    }


    public BenchmarkTaskResult jdkList1_SampleTime(InfraControl control, ThreadParams threadParams) throws Throwable {
        this.benchmarkParams = control.benchmarkParams;
        this.iterationParams = control.iterationParams;
        this.threadParams    = threadParams;
        this.notifyControl   = control.notifyControl;
        if (this.blackhole == null) {
            this.blackhole = new Blackhole("Today's password is swordfish. I understand instantiating Blackholes directly is dangerous.");
        }
        if (threadParams.getSubgroupIndex() == 0) {
            RawResults res = new RawResults();
            Assignment2_jmhType l_assignment20_G = _jmh_tryInit_f_assignment20_G(control);
            Assignment2_ListsState_jmhType l_listsstate1_G = _jmh_tryInit_f_listsstate1_G(control);

            control.preSetup();


            control.announceWarmupReady();
            while (control.warmupShouldWait) {
                l_assignment20_G.jdkSim1(l_listsstate1_G, blackhole);
                res.allOps++;
            }

            notifyControl.startMeasurement = true;
            int targetSamples = (int) (control.getDuration(TimeUnit.MILLISECONDS) * 20); // at max, 20 timestamps per millisecond
            int batchSize = iterationParams.getBatchSize();
            int opsPerInv = benchmarkParams.getOpsPerInvocation();
            SampleBuffer buffer = new SampleBuffer();
            jdkSim1_sample_jmhStub(control, res, benchmarkParams, iterationParams, threadParams, blackhole, notifyControl, startRndMask, buffer, targetSamples, opsPerInv, batchSize, l_listsstate1_G, l_assignment20_G);
            notifyControl.stopMeasurement = true;
            control.announceWarmdownReady();
            try {
                while (control.warmdownShouldWait) {
                    l_assignment20_G.jdkSim1(l_listsstate1_G, blackhole);
                    res.allOps++;
                }
                control.preTearDown();
            } catch (InterruptedException ie) {
                control.preTearDownForce();
            }

            if (control.isLastIteration()) {
                if (Assignment2_ListsState_jmhType.tearTrialMutexUpdater.compareAndSet(l_listsstate1_G, 0, 1)) {
                    try {
                        if (control.isFailing) throw new FailureAssistException();
                        if (l_listsstate1_G.readyTrial) {
                            l_listsstate1_G.readyTrial = false;
                        }
                    } catch (Throwable t) {
                        control.isFailing = true;
                        throw t;
                    } finally {
                        Assignment2_ListsState_jmhType.tearTrialMutexUpdater.set(l_listsstate1_G, 0);
                    }
                } else {
                    long l_listsstate1_G_backoff = 1;
                    while (Assignment2_ListsState_jmhType.tearTrialMutexUpdater.get(l_listsstate1_G) == 1) {
                        TimeUnit.MILLISECONDS.sleep(l_listsstate1_G_backoff);
                        l_listsstate1_G_backoff = Math.max(1024, l_listsstate1_G_backoff * 2);
                        if (control.isFailing) throw new FailureAssistException();
                        if (Thread.interrupted()) throw new InterruptedException();
                    }
                }
                synchronized(this.getClass()) {
                    f_listsstate1_G_map.remove(threadParams.getGroupIndex());
                }
                synchronized(this.getClass()) {
                    f_assignment20_G_map.remove(threadParams.getGroupIndex());
                }
            }
            res.allOps += res.measuredOps * batchSize;
            res.allOps *= opsPerInv;
            res.allOps /= batchSize;
            res.measuredOps *= opsPerInv;
            BenchmarkTaskResult results = new BenchmarkTaskResult(res.allOps, res.measuredOps);
            results.add(new SampleTimeResult(ResultRole.PRIMARY, "jdkSim1", buffer, benchmarkParams.getTimeUnit()));
            this.blackhole.evaporate("Yes, I am Stephen Hawking, and know a thing or two about black holes.");
            return results;
        } else
            throw new IllegalStateException("Harness failed to distribute threads among groups properly");
    }

    public static void jdkSim1_sample_jmhStub(InfraControl control, RawResults result, BenchmarkParams benchmarkParams, IterationParams iterationParams, ThreadParams threadParams, Blackhole blackhole, Control notifyControl, int startRndMask, SampleBuffer buffer, int targetSamples, long opsPerInv, int batchSize, Assignment2_ListsState_jmhType l_listsstate1_G, Assignment2_jmhType l_assignment20_G) throws Throwable {
        long realTime = 0;
        long operations = 0;
        int rnd = (int)System.nanoTime();
        int rndMask = startRndMask;
        long time = 0;
        int currentStride = 0;
        do {
            rnd = (rnd * 1664525 + 1013904223);
            boolean sample = (rnd & rndMask) == 0;
            if (sample) {
                time = System.nanoTime();
            }
            for (int b = 0; b < batchSize; b++) {
                if (control.volatileSpoiler) return;
                l_assignment20_G.jdkSim1(l_listsstate1_G, blackhole);
            }
            if (sample) {
                buffer.add((System.nanoTime() - time) / opsPerInv);
                if (currentStride++ > targetSamples) {
                    buffer.half();
                    currentStride = 0;
                    rndMask = (rndMask << 1) + 1;
                }
            }
            operations++;
        } while(!control.isDone);
        startRndMask = Math.max(startRndMask, rndMask);
        result.realTime = realTime;
        result.measuredOps = operations;
    }


    public BenchmarkTaskResult jdkList1_SingleShotTime(InfraControl control, ThreadParams threadParams) throws Throwable {
        this.benchmarkParams = control.benchmarkParams;
        this.iterationParams = control.iterationParams;
        this.threadParams    = threadParams;
        this.notifyControl   = control.notifyControl;
        if (this.blackhole == null) {
            this.blackhole = new Blackhole("Today's password is swordfish. I understand instantiating Blackholes directly is dangerous.");
        }
        if (threadParams.getSubgroupIndex() == 0) {
            Assignment2_jmhType l_assignment20_G = _jmh_tryInit_f_assignment20_G(control);
            Assignment2_ListsState_jmhType l_listsstate1_G = _jmh_tryInit_f_listsstate1_G(control);

            control.preSetup();


            notifyControl.startMeasurement = true;
            RawResults res = new RawResults();
            int batchSize = iterationParams.getBatchSize();
            jdkSim1_ss_jmhStub(control, res, benchmarkParams, iterationParams, threadParams, blackhole, notifyControl, startRndMask, batchSize, l_listsstate1_G, l_assignment20_G);
            control.preTearDown();

            if (control.isLastIteration()) {
                if (Assignment2_ListsState_jmhType.tearTrialMutexUpdater.compareAndSet(l_listsstate1_G, 0, 1)) {
                    try {
                        if (control.isFailing) throw new FailureAssistException();
                        if (l_listsstate1_G.readyTrial) {
                            l_listsstate1_G.readyTrial = false;
                        }
                    } catch (Throwable t) {
                        control.isFailing = true;
                        throw t;
                    } finally {
                        Assignment2_ListsState_jmhType.tearTrialMutexUpdater.set(l_listsstate1_G, 0);
                    }
                } else {
                    long l_listsstate1_G_backoff = 1;
                    while (Assignment2_ListsState_jmhType.tearTrialMutexUpdater.get(l_listsstate1_G) == 1) {
                        TimeUnit.MILLISECONDS.sleep(l_listsstate1_G_backoff);
                        l_listsstate1_G_backoff = Math.max(1024, l_listsstate1_G_backoff * 2);
                        if (control.isFailing) throw new FailureAssistException();
                        if (Thread.interrupted()) throw new InterruptedException();
                    }
                }
                synchronized(this.getClass()) {
                    f_listsstate1_G_map.remove(threadParams.getGroupIndex());
                }
                synchronized(this.getClass()) {
                    f_assignment20_G_map.remove(threadParams.getGroupIndex());
                }
            }
            int opsPerInv = control.benchmarkParams.getOpsPerInvocation();
            long totalOps = opsPerInv;
            BenchmarkTaskResult results = new BenchmarkTaskResult(totalOps, totalOps);
            results.add(new SingleShotResult(ResultRole.PRIMARY, "jdkSim1", res.getTime(), benchmarkParams.getTimeUnit()));
            this.blackhole.evaporate("Yes, I am Stephen Hawking, and know a thing or two about black holes.");
            return results;
        } else
            throw new IllegalStateException("Harness failed to distribute threads among groups properly");
    }

    public static void jdkSim1_ss_jmhStub(InfraControl control, RawResults result, BenchmarkParams benchmarkParams, IterationParams iterationParams, ThreadParams threadParams, Blackhole blackhole, Control notifyControl, int startRndMask, int batchSize, Assignment2_ListsState_jmhType l_listsstate1_G, Assignment2_jmhType l_assignment20_G) throws Throwable {
        long realTime = 0;
        result.startTime = System.nanoTime();
        for (int b = 0; b < batchSize; b++) {
            if (control.volatileSpoiler) return;
            l_assignment20_G.jdkSim1(l_listsstate1_G, blackhole);
        }
        result.stopTime = System.nanoTime();
        result.realTime = realTime;
    }

    
    static java.util.Map<Integer, Assignment2_jmhType> f_assignment20_G_map = java.util.Collections.synchronizedMap(new java.util.HashMap<Integer, Assignment2_jmhType>());
    
    Assignment2_jmhType _jmh_tryInit_f_assignment20_G(InfraControl control) throws Throwable {
        int groupIdx = threadParams.getGroupIndex();
        Assignment2_jmhType val = f_assignment20_G_map.get(groupIdx);
        if (val != null) {
            return val;
        }
        synchronized(this.getClass()) {
            try {
            if (control.isFailing) throw new FailureAssistException();
            val = f_assignment20_G_map.get(groupIdx);
            if (val != null) {
                return val;
            }
            val = new Assignment2_jmhType();
            val.readyTrial = true;
            f_assignment20_G_map.put(groupIdx, val);
            } catch (Throwable t) {
                control.isFailing = true;
                throw t;
            }
        }
        return val;
    }
    
    static java.util.Map<Integer, Assignment2_ListsState_jmhType> f_listsstate1_G_map = java.util.Collections.synchronizedMap(new java.util.HashMap<Integer, Assignment2_ListsState_jmhType>());
    
    Assignment2_ListsState_jmhType _jmh_tryInit_f_listsstate1_G(InfraControl control) throws Throwable {
        int groupIdx = threadParams.getGroupIndex();
        Assignment2_ListsState_jmhType val = f_listsstate1_G_map.get(groupIdx);
        if (val != null) {
            return val;
        }
        synchronized(this.getClass()) {
            try {
            if (control.isFailing) throw new FailureAssistException();
            val = f_listsstate1_G_map.get(groupIdx);
            if (val != null) {
                return val;
            }
            val = new Assignment2_ListsState_jmhType();
            Field f;
            f = com.Assignment2.ListsState.class.getDeclaredField("CHANCE");
            f.setAccessible(true);
            f.set(val, Integer.valueOf(control.getParam("CHANCE")));
            val.myInit();
            val.readyTrial = true;
            f_listsstate1_G_map.put(groupIdx, val);
            } catch (Throwable t) {
                control.isFailing = true;
                throw t;
            }
        }
        return val;
    }


}


package benchmark;

import metrics.OperationMetrics;
import structures.DynamicArray;
import structures.MinHeap;
import structures.MyLinkedList;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class BenchmarkRunner {

    private static volatile long sink;

    private static final int[] SIZES = {100, 1_000, 10_000, 100_000};

    private static final int W1_OPERATIONS = 10_000;
    private static final int W2_OPERATIONS = 1_000;
    private static final int W3_OPERATIONS = 1_000;

    private static final int WARM_UP_RUNS = 1;
    private static final int MEASURED_RUNS = 5;

    private static final long SEED = 42L;

    private static final Path RESULTS_FILE =
            Path.of("results", "results.csv");

    public static void main(String[] args) throws IOException {
        if (args.length > 0 && args[0].equalsIgnoreCase("sanity")) {
            runSanityCheck();
            return;
        }

        runFullBenchmark();
    }

    private static void runFullBenchmark() throws IOException {
        Files.createDirectories(RESULTS_FILE.getParent());

        try (BufferedWriter writer = Files.newBufferedWriter(RESULTS_FILE)) {
            writer.write(
                    "workload,variant,structure,n,time_ms,steps,moves,comparisons"
            );
            writer.newLine();

            for (int n : SIZES) {
                writeResult(writer, benchmarkW1DynamicArray(n));
                writeResult(writer, benchmarkW1LinkedList(n));

                writeResult(writer, benchmarkW2DynamicArray(n));
                writeResult(writer, benchmarkW2LinkedList(n));

                writeResult(writer, benchmarkW3DynamicArray(n, "head"));
                writeResult(writer, benchmarkW3LinkedList(n, "head"));

                writeResult(writer, benchmarkW3DynamicArray(n, "middle"));
                writeResult(writer, benchmarkW3LinkedList(n, "middle"));

                writeResult(writer, benchmarkW4MinHeap(n));
            }
        }

        System.out.println("Benchmark complete.");
        System.out.println("Results saved to: " + RESULTS_FILE);
    }

    private static void runSanityCheck() {
        int n = 100;

        runW1DynamicArrayOnce(n);
        runW1LinkedListOnce(n);

        runW2DynamicArrayOnce(n);
        runW2LinkedListOnce(n);

        runW3DynamicArrayOnce(n, "head");
        runW3LinkedListOnce(n, "head");

        runW3DynamicArrayOnce(n, "middle");
        runW3LinkedListOnce(n, "middle");

        runW4MinHeapOnce(n);

        System.out.println("Sanity benchmark passed.");
    }

    private static BenchmarkResult benchmarkW1DynamicArray(int n) {
        for (int i = 0; i < WARM_UP_RUNS; i++) {
            runW1DynamicArrayOnce(n);
        }

        BenchmarkSample[] samples = new BenchmarkSample[MEASURED_RUNS];

        for (int i = 0; i < MEASURED_RUNS; i++) {
            samples[i] = runW1DynamicArrayOnce(n);
        }

        return createResult(
                "W1",
                "-",
                "DynamicArray",
                n,
                samples
        );
    }

    private static BenchmarkResult benchmarkW1LinkedList(int n) {
        for (int i = 0; i < WARM_UP_RUNS; i++) {
            runW1LinkedListOnce(n);
        }

        BenchmarkSample[] samples = new BenchmarkSample[MEASURED_RUNS];

        for (int i = 0; i < MEASURED_RUNS; i++) {
            samples[i] = runW1LinkedListOnce(n);
        }

        return createResult(
                "W1",
                "-",
                "MyLinkedList",
                n,
                samples
        );
    }

    private static BenchmarkResult benchmarkW2DynamicArray(int n) {
        for (int i = 0; i < WARM_UP_RUNS; i++) {
            runW2DynamicArrayOnce(n);
        }

        BenchmarkSample[] samples = new BenchmarkSample[MEASURED_RUNS];

        for (int i = 0; i < MEASURED_RUNS; i++) {
            samples[i] = runW2DynamicArrayOnce(n);
        }

        return createResult(
                "W2",
                "-",
                "DynamicArray",
                n,
                samples
        );
    }

    private static BenchmarkResult benchmarkW2LinkedList(int n) {
        for (int i = 0; i < WARM_UP_RUNS; i++) {
            runW2LinkedListOnce(n);
        }

        BenchmarkSample[] samples = new BenchmarkSample[MEASURED_RUNS];

        for (int i = 0; i < MEASURED_RUNS; i++) {
            samples[i] = runW2LinkedListOnce(n);
        }

        return createResult(
                "W2",
                "-",
                "MyLinkedList",
                n,
                samples
        );
    }

    private static BenchmarkResult benchmarkW3DynamicArray(
            int n,
            String variant) {

        for (int i = 0; i < WARM_UP_RUNS; i++) {
            runW3DynamicArrayOnce(n, variant);
        }

        BenchmarkSample[] samples = new BenchmarkSample[MEASURED_RUNS];

        for (int i = 0; i < MEASURED_RUNS; i++) {
            samples[i] = runW3DynamicArrayOnce(n, variant);
        }

        return createResult(
                "W3",
                variant,
                "DynamicArray",
                n,
                samples
        );
    }

    private static BenchmarkResult benchmarkW3LinkedList(
            int n,
            String variant) {

        for (int i = 0; i < WARM_UP_RUNS; i++) {
            runW3LinkedListOnce(n, variant);
        }

        BenchmarkSample[] samples = new BenchmarkSample[MEASURED_RUNS];

        for (int i = 0; i < MEASURED_RUNS; i++) {
            samples[i] = runW3LinkedListOnce(n, variant);
        }

        return createResult(
                "W3",
                variant,
                "MyLinkedList",
                n,
                samples
        );
    }

    private static BenchmarkResult benchmarkW4MinHeap(int n) {
        for (int i = 0; i < WARM_UP_RUNS; i++) {
            runW4MinHeapOnce(n);
        }

        BenchmarkSample[] samples = new BenchmarkSample[MEASURED_RUNS];

        for (int i = 0; i < MEASURED_RUNS; i++) {
            samples[i] = runW4MinHeapOnce(n);
        }

        return createResult(
                "W4",
                "-",
                "MinHeap",
                n,
                samples
        );
    }

    private static BenchmarkSample runW1DynamicArrayOnce(int n) {
        int[] values = generateData(n);

        DynamicArray array = new DynamicArray();

        for (int value : values) {
            array.add(value);
        }

        array.resetMetrics();

        Random random = new Random(SEED);
        int[] indices = new int[W1_OPERATIONS];

        for (int i = 0; i < W1_OPERATIONS; i++) {
            indices[i] = random.nextInt(n);
        }

        long checksum = 0;

        long start = System.nanoTime();

        for (int index : indices) {
            checksum += array.get(index);
        }

        long end = System.nanoTime();

        sink = checksum;

        return sample(end - start, array.getMetrics());
    }

    private static BenchmarkSample runW1LinkedListOnce(int n) {
        int[] values = generateData(n);

        MyLinkedList list = new MyLinkedList();

        for (int value : values) {
            list.add(value);
        }

        list.resetMetrics();

        Random random = new Random(SEED);
        int[] indices = new int[W1_OPERATIONS];

        for (int i = 0; i < W1_OPERATIONS; i++) {
            indices[i] = random.nextInt(n);
        }

        long checksum = 0;

        long start = System.nanoTime();

        for (int index : indices) {
            checksum += list.get(index);
        }

        long end = System.nanoTime();

        sink = checksum;

        return sample(end - start, list.getMetrics());
    }

    private static BenchmarkSample runW2DynamicArrayOnce(int n) {
        int[] values = generateData(n);
        int[] queries = generateSearchQueries(values);

        DynamicArray array = new DynamicArray();

        for (int value : values) {
            array.add(value);
        }

        array.resetMetrics();

        long start = System.nanoTime();

        for (int query : queries) {
            array.contains(query);
        }

        long end = System.nanoTime();

        return sample(end - start, array.getMetrics());
    }

    private static BenchmarkSample runW2LinkedListOnce(int n) {
        int[] values = generateData(n);
        int[] queries = generateSearchQueries(values);

        MyLinkedList list = new MyLinkedList();

        for (int value : values) {
            list.add(value);
        }

        list.resetMetrics();

        long start = System.nanoTime();

        for (int query : queries) {
            list.contains(query);
        }

        long end = System.nanoTime();

        return sample(end - start, list.getMetrics());
    }

    private static BenchmarkSample runW3DynamicArrayOnce(
            int n,
            String variant) {

        int[] values = generateData(n);
        int[] insertedValues = generateData(W3_OPERATIONS);

        DynamicArray array = new DynamicArray();

        for (int value : values) {
            array.add(value);
        }

        array.resetMetrics();

        int index = variant.equals("head") ? 0 : n / 2;

        long start = System.nanoTime();

        for (int value : insertedValues) {
            array.add(index, value);
        }

        for (int i = 0; i < W3_OPERATIONS; i++) {
            array.remove(index);
        }

        long end = System.nanoTime();

        return sample(end - start, array.getMetrics());
    }

    private static BenchmarkSample runW3LinkedListOnce(
            int n,
            String variant) {

        int[] values = generateData(n);
        int[] insertedValues = generateData(W3_OPERATIONS);

        MyLinkedList list = new MyLinkedList();

        for (int value : values) {
            list.add(value);
        }

        list.resetMetrics();

        int index = variant.equals("head") ? 0 : n / 2;

        long start = System.nanoTime();

        for (int value : insertedValues) {
            list.add(index, value);
        }

        for (int i = 0; i < W3_OPERATIONS; i++) {
            list.remove(index);
        }

        long end = System.nanoTime();

        return sample(end - start, list.getMetrics());
    }

    private static BenchmarkSample runW4MinHeapOnce(int n) {
        int[] values = generateData(n);

        MinHeap heap = new MinHeap();
        heap.resetMetrics();

        int[] extracted = new int[n];

        long start = System.nanoTime();

        for (int value : values) {
            heap.insert(value);
        }

        for (int i = 0; i < n; i++) {
            extracted[i] = heap.extractMin();
        }

        long end = System.nanoTime();

        for (int i = 1; i < extracted.length; i++) {
            if (extracted[i] < extracted[i - 1]) {
                throw new IllegalStateException(
                        "MinHeap output is not sorted"
                );
            }
        }

        return sample(end - start, heap.getMetrics());
    }

    private static int[] generateData(int n) {
        Random random = new Random(SEED);
        int[] values = new int[n];

        for (int i = 0; i < n; i++) {
            values[i] = random.nextInt(1_000_000);
        }

        return values;
    }

    private static int[] generateSearchQueries(int[] values) {
        int[] queries = new int[W2_OPERATIONS];
        Random random = new Random(SEED);

        for (int i = 0; i < W2_OPERATIONS / 2; i++) {
            queries[2 * i] =
                    values[random.nextInt(values.length)];

            queries[2 * i + 1] =
                    -1 - i;
        }

        return queries;
    }

    private static BenchmarkSample sample(
            long timeNs,
            OperationMetrics metrics) {

        return new BenchmarkSample(
                timeNs,
                metrics.getSteps(),
                metrics.getMoves(),
                metrics.getComparisons()
        );
    }

    private static BenchmarkResult createResult(
            String workload,
            String variant,
            String structure,
            int n,
            BenchmarkSample[] samples) {

        long[] times = new long[samples.length];

        for (int i = 0; i < samples.length; i++) {
            times[i] = samples[i].timeNs;
        }

        Arrays.sort(times);

        long medianTimeNs = times[times.length / 2];

        BenchmarkSample metricsSample = samples[0];

        return new BenchmarkResult(
                workload,
                variant,
                structure,
                n,
                medianTimeNs,
                metricsSample.steps,
                metricsSample.moves,
                metricsSample.comparisons
        );
    }

    private static void writeResult(
            BufferedWriter writer,
            BenchmarkResult result) throws IOException {

        writer.write(result.toCsv());
        writer.newLine();

        System.out.println(result.toCsv());
    }

    private static class BenchmarkSample {
        private final long timeNs;
        private final long steps;
        private final long moves;
        private final long comparisons;

        private BenchmarkSample(
                long timeNs,
                long steps,
                long moves,
                long comparisons) {

            this.timeNs = timeNs;
            this.steps = steps;
            this.moves = moves;
            this.comparisons = comparisons;
        }
    }

    private static class BenchmarkResult {
        private final String workload;
        private final String variant;
        private final String structure;
        private final int n;
        private final long timeNs;
        private final long steps;
        private final long moves;
        private final long comparisons;

        private BenchmarkResult(
                String workload,
                String variant,
                String structure,
                int n,
                long timeNs,
                long steps,
                long moves,
                long comparisons) {

            this.workload = workload;
            this.variant = variant;
            this.structure = structure;
            this.n = n;
            this.timeNs = timeNs;
            this.steps = steps;
            this.moves = moves;
            this.comparisons = comparisons;
        }

        private String toCsv() {
            double timeMs = timeNs / 1_000_000.0;

            return String.format(
                    Locale.US,
                    "%s,%s,%s,%d,%.3f,%d,%d,%d",
                    workload,
                    variant,
                    structure,
                    n,
                    timeMs,
                    steps,
                    moves,
                    comparisons
            );
        }
    }
}
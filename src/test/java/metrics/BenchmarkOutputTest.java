package metrics;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BenchmarkOutputTest {

    private static final Path RESULTS_FILE =
            Path.of("results", "results.csv");

    private static final String EXPECTED_HEADER =
            "workload,variant,structure,n,time_ms,steps,moves,comparisons";

    private static final int[] SIZES =
            {100, 1_000, 10_000, 100_000};

    @Test
    void csvExistsAndHasExpectedShape() throws IOException {
        assertTrue(
                Files.exists(RESULTS_FILE),
                "results/results.csv does not exist"
        );

        List<String> lines = Files.readAllLines(RESULTS_FILE);

        assertEquals(37, lines.size());
        assertEquals(EXPECTED_HEADER, lines.get(0));
    }

    @Test
    void containsAllRequiredBenchmarkCases() throws IOException {
        Set<String> expected = new HashSet<>();

        for (int n : SIZES) {
            expected.add(key("W1", "-", "DynamicArray", n));
            expected.add(key("W1", "-", "MyLinkedList", n));

            expected.add(key("W2", "-", "DynamicArray", n));
            expected.add(key("W2", "-", "MyLinkedList", n));

            expected.add(key("W3", "head", "DynamicArray", n));
            expected.add(key("W3", "head", "MyLinkedList", n));

            expected.add(key("W3", "middle", "DynamicArray", n));
            expected.add(key("W3", "middle", "MyLinkedList", n));

            expected.add(key("W4", "-", "MinHeap", n));
        }

        Set<String> actual = new HashSet<>();

        for (String[] row : readRows()) {
            actual.add(
                    key(
                            row[0],
                            row[1],
                            row[2],
                            Integer.parseInt(row[3])
                    )
            );
        }

        assertEquals(expected, actual);
    }

    @Test
    void workloadCountsAreCorrect() throws IOException {
        Map<String, Integer> counts = new HashMap<>();

        for (String[] row : readRows()) {
            counts.put(
                    row[0],
                    counts.getOrDefault(row[0], 0) + 1
            );
        }

        assertEquals(8, counts.getOrDefault("W1", 0));
        assertEquals(8, counts.getOrDefault("W2", 0));
        assertEquals(16, counts.getOrDefault("W3", 0));
        assertEquals(4, counts.getOrDefault("W4", 0));
    }

    @Test
    void variantsAreCorrect() throws IOException {
        int headCount = 0;
        int middleCount = 0;
        int dashCount = 0;

        for (String[] row : readRows()) {
            if (row[1].equals("head")) {
                headCount++;
            } else if (row[1].equals("middle")) {
                middleCount++;
            } else if (row[1].equals("-")) {
                dashCount++;
            }
        }

        assertEquals(8, headCount);
        assertEquals(8, middleCount);
        assertEquals(20, dashCount);
    }

    @Test
    void numericValuesAreValid() throws IOException {
        Set<Integer> validSizes =
                Set.of(100, 1_000, 10_000, 100_000);

        for (String[] row : readRows()) {
            int n = Integer.parseInt(row[3]);
            double timeMs = Double.parseDouble(row[4]);
            long steps = Long.parseLong(row[5]);
            long moves = Long.parseLong(row[6]);
            long comparisons = Long.parseLong(row[7]);

            assertTrue(validSizes.contains(n));
            assertTrue(timeMs >= 0);
            assertTrue(steps >= 0);
            assertTrue(moves >= 0);
            assertTrue(comparisons >= 0);
        }
    }

    @Test
    void workloadCountersAreMeaningful() throws IOException {
        for (String[] row : readRows()) {
            String workload = row[0];

            long steps = Long.parseLong(row[5]);
            long moves = Long.parseLong(row[6]);
            long comparisons = Long.parseLong(row[7]);

            if (workload.equals("W1")) {
                assertTrue(steps > 0);
            }

            if (workload.equals("W2")) {
                assertTrue(steps > 0);
                assertTrue(comparisons > 0);
            }

            if (workload.equals("W3")) {
                assertTrue(steps > 0);
                assertTrue(moves > 0);
            }

            if (workload.equals("W4")) {
                assertTrue(steps > 0);
                assertTrue(moves > 0);
                assertTrue(comparisons > 0);
            }
        }
    }

    private List<String[]> readRows() throws IOException {
        List<String> lines = Files.readAllLines(RESULTS_FILE);

        return lines.subList(1, lines.size())
                .stream()
                .map(this::parseRow)
                .toList();
    }

    private String[] parseRow(String line) {
        String[] columns = line.split(",", -1);

        assertEquals(
                8,
                columns.length,
                "Invalid CSV row: " + line
        );

        return columns;
    }

    private String key(
            String workload,
            String variant,
            String structure,
            int n) {

        return workload
                + "|"
                + variant
                + "|"
                + structure
                + "|"
                + n;
    }
}
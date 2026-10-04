# DAA Assignment 2 - Data Structures

#### GitHub Repository: https://github.com/Yerassyp/DAA_Assignment2


This project was created for Assignment 2 of the Design and Analysis of Algorithms course.

It contains three data structures implemented from scratch using primitive `int` values:

- `DynamicArray`
- `MyLinkedList`
- `MinHeap`

The project also includes operation counters, JUnit 5 tests, benchmark workloads, CSV results, plots, and a report.

## Project Structure

```text
src/
├── main/
│   └── java/
│       ├── benchmark/
│       │   └── BenchmarkRunner.java
│       ├── metrics/
│       │   └── OperationMetrics.java
│       └── structures/
│           ├── DynamicArray.java
│           ├── MyLinkedList.java
│           └── MinHeap.java
│
└── test/
    └── java/
        ├── metrics/
        │   ├── BenchmarkOutputTest.java
        │   └── OperationMetricsTest.java
        └── structures/
            ├── DynamicArrayTest.java
            ├── MyLinkedListTest.java
            └── MinHeapTest.java

results/
├── results.csv
└── plots/

REPORT.md
README.md
pom.xml
```

## Requirements

- Java 17
- Maven Wrapper included in the project

## Build

On Windows PowerShell:

```powershell
.\mvnw.cmd compile
```

## Run Tests

```powershell
.\mvnw.cmd test
```

The project currently contains JUnit 5 tests for:

- DynamicArray correctness and edge cases
- MyLinkedList correctness and edge cases
- MinHeap correctness and heap property
- operation counters
- benchmark CSV output

## Run Benchmark

First compile the project:

```powershell
.\mvnw.cmd compile
```

Then run:

```powershell
java -cp target/classes benchmark.BenchmarkRunner
```

The benchmark runs workloads W1-W4 for:

```text
n = 100, 1000, 10000, 100000
```

It uses `Random(42)`, one discarded warm-up run, five measured runs, and saves the median running time.

## Results

Benchmark results are saved in:

```text
results/results.csv
```

The CSV columns are:

```text
workload,variant,structure,n,time_ms,steps,moves,comparisons
```

Generated PNG plots are stored in:

```text
results/plots/
```

## Report

The complexity analysis, loop invariant proofs, benchmark plots, and discussion are available in:

```text
REPORT.md
```
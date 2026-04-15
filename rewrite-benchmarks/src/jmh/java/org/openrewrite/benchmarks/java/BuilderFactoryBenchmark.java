/*
 * Copyright 2026 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.benchmarks.java;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openrewrite.java.JavaParser;

import java.util.concurrent.TimeUnit;

/**
 * Microbenchmark for JavaParser.fromJavaVersion() builder factory.
 * Measures the overhead of reflection vs MethodHandle invocation.
 */
@Fork(value = 2, warmups = 1)
@Measurement(iterations = 5, time = 1)
@Warmup(iterations = 3, time = 1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class BuilderFactoryBenchmark {

    /**
     * Measures the cost of repeatedly calling fromJavaVersion() to obtain
     * a new builder instance. This exercises the cached supplier's invoke path.
     */
    @Benchmark
    public void builderFactory(Blackhole bh) {
        JavaParser.Builder<? extends JavaParser, ?> builder = JavaParser.fromJavaVersion();
        bh.consume(builder);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BuilderFactoryBenchmark.class.getSimpleName())
                .shouldFailOnError(true)
                .build();
        new Runner(opt).run();
    }
}

package com.e2x.bigcommerce.routinefinder.cli.reporting;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.RoutineGraphStatsHelper;
import com.google.api.client.util.Maps;

import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.function.Supplier;

public class RoutineGraphStatisticCompiler {

    private final RoutineGraph graph;
    private final Map<Stats, Object> stats = Maps.newHashMap();

    public RoutineGraphStatisticCompiler(RoutineGraph graph) {
        this.graph = graph;
    }

    public long getRoutineCount() {
        return (long) getOrDefault(Stats.ROUTINE_COUNT, () ->
            RoutineGraphStatsHelper.routineCountIn(graph)
        );
    }

    public long getLongestPathLength() {
        return getIntSummaryStatisticsFrom(graph).getMax();
    }

    public long getShortestPathLength() {
        return getIntSummaryStatisticsFrom(graph).getMin();
    }

    public double getAveragePathLength() {
        return getIntSummaryStatisticsFrom(graph).getAverage();
    }

    private IntSummaryStatistics getIntSummaryStatisticsFrom(RoutineGraph routineGraph) {
        return (IntSummaryStatistics) getOrDefault(Stats.SUMMARY_STATS, () -> RoutineGraphStatsHelper
                .allPathLengthsIn(routineGraph)
                .stream()
                .mapToInt(v->v)
                .summaryStatistics());
    }
    private Object getOrDefault(Stats stat, Supplier<Object> statsSupplier) {
        if (!stats.containsKey(stat)) {
            stats.put(stat, statsSupplier.get());
        }

        return stats.get(stat);
    }

    public enum Stats {
        ROUTINE_COUNT,
        SUMMARY_STATS,
    }
}

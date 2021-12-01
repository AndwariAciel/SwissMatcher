package de.andwari.swissmatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.matching.blossom.v5.KolmogorovWeightedPerfectMatching;
import org.jgrapht.alg.matching.blossom.v5.ObjectiveSense;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.util.SupplierUtil;

class Matcher {

    private Matcher() {}

    public static List<Pair<Integer, Integer>> getMatching(Map<Integer, List<Integer>> groups,
                                                           List<Pair<Integer, Integer>> playedMatches) {
        Graph<Integer, DefaultWeightedEdge> graph = addVerticesAndWeights(groups, playedMatches);

        KolmogorovWeightedPerfectMatching<Integer, DefaultWeightedEdge> matching =
                new KolmogorovWeightedPerfectMatching<>(graph, ObjectiveSense.MINIMIZE);
        return createPairings(matching.getMatching().getEdges(), graph);
    }

    private static List<Pair<Integer, Integer>> createPairings(Set<DefaultWeightedEdge> edges,
                                                               Graph<Integer, DefaultWeightedEdge> graph) {
        List<Pair<Integer, Integer>> pairings = new ArrayList<>();
        for (DefaultWeightedEdge edge : edges) {
            pairings.add(new ImmutablePair<>(graph.getEdgeSource(edge), graph.getEdgeTarget(edge)));
        }
        return pairings;
    }

    private static Graph<Integer, DefaultWeightedEdge> addVerticesAndWeights(Map<Integer, List<Integer>> groups,
                                                                             List<Pair<Integer, Integer>> playedMatches) {
        var graph =
                new SimpleWeightedGraph<>(SupplierUtil.createIntegerSupplier(getStart(groups)), SupplierUtil.createDefaultWeightedEdgeSupplier());
        for (int i = 0; i < getNumberOfPlayers(groups); i++) {
            graph.addVertex();
        }
        addWeights(graph, groups, playedMatches);
        return graph;
    }

    private static int getStart(Map<Integer, List<Integer>> groups) {
        if (groups.get(groups.size() - 1).contains(-1)) {
            return -1;
        }
        return 0;
    }

    private static void addWeights(Graph<Integer, DefaultWeightedEdge> graph, Map<Integer, List<Integer>> groups,
                                   List<Pair<Integer, Integer>> playedMatches) {
        var unwrappedGroups = unwrapGroups(groups);
        for (int i = 0; i < unwrappedGroups.size(); i++) {
            for (int j = i + 1; j < unwrappedGroups.size(); j++) {
                var player1 = unwrappedGroups.get(i);
                var player2 = unwrappedGroups.get(j);
                if (!containsPair(playedMatches, player1.getLeft(), player2.getLeft())) {
                    Graphs.addEdge(graph, player1.getLeft(), player2.getLeft(),
                            calculateWeight(player1, player2));
                }
            }
        }
    }

    private static double calculateWeight(Pair<Integer, Integer> player1, Pair<Integer, Integer> player2) {
        return Math.abs(player1.getRight() - player2.getRight()) * 10.;
    }

    private static boolean containsPair(List<Pair<Integer, Integer>> playedMatches, Integer player1, Integer player2) {
        return playedMatches.stream().anyMatch(p -> p.getRight().equals(player1) && p.getLeft().equals(player2) ||
                p.getRight().equals(player2) && p.getLeft().equals(player1));
    }

    private static int getNumberOfPlayers(Map<Integer, List<Integer>> groups) {
        return groups.values().stream().flatMapToInt(list -> IntStream.of(list.size())).sum();
    }

    private static List<Pair<Integer, Integer>> unwrapGroups(Map<Integer, List<Integer>> groups) {
        List<Pair<Integer, Integer>> list = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : groups.entrySet()) {
            for (Integer player : entry.getValue()) {
                list.add(new ImmutablePair<>(player, entry.getKey()));
            }
        }
        return list;
    }
}

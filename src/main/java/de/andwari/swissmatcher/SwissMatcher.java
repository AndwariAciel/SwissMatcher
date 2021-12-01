package de.andwari.swissmatcher;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;

public class SwissMatcher {

    private final Map<Integer, List<Integer>> groups;
    private final List<Pair<Integer, Integer>> playedMatches;

    public SwissMatcher() {
        this.playedMatches = new ArrayList<>();
        this.groups = new TreeMap<>();
    }

    public void addPlayerToGroup(int group, int player) {
        if (isNull(groups.get(group))) {
            groups.put(group, new ArrayList<>());
        }
        groups.get(group).add(player);
        Collections.shuffle(groups.get(group));
    }

    public List<Pair<Integer, Integer>> getMatching() {
        verify();
        handleBye();
        var matches = Matcher.getMatching(groups, playedMatches);
        playedMatches.addAll(matches);
        groups.clear();
        return matches;
    }

    private void verify() {
        var players = groups.values().stream().flatMap(List::stream).collect(Collectors.toList());
        if(players.stream().mapToInt(Integer::intValue).max().orElse(0) >= players.size()) {
            throw new IllegalArgumentException("Some player numbers are too high");
        }
        if(players.stream().mapToInt(Integer::intValue).min().orElse(0) < 0) {
            throw new IllegalArgumentException("No negative player numbers allowed");
        }
        if (players.stream().anyMatch(i -> Collections.frequency(players, i) > 1)) {
            throw new IllegalArgumentException("Some players are duplicated");
        }
    }

    /**
     * If there is an uneven number of players a BYE is added.
     * It has always identifier -1 and it will always be added after the highest group.
     **/
    private void handleBye() {
        long size = groups.values().stream().mapToLong(List::size).sum();
        if (size % 2 == 1) {
            var maxGroup = groups.keySet().stream().mapToInt(Integer::intValue).max();
            maxGroup.ifPresent(i -> addPlayerToGroup(i + 1, -1));
        }
    }

}

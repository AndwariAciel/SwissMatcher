package de.andwari.swissmatcher;

import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

class SwissMatcherTest {

    @Test
    void test8Players() {
        SwissMatcher matcher = new SwissMatcher();
        matcher.addPlayerToGroup(0, 0);
        matcher.addPlayerToGroup(0, 1);
        matcher.addPlayerToGroup(1, 2);
        matcher.addPlayerToGroup(1, 3);
        matcher.addPlayerToGroup(1, 4);
        matcher.addPlayerToGroup(1, 5);
        matcher.addPlayerToGroup(1, 6);
        matcher.addPlayerToGroup(1, 7);

        var matching = matcher.getMatching();
        System.out.println(matching.size());

        matcher.addPlayerToGroup(0, 0);
        matcher.addPlayerToGroup(0, 1);
        matcher.addPlayerToGroup(1, 2);
        matcher.addPlayerToGroup(1, 3);
        matcher.addPlayerToGroup(1, 4);
        matcher.addPlayerToGroup(1, 5);
        matcher.addPlayerToGroup(1, 6);
        matcher.addPlayerToGroup(1, 7);

        var secondMatching = matcher.getMatching();
    }

    @Test
    void test8PlayersSecondRound() {
        SwissMatcher matcher = new SwissMatcher();
        matcher.addPlayerToGroup(1, 0);
        matcher.addPlayerToGroup(2, 1);
        matcher.addPlayerToGroup(1, 2);
        matcher.addPlayerToGroup(0, 3);
        matcher.addPlayerToGroup(1, 4);
        matcher.addPlayerToGroup(1, 5);
        matcher.addPlayerToGroup(0, 6);
        matcher.addPlayerToGroup(2, 7);

        var matching = matcher.getMatching();
        System.out.println(matching.size());
    }

    @Test
    void test8PlayersWithBye() {
        SwissMatcher matcher = new SwissMatcher();
        matcher.addPlayerToGroup(0, 0);
        matcher.addPlayerToGroup(0, 1);
        matcher.addPlayerToGroup(0, 2);
        matcher.addPlayerToGroup(0, 3);
        matcher.addPlayerToGroup(1, 4);
        matcher.addPlayerToGroup(0, 5);
        matcher.addPlayerToGroup(0, 6);

        var matching = matcher.getMatching();
        System.out.println(matching.size());
    }

    @Test
    void test5PlayersWithBye() {
        SwissMatcher matcher = new SwissMatcher();
        matcher.addPlayerToGroup(0, 0);
        matcher.addPlayerToGroup(1, 1);
        matcher.addPlayerToGroup(0, 2);
        matcher.addPlayerToGroup(1, 3);
        matcher.addPlayerToGroup(1, 4);


        var matching = matcher.getMatching();
        System.out.println(matching.size());
    }

}
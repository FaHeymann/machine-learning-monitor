package services;

import java.util.ArrayList;
import java.util.List;

public class ArrayCombinations {

    public <E> List<List<E>> compute(final List<List<E>> containers) {
        return compute(0, containers);
    }

    private <E> List<List<E>> compute(final int currentIndex, final List<List<E>> containers) {
        if (currentIndex == containers.size()) {
            List<List<E>> combinations = new ArrayList<>();
            combinations.add(new ArrayList<>());
            return combinations;
        }

        List<List<E>> combinations = new ArrayList<>();
        List<List<E>> suffixList = compute(currentIndex + 1, containers);
        containers.get(currentIndex).forEach(containerItem -> suffixList.forEach(suffix -> {
            List<E> nextCombination = new ArrayList<>(suffix);
            nextCombination.add(containerItem);
            combinations.add(nextCombination);
        }));
        return combinations;
    }
}

import org.junit.Before;
import services.ArrayCombinations;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ArrayCombinationsTest {

    private ArrayCombinations arrayCombinations;

    @Before
    public void before() {
        this.arrayCombinations = new ArrayCombinations();
    }

    @Test
    public void test() {
        List<List<String>> containers = new ArrayList<>();
        containers.add(new ArrayList<>(Arrays.asList(new String[] {"1", "2", "3"})));
        containers.add(new ArrayList<>(Arrays.asList(new String[] {"a", "b", "c"})));
        containers.add(new ArrayList<>(Arrays.asList(new String[] {"x", "y", "z"})));

        List<List<String>> result = this.arrayCombinations.compute(containers);

        assertEquals(27, result.size());
    }
}

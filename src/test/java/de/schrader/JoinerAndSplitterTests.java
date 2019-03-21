package de.schrader;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JoinerAndSplitterTests {

    @Test
    public void convertListToString() {
        List<String> names = Lists.newArrayList("John", "Jane", "Adam", "Tom");
        String result = Joiner.on(",").join(names);
        assertThat(result).isEqualTo("John,Jane,Adam,Tom");
    }

    @Test
    public void convertMapToString() {
        Map<String, Integer> salary = Maps.newHashMap();
        salary.put("John", 1000);
        salary.put("Jane", 1500);
        String result = Joiner.on(" , ").withKeyValueSeparator(" = ").join(salary);
        assertThat(result)
                .contains("John = 1000")
                .contains("Jane = 1500");
    }

    @Test
    public void joinNestedCollections() {
        List<ArrayList<String>> nested = Lists.newArrayList(
                Lists.newArrayList("apple", "banana", "orange"),
                Lists.newArrayList("cat", "dog", "bird"),
                Lists.newArrayList("John", "Jane", "Adam"));
        String result = Joiner.on(";").join(Iterables.transform(nested,
                new Function<List<String>, String>() {
                    @Override
                    public String apply(List<String> input) {
                        return Joiner.on("-").join(input);
                    }
                }));
        assertThat(result)
                .contains("apple-banana-orange")
                .contains("cat-dog-bird")
                .contains("John-Jane-Adam");
    }

    @Test
    public void convertListToStringAndSkipNull() {
        List<String> names = Lists.newArrayList("John", null, "Jane", "Adam", "Tom");
        String result = Joiner.on(",").skipNulls().join(names);
        assertThat(result).isEqualTo("John,Jane,Adam,Tom");
    }

    @Test
    public void convertListToStringAndSkipNullWitUseForNull() {
        List<String> names = Lists.newArrayList("John", null, "Jane", "Adam", "Tom");
        String result = Joiner.on(",").useForNull("nameless").join(names);
        assertThat(result).isEqualTo("John,nameless,Jane,Adam,Tom");
    }

    @Test
    public void createListFromString() {
        String input = "apple - banana - orange";
        List<String> result = Splitter.on("-").trimResults()
                .splitToList(input);
        assertThat(result).contains("apple", "banana", "orange");
    }

    @Test
    public void createMapFromString() {
        String input = "John=first,Adam=second";
        Map<String, String> result = Splitter.on(",")
                .withKeyValueSeparator("=")
                .split(input);
        assertThat(result.get("John")).isEqualTo("first");
        assertThat(result.get("Adam")).isEqualTo("second");
    }

    @Test
    public void splitStringOnMultipleSeparator() {
        String input = "apple.banana,,orange,,.";
        List<String> result = Splitter.onPattern("[.,]")
                .omitEmptyStrings()
                .splitToList(input);
        assertThat(result).contains("apple", "banana", "orange");
    }

    @Test
    public void whenSplitStringOnSpecificLength_thenSplit() {
        String input = "Hello world";
        List<String> result = Splitter.fixedLength(3).splitToList(input);

        assertThat(result).contains("Hel", "lo ", "wor", "ld");
    }

    @Test
    public void whenLimitSplitting_thenLimited() {
        String input = "a,b,c,d,e";
        List<String> result = Splitter.on(",")
                .limit(4)
                .splitToList(input);
        assertThat(result.size()).isEqualTo(4);
        assertThat(result).contains("a", "b", "c", "d,e");
    }
}

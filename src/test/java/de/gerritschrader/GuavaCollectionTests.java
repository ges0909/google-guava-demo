package de.gerritschrader;

import com.google.common.collect.*;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class GuavaCollectionTests {

    @Test
    public void list() {
        List<Number> list = Lists.newArrayList();
        assertThat(list).isNotNull().isEmpty();
    }

    @Test
    public void set() {
        Set<Number> set = Sets.newHashSet();
        assertThat(set).isNotNull().isEmpty();
    }

    @Test
    public void map() {
        Map<Number, String> map = Maps.newHashMap();
        assertThat(map).isNotNull().isEmpty();
    }

    @Test
    // create immutable List from a standard collection
    public void immutableList() {
        List<Number> list = Lists.newArrayList();
        ImmutableList<Number> immutableList = ImmutableList.copyOf(list);
        assertThat(immutableList).isNotNull().isEmpty();
    }

    @Test
    // create immutable Set from a standard collection
    public void immutableSet() {
        Set<Number> set = Sets.newHashSet();
        ImmutableSet<Number> immutableSet = ImmutableSet.copyOf(set);
        assertThat(immutableSet).isNotNull().isEmpty();
    }

    @Test
    // create immutable Map from a standard collection
    public void immutableMap() {
        Map<Number, String> map = Maps.newHashMap();
        ImmutableMap<Number, String> immutableMap = ImmutableMap.copyOf(map);
        assertThat(immutableMap).isNotNull().isEmpty();
    }

    @Test
    public void addIterableToCollection() {
        String[] arr = {"uno", "due", "tre"};
        Iterable<String> iter = Lists.newArrayList(arr);
        Collection<String> collection = Lists.newArrayList();
        Iterables.addAll(collection, iter);
        assertThat(collection).isNotNull().isNotEmpty().hasSize(arr.length);
    }

    @Test
    // check if collection contains element(s) according to a custom matching rule
    public void anyWithCustomMatcher() {
        Iterable<String> collection = Lists.newArrayList("a", "bc", "def");
        // 'any' returns 'true' if any element satisfies the predicate
        boolean containsAny = Iterables.any(collection, elem -> elem != null && elem.length() == 1);
        assertThat(containsAny).isTrue();
        boolean containsNot = Iterables.any(collection, "uno"::equals);
        assertThat(containsNot).isFalse();
    }

    @Test
    public void findWithCustomMatcher() {
        Iterable<String> collection = Sets.newHashSet("a", "bc", "def");
        String findAny = Iterables.find(collection, elem -> elem.length() == 1);
        assertThat(findAny).isEqualTo("a");
    }

    @Test
    public void findDefaultValue() {
        String whenNotFound = "";
        Iterable<String> collection = Sets.newHashSet("a", "bc", "def");
        String findAny = Iterables.find(collection, elem -> "not found".equals(elem), whenNotFound);
        assertThat(findAny).isEqualTo(whenNotFound);
    }

    @Test
    public void filter() {
        Iterable<String> collection = Sets.newHashSet("a", "bc", "def");
        // Predicate<String> predicate = elem -> elem.length() == 1;
        Iterable<String> resultSet = Iterables.filter(collection, elem -> elem.length() == 1);
        assertThat(resultSet).isNotEmpty();
    }

    @Test
    public void casting() {
        List<Number> list = Lists.newArrayList();
        List<Integer> castedList = (List<Integer>) (List<? extends Number>) list;
        assertThat(castedList).isNotNull();
    }

    @Test
    public void hashBasedTable() {
        // first name, last name, age
        Table<String /*row key*/, String /*column key*/, Integer /*cell value*/> table = HashBasedTable.create();
        table.put("Max", "Mustermann", 44);
        table.put("Biene", "Maya", 4);
        Integer cellValue = table.get("Biene", "Maya");
        assertThat(table.size()).isEqualTo(2);
        assertThat(cellValue).isEqualTo(4);
    }

    @Test
    public void treeBasedTable() {
        // if a table is needed whose row keys and the column keys need to be ordered by
        // their natural ordering or by supplying comparators, use a TreeBasedTable, which
        // uses TreeMap internally
        Table<String, String, Integer> table = HashBasedTable.create();
    }
}

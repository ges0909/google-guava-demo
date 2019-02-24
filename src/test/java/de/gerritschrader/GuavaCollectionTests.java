package de.gerritschrader;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.*;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class GuavaCollectionTests {

    @Test
    public void newArrayList() {
        final List<Number> list = Lists.newArrayList();
        assertThat(list).isNotNull().isEmpty();
    }

    @Test
    public void newHashSet() {
        final Set<Number> set = Sets.newHashSet();
        assertThat(set).isNotNull().isEmpty();
    }

    @Test
    public void newHashMap() {
        final Map<Number, String> map = Maps.newHashMap();
        assertThat(map).isNotNull().isEmpty();
    }

    @Test
    public void immutableListFromStandardList() {
        final List<Number> list = Lists.newArrayList();
        final ImmutableList<Number> immutableList = ImmutableList.copyOf(list);
        assertThat(immutableList).isNotNull().isEmpty();
    }

    @Test
    public void immutableSetFromStandardSet() {
        final Set<Number> set = Sets.newHashSet();
        final ImmutableSet<Number> immutableSet = ImmutableSet.copyOf(set);
        assertThat(immutableSet).isNotNull().isEmpty();
    }

    @Test
    public void immutableMapFromStandardMap() {
        final Map<Number, String> map = Maps.newHashMap();
        final ImmutableMap<Number, String> immutableMap = ImmutableMap.copyOf(map);
        assertThat(immutableMap).isNotNull().isEmpty();
    }

    @Test
    public void addIterableToCollection() {
        final String[] array = {"uno", "due", "tre"};
        final Iterable<String> iterable = Lists.newArrayList(array);
        final Collection<String> collection = Lists.newArrayList();
        Iterables.addAll(collection, iterable);
        assertThat(collection)
                .isNotNull()
                .isNotEmpty()
                .hasSize(array.length)
                .containsExactlyInAnyOrder("uno", "due", "tre");
    }

    @Test
    public void checkIfAnyElementsMatchACondition() {
        final Iterable<String> collection = Lists.newArrayList("a", "bc", "def");
        // 'any' returns 'true' if any element satisfies the predicate
        final boolean containsAny = Iterables.any(collection, elem -> elem != null && elem.length() == 1);
        assertThat(containsAny).isTrue();
        final boolean containsNot = Iterables.any(collection, "uno"::equals);
        assertThat(containsNot).isFalse();
    }


    @Test
    public void checkIfAllElementsMatchACondition() {
        final List<String> names = Lists.newArrayList("John", "Jane", "Adam", "Tom");
        boolean containsAll = Iterables.all(names, Predicates.containsPattern("n|m"));
        assertThat(containsAll).isTrue();
        containsAll = Iterables.all(names, Predicates.containsPattern("a"));
        assertThat(containsAll).isFalse();
    }

    @Test
    public void findWithCustomMatcher() {
        final Iterable<String> collection = Sets.newHashSet("a", "bc", "def");
        final String findAny = Iterables.find(collection, elem -> elem.length() == 1);
        assertThat(findAny).isEqualTo("a");
    }

    @Test
    public void findWithCustomMatcherAndDefaultValue() {
        final String whenNotFound = "";
        final Iterable<String> collection = Sets.newHashSet("a", "bc", "def");
        final String findAny = Iterables.find(collection, elem -> "not found".equals(elem), whenNotFound);
        assertThat(findAny).isEqualTo(whenNotFound);
    }

    @Test
    public void filterWithPredefinedPredicate() {
        final List<String> names = Lists.newArrayList("John", "Jane", "Adam", "Tom");
        // variant 1
        final Iterable<String> iterableResult = Iterables.filter(names, Predicates.containsPattern("a"));
        assertThat(iterableResult).containsExactlyInAnyOrder("Jane", "Adam");
        // variant 2
        final Collection<String> collectionResult = Collections2.filter(names, Predicates.containsPattern("a"));
        assertThat(collectionResult.size()).isEqualTo(2);
        assertThat(collectionResult).containsExactlyInAnyOrder("Jane", "Adam");
        // the result of 'Collections.filter()' is a live view of the original collection; changes to one will be reflected in the other
        collectionResult.add("Anna");
        assertThat(names.size()).isEqualTo(5);
        // 'result' is constrained by the predicate; if we add an element that does’nt satisfy that Predicate, an IllegalArgumentException will be thrown
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> collectionResult.add("Elvis"));
    }

    @Test
    public void filterWithCustomPredicate() {
        final Iterable<String> collection = Sets.newHashSet("a", "bc", "def");
        final Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String input) {
                return input.length() == 1;
            }
        };
        final Iterable<String> resultSet = Iterables.filter(collection, predicate);
        assertThat(resultSet).isNotEmpty();
    }

    @Test
    public void filterWithMultiplePredicates() {
        final List<String> names = Lists.newArrayList("John", "Jane", "Adam", "Tom");
        final Collection<String> result = Collections2.filter(names,
                Predicates.or(
                        Predicates.containsPattern("J"),
                        Predicates.not(Predicates.containsPattern("a"))));
        assertThat(result.size()).isEqualTo(3);
        assertThat(result).containsExactlyInAnyOrder("John", "Jane", "Tom");
    }

    @Test
    public void filterOnlyNonNull() {
        final List<String> names = Lists.newArrayList("John", null, "Jane", null, "Adam", "Tom");
        final Collection<String> result = Collections2.filter(names, Predicates.notNull());
        assertThat(result.size()).isEqualTo(4);
        assertThat(result).containsExactlyInAnyOrder("John", "Jane", "Adam", "Tom");
    }

    @Test
    public void casting() {
        final List<Number> list = Lists.newArrayList();
        final List<Integer> castedList = (List<Integer>) (List<? extends Number>) list;
        assertThat(castedList).isNotNull();
    }

    @Test
    public void hashBasedTable() {
        // first name, last name, age
        final Table<String /*row key*/, String /*column key*/, Integer /*cell value*/> table = HashBasedTable.create();
        table.put("Max", "Mustermann", 44);
        table.put("Biene", "Maya", 4);
        final Integer cellValue = table.get("Biene", "Maya");
        assertThat(table.size()).isEqualTo(2);
        assertThat(cellValue).isEqualTo(4);
    }

    @Test
    public void treeBasedTable() {
        // if a table is needed whose row keys and the column keys need to be ordered by
        // their natural ordering or by supplying comparators, use a TreeBasedTable, which
        // uses TreeMap internally
        final Table<String, String, Integer> table = HashBasedTable.create();
    }

    @Test
    public void transform() {
        final List<String> names = Lists.newArrayList("John", "Jane", "Adam", "Tom");
        final Function<String, Integer> mapper = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        // variant 1
        final Iterable<Integer> iterableResult = Iterables.transform(names, mapper);
        assertThat(iterableResult).contains(4, 4, 4, 3);
        // variant 2
        final Collection<Integer> collectionResult = Collections2.transform(names, mapper);
        assertThat(collectionResult.size()).isEqualTo(4);
        assertThat(collectionResult).contains(4, 4, 4, 3);
        collectionResult.remove(3);
        assertThat(collectionResult.size()).isEqualTo(3);
    }

    @Test
    public void createFunctionFromAPredicate() {
        final List<String> names = Lists.newArrayList("John", "Jane", "Adam", "Tom");
        final Collection<Boolean> result = Collections2.transform(names,
                Functions.forPredicate(Predicates.containsPattern("m")));
        assertThat(result.size()).isEqualTo(4);
        assertThat(result).contains(false, false, true, true);
    }

    @Test
    public void transformWithComposedFunction() {
        final Function<String, Integer> mapper1 = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        final Function<Integer, Boolean> mapper2 = new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };
        final List<String> names = Lists.newArrayList("John", "Jane", "Adam", "Tom");
        final Collection<Boolean> result = Collections2.transform(names, Functions.compose(mapper2, mapper1));
        assertThat(result.size()).isEqualTo(4);
        assertThat(result).contains(true, true, true, false);
    }

    @Test
    public void combineFilterAndTransformWithFluentIterable() {
        final Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.startsWith("A") || input.startsWith("T");
            }
        };
        final Function<String, Integer> mapper = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        final List<String> names = Lists.newArrayList("John", "Jane", "Adam", "Tom");
        final Collection<Integer> result = FluentIterable.from(names)
                .filter(predicate)
                .transform(mapper)
                .toList();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).containsExactlyInAnyOrder(4, 3);
    }
}

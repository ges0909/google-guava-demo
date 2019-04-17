package schrader.guava.test;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.primitives.Ints;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CollectionTests {

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
    public void addAll() {
        final String[] array = { "uno", "due", "tre" };
        final Iterable<String> iterable = Lists.newArrayList(array);
        final Collection<String> collection = Lists.newArrayList();
        Iterables.addAll(collection, iterable);
        assertThat(collection).isNotNull().isNotEmpty().hasSize(array.length).containsExactly("uno", "due", "tre");
    }

    @Test
    public void concatIterables() {
        List<String> list1 = Lists.newArrayList("uno");
        List<String> list2 = Lists.newArrayList("due", "tre");
        List<String> list3 = Lists.newArrayList("quattro", "cinque");
        Iterable<String> iterable = Iterables.concat(list1, list2, list3);
        assertThat(iterable).isNotNull().isNotEmpty().hasSize(list1.size() + list2.size() + list3.size())
                .containsExactly("uno", "due", "tre", "quattro", "cinque");
    }

    @Test
    public void checkIfAnyElementMatchACondition() {
        final Iterable<String> collection = Lists.newArrayList("uno", "due", "tre");
        // 'any' returns 'true' if any element satisfies the predicate
        final boolean containsAny = Iterables.any(collection, new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String input) {
                return input.length() == 3;
            }
        });
        assertThat(containsAny).isTrue();
        final boolean containsNot = Iterables.any(collection, "one"::equals);
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
    public void getSizeOfIterable() {
        List<Integer> list = Lists.newArrayList(1, 2, 3);
        int size = Iterables.size(list);
        assertThat(size).isEqualTo(3);
    }

    @Test
    public void getFirst() {
        List<String> list = Lists.newArrayList("uno", "due", "tre");
        String firstElement = Iterables.getFirst(list, null);
        assertThat(firstElement).isEqualTo("uno");
    }

    @Test
    public void getLast() {
        List<String> list = Lists.newArrayList("uno", "due", "tre");
        String lastElement = Iterables.getLast(list, null);
        assertThat(lastElement).isEqualTo("tre");
    }

    @Test
    public void getOnlyElement() {
        List<String> list = Lists.newArrayList("uno");
        String anyElement = Iterables.getOnlyElement(list);
        assertThat(anyElement).isEqualTo("uno");
    }

    @Test
    public void getOnlyElementFailed() {
        List<String> list = Lists.newArrayList("uno", "due");
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> Iterables.getOnlyElement(list));
    }

    @Test
    public void getElementByIndex() {
        String text = "Bacon ipsum dolor sit amet tri-tip rump shoulder kielbasa strip steak";
        Iterable<String> chars = Splitter.on(CharMatcher.whitespace()).split(text);
        String elementAtPos5 = Iterables.get(chars, 5);
        assertThat(elementAtPos5).isEqualTo(elementAtPos5);
    }

    @Test
    public void findFirstNonNullElementInList() {
        List<String> list = Lists.newArrayList(null, "uno", null, "due", "tre");
        String firstNonNullElement = Iterables.find(list, Predicates.notNull());
        assertThat(firstNonNullElement).isEqualTo("uno");
    }

    @Test
    public void findFirstElementInListWithCustomMatcher() {
        List<Integer> list = Lists.newArrayList(1, 2, 3);
        Integer firstElement = Iterables.find(list, new Predicate<Integer>() {
            public boolean apply(Integer element) {
                return element == 3;
            }
        });
        assertThat(firstElement).isEqualTo(3);
    }

    @Test
    public void findFirstElementInIterableWithCustomMatcher() {
        final Iterable<String> iterable = Sets.newHashSet("zero", "uno", "due", "tre", "otto", "nove");
        final String firstElement = Iterables.find(iterable, new Predicate<String>() {
            public boolean apply(String element) {
                return element.length() == 4;
            }
        });
        assertThat(firstElement).isEqualTo("zero");
    }

    @Test
    public void returnDefaultValueWhenElementNotFound() {
        final String whenNotFound = "";
        final Iterable<String> iterable = Sets.newHashSet("uno", "due", "tre");
        final String notFound = Iterables.find(iterable, new Predicate<String>() {
            public boolean apply(String element) {
                return "not found".equals(element);
            }
        }, whenNotFound);
        assertThat(notFound).isEqualTo(whenNotFound);
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
        // the result of 'Collections.filter()' is a live view of the original
        // collection; changes to one will be reflected in the other
        collectionResult.add("Anna");
        assertThat(names.size()).isEqualTo(5);
        // 'result' is constrained by the predicate; if we add an element that does’nt
        // satisfy that Predicate, an IllegalArgumentException will be thrown
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> collectionResult.add("Elvis"));
    }

    @Test
    public void filterWithCustomPredicate() {
        final Iterable<String> collection = Sets.newHashSet("a", "bc", "def");
        final Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String element) {
                return element.length() == 1;
            }
        };
        final Iterable<String> resultSet = Iterables.filter(collection, predicate);
        assertThat(resultSet).isNotEmpty();
    }

    @Test
    public void filterWithMultiplePredicates() {
        final List<String> names = Lists.newArrayList("John", "Jane", "Adam", "Tom");
        final Collection<String> result = Collections2.filter(names,
                Predicates.or(Predicates.containsPattern("J"), Predicates.not(Predicates.containsPattern("a"))));
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
    public void filterByObjectType() {
        List<Object> mixedTypeList = Lists.newArrayList();
        mixedTypeList.add(new Integer(15));
        mixedTypeList.add(new Double(12));
        mixedTypeList.add("hello");
        mixedTypeList.add(Lists.newArrayList());
        mixedTypeList.add(Maps.newConcurrentMap());
        mixedTypeList.add("world");
        Iterable<String> result = Iterables.filter(mixedTypeList, String.class);
        assertThat(result).containsExactlyInAnyOrder("hello", "world");
    }

    @Test
    public void transformIterable() {
        final List<String> names = Lists.newArrayList("John", "Jane", "Adam", "Tom");
        final Function<String, Integer> mapper = new Function<String, Integer>() {
            @Override
            public Integer apply(String element) {
                return element.length();
            }
        };
        final Iterable<Integer> iterableResult = Iterables.transform(names, mapper);
        assertThat(iterableResult).contains(4, 4, 4, 3);
    }

    @Test
    public void transformCollection() {
        final List<String> names = Lists.newArrayList("John", "Jane", "Adam", "Tom");
        final Function<String, Integer> mapper = new Function<String, Integer>() {
            @Override
            public Integer apply(String element) {
                return element.length();
            }
        };
        final Collection<Integer> collectionResult = Collections2.transform(names, mapper);
        assertThat(collectionResult.size()).isEqualTo(4);
        assertThat(collectionResult).contains(4, 4, 4, 3);
        // changes of the result are reflected in the original collection
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
            public Integer apply(String element) {
                return element.length();
            }
        };
        final Function<Integer, Boolean> mapper2 = new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(Integer element) {
                return element % 2 == 0;
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
            public boolean apply(String element) {
                return element.startsWith("A") || element.startsWith("T");
            }
        };
        final Function<String, Integer> mapper = new Function<String, Integer>() {
            @Override
            public Integer apply(String element) {
                return element.length();
            }
        };
        final List<String> names = Lists.newArrayList("John", "Jane", "Adam", "Tom");
        final Collection<Integer> result = FluentIterable.from(names).filter(predicate).transform(mapper).toList();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).containsExactlyInAnyOrder(4, 3);
    }

    @Test
    public void partitionListIntoSubLists() {
        final List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8);
        final List<List<Integer>> subSets = Lists.partition(list, 3);
        final List<Integer> lastPartition = subSets.get(2);
        final List<Integer> expectedLastPartition = Lists.newArrayList(7, 8);
        assertThat(subSets.size()).isEqualTo(3);
        assertThat(lastPartition).isEqualTo(expectedLastPartition);
    }

    @Test
    public void partitionCollectionIntoSubLists() {
        final Collection<Integer> collection = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8);
        final Iterable<List<Integer>> subSets = Iterables.partition(collection, 3);
        final List<Integer> firstPartition = subSets.iterator().next();
        final List<Integer> expectedLastPartition = Lists.newArrayList(1, 2, 3);
        assertThat(firstPartition).isEqualTo(expectedLastPartition);
    }

    @Test
    public void givenListPartitioned_whenOriginalListIsModified_thenPartitionsChangeAsWell() {
        // keep in mind that the partitions are sublist views of the original
        // collection, which
        // means that changes in the original collection will be reflected in the
        // partitions
        final List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8);
        final List<List<Integer>> subSets = Lists.partition(list, 3);
        list.add(9);
        final List<Integer> lastPartition = subSets.get(2);
        final List<Integer> expectedLastPartition = Lists.newArrayList(7, 8, 9);
    }

    @Test
    public void cast() {
        final List<Number> list = Lists.newArrayList();
        final List<Integer> castedList = (List<Integer>) (List<? extends Number>) list;
        assertThat(castedList).isNotNull();
    }

    @Test
    public void convertListToArray() {
        final List<Integer> list = Lists.newArrayList(0, 1, 2, 3, 4, 5);
        final int[] array = Ints.toArray(list);
        assertThat(array).isEqualTo(new int[] { 0, 1, 2, 3, 4, 5 });
    }

    @Test
    public void convertArrayToList() {
        final Integer[] array = { 0, 1, 2, 3, 4, 5 };
        final List<Integer> list = Lists.newArrayList(array);
        assertThat(list).isEqualTo(Arrays.asList(array));
    }

    @Test
    public void frequencyOfAnObjectInIterable() {
        String jingleChorus = "Oh, jingle bells, jingle bells " + "Jingle all the way " + "Oh, what fun it is to ride "
                + "In a one horse open sleigh " + "Jingle bells, jingle bells " + "Jingle all the way "
                + "Oh, what fun it is to ride " + "In a one horse open sleigh";
        List<String> words = Splitter.on(CharMatcher.anyOf(" .")).trimResults(CharMatcher.is('.')).omitEmptyStrings()
                .splitToList(jingleChorus.toLowerCase());
        int numberOfOccurrences = Iterables.frequency(words, "jingle");
        assertThat(numberOfOccurrences).isEqualTo(6);
    }

    @Test
    public void equalLists() {
        final Iterable<String> list = Lists.newArrayList("A", "B", "C");
        final Iterable<String> list2 = Lists.newArrayList("A", "B", "C");
        assertThat(Iterables.elementsEqual(list, list2)).isTrue();
        assertThat(list.equals(list2)).isTrue(); // alternative
    }

    @Test
    public void groupBy() {
        final List<String> list = Lists.newArrayList("uno", "due", "tre", "quattro", "cinque");
        final Multimap<Integer, String> groupByLength = Multimaps.index(list, new Function<String, Integer>() {
            @Nullable
            @Override
            public Integer apply(@Nullable String input) {
                return input.length();
            }
        });
        assertThat(groupByLength.asMap().size()).isEqualTo(3);
    }

    /*
     * Set<WahlscheinTO> zuDruckendeWahlscheine =
     * FluentIterable.from(wahlscheineZuSpeichern) .transformAndConcat(new
     * Function<WaehlerverzeichnisEintragTO, Set<WahlscheinTO>>() {
     * 
     * @Nullable
     * 
     * @Override public Set<WahlscheinTO> apply(@Nullable
     * WaehlerverzeichnisEintragTO waehlerverzeichnisEintragTO) { return
     * waehlerverzeichnisEintragTO.getWahlscheine(); } }) .filter(new
     * Predicate<WahlscheinTO>() {
     * 
     * @Override public boolean apply(@Nullable WahlscheinTO wahlscheinTO) { return
     * wahlscheinTO.getWahlscheinstatus() == AUSGEHAENDIGT &&
     * wahlscheinTO.getAusgestelltAm() == null; } })
     */
}

package schrader.guava.test;

import com.google.common.collect.*;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class TableTest {

    @Test
    public void hashBasedTable() {
        final Table<String, String, Integer> table = HashBasedTable.create(); // uses LinkedHashMap internally
    }

    @Test
    public void treeBasedTable() {
        final Table<String, String, Integer> table = TreeBasedTable.create(); // for natural ordering of row and column keys; uses TreeMap internally
    }

    @Test
    public void arrayTable() {
        final List<String> rowTable = Lists.newArrayList("Mumbai", "Harvard");
        final List<String> columnTable = Lists.newArrayList("Chemical", "IT", "Electrical");
        final Table<String, String, Integer> table = ArrayTable.create(rowTable, columnTable);
    }

    @Test
    public void immutableTable() {
        Table<String, String, Integer> universityCourseSeatTable = ImmutableTable.<String, String, Integer>builder()
                .put("Mumbai", "Chemical", 120)
                .build();
    }

    /**
     * Retrieval
     */

    @Test
    public void givenTable_whenGet_returnsSuccessfully() {
        Table<String, String, Integer> universityCourseSeatTable = HashBasedTable.create();
        universityCourseSeatTable.put("Mumbai", "Chemical", 120);
        universityCourseSeatTable.put("Mumbai", "IT", 60);
        universityCourseSeatTable.put("Harvard", "Electrical", 60);
        universityCourseSeatTable.put("Harvard", "IT", 120);

        int seatCount = universityCourseSeatTable.get("Mumbai", "IT");
        Integer seatCountForNoEntry = universityCourseSeatTable.get("Oxford", "IT");

        assertThat(seatCount).isEqualTo(60);
        assertThat(seatCountForNoEntry).isNull();
    }

    /**
     * Checking for an Entry
     */

    @Test
    public void givenTable_whenContains_returnsSuccessfully() {
        Table<String, String, Integer> universityCourseSeatTable = HashBasedTable.create();
        universityCourseSeatTable.put("Mumbai", "Chemical", 120);
        universityCourseSeatTable.put("Mumbai", "IT", 60);
        universityCourseSeatTable.put("Harvard", "Electrical", 60);
        universityCourseSeatTable.put("Harvard", "IT", 120);

        boolean entryIsPresent = universityCourseSeatTable.contains("Mumbai", "IT");
        boolean courseIsPresent = universityCourseSeatTable.containsColumn("IT");
        boolean universityIsPresent = universityCourseSeatTable.containsRow("Mumbai");
        boolean seatCountIsPresent = universityCourseSeatTable.containsValue(60);

        assertThat(entryIsPresent).isTrue();
        assertThat(courseIsPresent).isTrue();
        assertThat(universityIsPresent).isTrue();
        assertThat(seatCountIsPresent).isTrue();
    }

    /**
     * Removal
     */

    @Test
    public void givenTable_whenRemove_returnsSuccessfully() {
        Table<String, String, Integer> universityCourseSeatTable = HashBasedTable.create();
        universityCourseSeatTable.put("Mumbai", "Chemical", 120);
        universityCourseSeatTable.put("Mumbai", "IT", 60);

        int seatCount = universityCourseSeatTable.remove("Mumbai", "IT");

        assertThat(seatCount).isEqualTo(60);
        assertThat(universityCourseSeatTable.remove("Mumbai", "IT")).isNull();
    }

    /**
     * Row Key to Cell Value Map
     */

    @Test
    public void givenTable_whenColumn_returnsSuccessfully() {
        Table<String, String, Integer> universityCourseSeatTable = HashBasedTable.create();
        universityCourseSeatTable.put("Mumbai", "Chemical", 120);
        universityCourseSeatTable.put("Mumbai", "IT", 60);
        universityCourseSeatTable.put("Harvard", "Electrical", 60);
        universityCourseSeatTable.put("Harvard", "IT", 120);

        Map<String, Integer> universitySeatMap = universityCourseSeatTable.column("IT");

        assertThat(universitySeatMap).hasSize(2);
        assertThat(universitySeatMap.get("Mumbai")).isEqualTo(60);
        assertThat(universitySeatMap.get("Harvard")).isEqualTo(120);
    }

    /**
     * Map Representation of a Table
     */

    @Test
    public void givenTable_whenColumnMap_returnsSuccessfully() {
        Table<String, String, Integer> universityCourseSeatTable = HashBasedTable.create();
        universityCourseSeatTable.put("Mumbai", "Chemical", 120);
        universityCourseSeatTable.put("Mumbai", "IT", 60);
        universityCourseSeatTable.put("Harvard", "Electrical", 60);
        universityCourseSeatTable.put("Harvard", "IT", 120);

        Map<String, Map<String, Integer>> courseKeyUniversitySeatMap = universityCourseSeatTable.columnMap();

        assertThat(courseKeyUniversitySeatMap).hasSize(3);
        assertThat(courseKeyUniversitySeatMap.get("IT")).hasSize(2);
        assertThat(courseKeyUniversitySeatMap.get("Electrical")).hasSize(1);
        assertThat(courseKeyUniversitySeatMap.get("Chemical")).hasSize(1);
    }

    /**
     * Column Key to Cell Value Map
     */
    @Test
    public void givenTable_whenRow_returnsSuccessfully() {
        Table<String, String, Integer> universityCourseSeatTable = HashBasedTable.create();
        universityCourseSeatTable.put("Mumbai", "Chemical", 120);
        universityCourseSeatTable.put("Mumbai", "IT", 60);
        universityCourseSeatTable.put("Harvard", "Electrical", 60);
        universityCourseSeatTable.put("Harvard", "IT", 120);

        Map<String, Integer> courseSeatMap = universityCourseSeatTable.row("Mumbai");

        assertThat(courseSeatMap).hasSize(2);
        assertThat(courseSeatMap.get("IT")).isEqualTo(60);
        assertThat(courseSeatMap.get("Chemical")).isEqualTo(120);
    }

    /**
     * Get Distinct Row Key
     */

    @Test
    public void givenTable_whenRowKeySet_returnsSuccessfully() {
        Table<String, String, Integer> universityCourseSeatTable = HashBasedTable.create();
        universityCourseSeatTable.put("Mumbai", "Chemical", 120);
        universityCourseSeatTable.put("Mumbai", "IT", 60);
        universityCourseSeatTable.put("Harvard", "Electrical", 60);
        universityCourseSeatTable.put("Harvard", "IT", 120);

        Set<String> universitySet = universityCourseSeatTable.rowKeySet();

        assertThat(universitySet).hasSize(2);
    }

    /**
     * Get Distinct Column Key
     */

    @Test
    public void givenTable_whenColKeySet_returnsSuccessfully() {
        Table<String, String, Integer> universityCourseSeatTable = HashBasedTable.create();
        universityCourseSeatTable.put("Mumbai", "Chemical", 120);
        universityCourseSeatTable.put("Mumbai", "IT", 60);
        universityCourseSeatTable.put("Harvard", "Electrical", 60);
        universityCourseSeatTable.put("Harvard", "IT", 120);

        Set<String> courseSet = universityCourseSeatTable.columnKeySet();

        assertThat(courseSet).hasSize(3);
    }

    @Test
    public void givenTable_convertToArray() {
        final List<String> rowTable = Lists.newArrayList("001", "002");
        final List<String> columnTable = Lists.newArrayList("EU A1", "EU A2", "BM A1");
        final Table<String, String, Integer> table = ArrayTable.create(rowTable, columnTable);
        table.put("001", "EU A1", 100);
        table.put("001", "EU A2", 200);
        table.put("001", "BM A1", 300);
        table.put("002", "EU A1", 50);
        table.put("002", "EU A2", 100);
        table.put("002", "BM A1", 150);

        Integer[][] model = convertTableToArray(table);
        assertThat(model[0][0]).isEqualTo(100);
        assertThat(model[0][1]).isEqualTo(200);
        assertThat(model[0][2]).isEqualTo(300);
        assertThat(model[1][0]).isEqualTo(50);
        assertThat(model[1][1]).isEqualTo(100);
        assertThat(model[1][2]).isEqualTo(150);
    }

    private Integer[][] convertTableToArray(Table<String, String, Integer> table) {
        int rows = table.rowKeySet().size();
        int cols = table.columnKeySet().size();
        Integer[][] array = new Integer[rows][cols];
        int row = 0;
        for (String rowKey : table.rowKeySet()) {
            int col = 0;
            for (String colKey : table.columnKeySet()) {
                Integer value = table.get(rowKey, colKey);
                array[row][col++] = value;
            }
            row++;
        }
        return array;
    }
}

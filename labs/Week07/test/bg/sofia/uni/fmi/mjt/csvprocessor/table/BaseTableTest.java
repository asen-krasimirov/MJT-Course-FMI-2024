package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class BaseTableTest {

    BaseTable baseTable;

    @BeforeEach
    void setUp() {
        baseTable = new BaseTable();
    }

    @Test
    void testAddNullData() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> baseTable.addData(null),
            "addData(...) should throw IllegalArgumentException when called with null value.");
    }

    @Test
    void testAddCorrectDataWhenTableIsEmpty() {
        String[] data = {"header1", "header2", "header3"};

        Assertions.assertDoesNotThrow(() -> baseTable.addData(data),
            "addData(...) should not throw when is called with correct data.");
    }

    @Test
    void testAddDataInIncorrectFormat() {
        String[] data = {"header1", "header2", "header3"};
        Assertions.assertDoesNotThrow(() -> baseTable.addData(data),
            "addData(...) should not throw when is called with correct data.");

        String[] incorrectData1 = {"test4", "test5"};

        Assertions.assertThrows(CsvDataNotCorrectException.class, () -> baseTable.addData(incorrectData1),
            "addData(...) should throw CsvDataNotCorrectException when called with less data parts then number of columns.");

        String[] incorrectData2 = {"test6", "test7", "test8", "test8"};

        Assertions.assertThrows(CsvDataNotCorrectException.class, () -> baseTable.addData(incorrectData2),
            "addData(...) should throw CsvDataNotCorrectException when called with more data parts then number of columns.");
    }

    @Test
    void testAddDataInCorrectFormat() {
        String[] data = {"header1", "header2", "header3"};

        Assertions.assertDoesNotThrow(() -> baseTable.addData(data),
            "addData(...) should not throw when is called with correct data.");

        String[] correctData1 = {"test4", "test5", "test6"};

        Assertions.assertDoesNotThrow(() -> baseTable.addData(correctData1),
            "addData(...) should not throw then called with correct number of data parts.");

    }

    @Test
    void testGetColumnNames() {
        String[] data = {"header1", "header2", "header3"};

        Assertions.assertDoesNotThrow(() -> baseTable.addData(data),
            "addData(...) should not throw when is called with correct data.");

        Set<String> expectedResult = new LinkedHashSet<>();
        expectedResult.add("header1");
        expectedResult.add("header2");
        expectedResult.add("header3");

        Collection<String> result = baseTable.getColumnNames();

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertIterableEquals(expectedResult, result,
            "getColumnNames() should return correct date then called.");
        Assertions.assertThrows(UnsupportedOperationException.class, () -> result.add("test4"),
            "getColumnNames() result should be able to be modified.");
    }

    @Test
    void testGetColumnDataWithNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> baseTable.getColumnData(null),
            "getColumnData(...) should throw IllegalArgumentException when called with null value.");
    }

    @Test
    void testGetColumnDataWithBlank() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> baseTable.getColumnData("   "),
            "getColumnData(...) should throw IllegalArgumentException when called with blank string.");
    }

    @Test
    void testGetColumnDataWithEmpty() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> baseTable.getColumnData(""),
            "getColumnData(...) should throw IllegalArgumentException when called with empty string.");
    }

    @Test
    void testGetColumnDataWithIncorrectColumnHeader() {
        String[] data = {"header1", "header2", "header3"};

        Assertions.assertDoesNotThrow(() -> baseTable.addData(data),
            "addData(...) should not throw when is called with correct data.");

        String[] correctData1 = {"test4", "test5", "test6"};

        Assertions.assertDoesNotThrow(() -> baseTable.addData(correctData1),
            "addData(...) should not throw then called with correct number of data parts.");

        Assertions.assertThrows(IllegalArgumentException.class, () -> baseTable.getColumnData("header4"),
            "getColumnData(...) should throw IllegalArgumentException when called with incorrect column header.");
    }

    @Test
    void testGetColumnDataWithCorrectColumnHeader() {
        String[] data = {"header1", "header2", "header3"};

        Assertions.assertDoesNotThrow(() -> baseTable.addData(data),
            "addData(...) should not throw when is called with correct data.");

        String[] correctData1 = {"test4", "test5", "test6"};

        Assertions.assertDoesNotThrow(() -> baseTable.addData(correctData1),
            "addData(...) should not throw then called with correct number of data parts.");

        String[] correctData2 = {"test7", "test8", "test9"};

        Assertions.assertDoesNotThrow(() -> baseTable.addData(correctData2),
            "addData(...) should not throw then called with correct number of data parts.");

        Collection<String> expectedResult = new LinkedHashSet<>();
        expectedResult.add("test5");
        expectedResult.add("test8");

        Collection<String> result = baseTable.getColumnData("header2");

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertIterableEquals(expectedResult, result,
            "getColumnData(...) should return correct date then called with correct column header.");
        Assertions.assertThrows(UnsupportedOperationException.class, () -> result.add("test4"),
            "getColumnData(...) result should be able to be modified.");
    }

    @Test
    void testGetRowCountWithoutContent() {
        Assertions.assertEquals(0, baseTable.getRowsCount(),
            "getRowsCount() should return 0 when there is nothing in table.");
    }

    @Test
    void testGetRowCountWithContentOnlyHeaders() {
        String[] data = {"header1", "header2", "header3"};

        Assertions.assertDoesNotThrow(() -> baseTable.addData(data),
            "addData(...) should not throw when is called with correct data.");

        Assertions.assertEquals(1, baseTable.getRowsCount(),
            "getRowsCount() should return 1 when there are only headers in table.");
    }

    @Test
    void testGetRowCountWithContent() {
        String[] data = {"header1", "header2", "header3"};

        Assertions.assertDoesNotThrow(() -> baseTable.addData(data),
            "addData(...) should not throw when is called with correct data.");

        String[] correctData = {"test4", "test5", "test6"};

        Assertions.assertDoesNotThrow(() -> baseTable.addData(correctData),
            "addData(...) should not throw then called with correct number of data parts.");


        Assertions.assertEquals(2, baseTable.getRowsCount(),
            "getRowsCount() should return correct rows count when there is content in table.");
    }

}

package bg.sofia.uni.fmi.mjt.csvprocessor.table.column;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class BaseColumnTest {

    BaseColumn baseColumn;

    @BeforeEach
    void setUp() {
        baseColumn = new BaseColumn();
    }

    @Test
    void testAddNullData() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> baseColumn.addData(null),
            "addData(...) should throw IllegalArgumentException when called with null value.");
    }

    @Test
    void testAddBlankData() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> baseColumn.addData("   "),
            "addData(...) should throw IllegalArgumentException when called with blank string.");
    }

    @Test
    void testAddEmptyData() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> baseColumn.addData(""),
            "addData(...) should throw IllegalArgumentException when called with empty string.");
    }

    @Test
    void testAddCorrectData() {
        Assertions.assertDoesNotThrow(() -> baseColumn.addData("testString"),
            "addData(...) should not throw when is called with correct data.");
    }

    @Test
    void testGetData() {
        baseColumn.addData("test1");
        baseColumn.addData("test2");
        baseColumn.addData("test3");

        Collection<String> expectedResult = new ArrayList<>();
        expectedResult.add("test1");
        expectedResult.add("test2");
        expectedResult.add("test3");

        Collection<String> result = baseColumn.getData();

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertIterableEquals(expectedResult, result, "getData(...) should return correct date then called.");
        Assertions.assertThrows(UnsupportedOperationException.class, () -> result.add("test4"),
            "getData(...) result should not be able to be modified.");
    }

}

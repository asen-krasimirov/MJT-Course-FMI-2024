package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

public class MarkdownTablePrinterTest {

    BaseTable baseTable;
    MarkdownTablePrinter markdownTablePrinter;

    @BeforeEach
    void setUp() {
        baseTable = new BaseTable();
        markdownTablePrinter = new MarkdownTablePrinter();
    }

    @Test
    void testPrintTableWithOnlyHeaders() {
        Assertions.assertDoesNotThrow(() -> baseTable.addData(new String[] {"header1", "header2"}),
            "addData(...) should not throw when called with correct data.");

        Collection<String> expectedResult = new ArrayList<>();
        expectedResult.add("| header1 | header2 |");
        expectedResult.add("| :------ | :-----: |");

        Collection<String> result = markdownTablePrinter.printTable(baseTable, ColumnAlignment.LEFT,
            ColumnAlignment.CENTER);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertIterableEquals(expectedResult, result,
            "printTable(...) should return correct date then called with correct data.");
        Assertions.assertThrows(UnsupportedOperationException.class, () -> result.add("test4"),
            "printTable(...) result should not be able to be modified.");
    }

    @Test
    void testPrintTableWithExactNumberOfColumnAlignments() {
        Assertions.assertDoesNotThrow(() -> baseTable.addData(new String[] {"header1", "header2"}),
            "addData(...) should not throw when called with correct data.");
        Assertions.assertDoesNotThrow(() -> baseTable.addData(new String[] {"test1", "test2"}),
            "addData(...) should not throw when called with correct data.");

        Collection<String> expectedResult = new ArrayList<>();
        expectedResult.add("| header1 | header2 |");
        expectedResult.add("| :------ | :-----: |");
        expectedResult.add("| test1   | test2   |");

        Collection<String> result = markdownTablePrinter.printTable(baseTable, ColumnAlignment.LEFT,
            ColumnAlignment.CENTER);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertIterableEquals(expectedResult, result,
            "printTable(...) should return correct date then called with correct data.");
        Assertions.assertThrows(UnsupportedOperationException.class, () -> result.add("test4"),
            "printTable(...) result should not be able to be modified.");
    }

    @Test
    void testPrintTableWithLessColumnAlignmentsThenHeaders() {
        Assertions.assertDoesNotThrow(() -> baseTable.addData(new String[] {"header1", "header2"}),
            "addData(...) should not throw when called with correct data.");
        Assertions.assertDoesNotThrow(() -> baseTable.addData(new String[] {"test1", "test2"}),
            "addData(...) should not throw when called with correct data.");

        Collection<String> expectedResult = new ArrayList<>();
        expectedResult.add("| header1 | header2 |");
        expectedResult.add("| ------- | ------- |");
        expectedResult.add("| test1   | test2   |");

        Collection<String> result = markdownTablePrinter.printTable(baseTable);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertIterableEquals(expectedResult, result,
            "printTable(...) should return correct date then called with correct data.");
        Assertions.assertThrows(UnsupportedOperationException.class, () -> result.add("test4"),
            "printTable(...) result should not be able to be modified.");
    }

    @Test
    void testPrintTableWithMoreColumnAlignmentsThenHeaders() {
        Assertions.assertDoesNotThrow(() -> baseTable.addData(new String[] {"header1", "header2"}),
            "addData(...) should not throw when called with correct data.");
        Assertions.assertDoesNotThrow(() -> baseTable.addData(new String[] {"test1", "test2"}),
            "addData(...) should not throw when called with correct data.");

        Collection<String> expectedResult = new ArrayList<>();
        expectedResult.add("| header1 | header2 |");
        expectedResult.add("| :------ | :-----: |");
        expectedResult.add("| test1   | test2   |");

        Collection<String> result = markdownTablePrinter.printTable(baseTable, ColumnAlignment.LEFT,
            ColumnAlignment.CENTER,
            ColumnAlignment.RIGHT);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertIterableEquals(expectedResult, result,
            "printTable(...) should return correct date then called with correct data.");
        Assertions.assertThrows(UnsupportedOperationException.class, () -> result.add("test4"),
            "printTable(...) result should not be able to be modified.");
    }

    @Test
    void testPrintTableWhenRowHasMaxLength() {
        Assertions.assertDoesNotThrow(() -> baseTable.addData(new String[] {"header1", "header2"}),
            "addData(...) should not throw when called with correct data.");
        Assertions.assertDoesNotThrow(() -> baseTable.addData(new String[] {"test1test1test1", "test2test2"}),
            "addData(...) should not throw when called with correct data.");

        Collection<String> expectedResult = new ArrayList<>();
        expectedResult.add("| header1         | header2    |");
        expectedResult.add("| :-------------- | :--------: |");
        expectedResult.add("| test1test1test1 | test2test2 |");

        Collection<String> result = markdownTablePrinter.printTable(baseTable, ColumnAlignment.LEFT,
            ColumnAlignment.CENTER);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertIterableEquals(expectedResult, result,
            "printTable(...) should return correct date then called with correct data.");
        Assertions.assertThrows(UnsupportedOperationException.class, () -> result.add("test4"),
            "printTable(...) result should not be able to be modified.");
    }
}

package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CsvProcessorTest {

    private Table table;

    private CsvProcessor csvProcessor;

    void testCorrectlyReadDataFromCsv(String delimiter) {
        Table table1 = new BaseTable();
        csvProcessor = new CsvProcessor(table1);

        StringReader reader = new StringReader(
            "header1" + delimiter + "header2" +
                System.lineSeparator() +
                "test3" + delimiter + "test4" +
                System.lineSeparator() +
                "test5" + delimiter + "test6"
        );

        Assertions.assertDoesNotThrow(() -> csvProcessor.readCsv(reader, delimiter),
            "readCsv(...) should not throw when called with correct data.");


        Collection<String> expectedHeaders = List.of("header1", "header2");
        Collection<String> expectedColumn1Data = List.of("test3", "test5");
        Collection<String> expectedColumn2Data = List.of("test4", "test6");

        Assertions.assertIterableEquals(expectedHeaders, table1.getColumnNames(),
            "Table's headers should be read correctly.");

        Assertions.assertIterableEquals(expectedColumn1Data, table1.getColumnData("header1"),
            "Table's column1 data should be read correctly.");

        Assertions.assertIterableEquals(expectedColumn2Data, table1.getColumnData("header2"),
            "Table's column2 data should be read correctly.");
    }

    @Test
    void testCorrectlyReadDataFromCsvWithCommaDelimiter() {
        testCorrectlyReadDataFromCsv(",");
    }

    @Test
    void testCorrectlyReadDataFromCsvWithDotDelimiter() {
        testCorrectlyReadDataFromCsv(".");
    }

    @Test
    void testCorrectlyReadEmptyDataFromCsv() {
        Table table1 = new BaseTable();
        csvProcessor = new CsvProcessor(table1);

        StringReader reader = new StringReader(
            ""
        );

        Assertions.assertDoesNotThrow(() -> csvProcessor.readCsv(reader, ","),
            "readCsv(...) should not throw when called with correct data.");


        Collection<String> expectedHeaders = List.of();

        Assertions.assertIterableEquals(expectedHeaders, table1.getColumnNames(),
            "Table fields should be empty.");
    }

    @Test
    void testWriteTableWithFilledTable() {
        table = mock();
        csvProcessor = new CsvProcessor(table);

        Collection<String> headers = List.of("header1", "header2");
        Collection<String> column1Data = List.of("test3", "test5");
        Collection<String> column2Data = List.of("test4", "test6");

        when(table.getColumnNames()).thenReturn(headers);
        when(table.getColumnData("header1")).thenReturn(column1Data);
        when(table.getColumnData("header2")).thenReturn(column2Data);

        StringWriter writer = new StringWriter();
        csvProcessor.writeTable(writer, ColumnAlignment.LEFT, ColumnAlignment.CENTER);

        String expectedResult = "| header1 | header2 |" +
            System.lineSeparator() +
            "| :------ | :-----: |" +
            System.lineSeparator() +
            "| test3   | test4   |" +
            System.lineSeparator() +
            "| test5   | test6   |";

        Assertions.assertEquals(expectedResult, writer.toString(),
            "writeTable(...) should write data in writer in correct format.");
    }

    @Test
    void testWriteTableWithEmptyTable() {
        table = mock();
        csvProcessor = new CsvProcessor(table);

        Collection<String> headers = List.of();

        when(table.getColumnNames()).thenReturn(headers);

        StringWriter writer = new StringWriter();
        csvProcessor.writeTable(writer);

        String expectedResult = "";

        Assertions.assertEquals(expectedResult, writer.toString(),
            "writeTable(...) should write nothing when table is empty.");
    }
}

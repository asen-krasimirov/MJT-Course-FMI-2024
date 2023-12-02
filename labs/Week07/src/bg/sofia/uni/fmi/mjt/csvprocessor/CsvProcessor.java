package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.MarkdownTablePrinter;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.TablePrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.Collection;

public class CsvProcessor implements CsvProcessorAPI {

    private final Table table;

    private final TablePrinter tablePrinter;

    public CsvProcessor() {
        this(new BaseTable());
    }

    public CsvProcessor(Table table) {
        this.table = table;
        this.tablePrinter = new MarkdownTablePrinter();
    }

    @Override
    public void readCsv(Reader reader, String delimiter) throws CsvDataNotCorrectException {
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            String[] result;

            while ((line = bufferedReader.readLine()) != null) {
                result = line.split("\\Q" + delimiter + "\\E");
                table.addData(result);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("A problem occurred while reading the CSV.", e);
        }
    }

    @Override
    public void writeTable(Writer writer, ColumnAlignment... alignments) {
        Collection<String> rows = tablePrinter.printTable(table, alignments);

        int rowCount = 1;

        try {
            for (String row : rows) {
                writer.append(row);

                if (rowCount < rows.size()) {
                    writer.append(System.lineSeparator());
                }

                rowCount++;
            }

            writer.flush();
        } catch (IOException e) {
            throw new UncheckedIOException("A problem occurred while writing the Table.", e);
        }
    }
}

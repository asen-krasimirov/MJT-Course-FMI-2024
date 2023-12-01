package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MarkdownTablePrinter implements TablePrinter {

    private static final int MIN_MAX_COLUMN_LENGTH = 3;

    private void setUpColumnsData(Table table, ArrayList<String> headers, int[] maxColumnLength,
                                  ArrayList<ArrayList<String>> columnsData) {
        for (int i = 0; i < headers.size(); ++i) {
            ArrayList<String> currentColumnData = new ArrayList<>(table.getColumnData(headers.get(i)));

            maxColumnLength[i] = Math.max(MIN_MAX_COLUMN_LENGTH, headers.get(i).length());

            for (String row : currentColumnData) {
                maxColumnLength[i] = Math.max(maxColumnLength[i], row.length());
            }

            columnsData.add(currentColumnData);
        }
    }

    private void appendHeadersRow(ArrayList<String> headers, int[] maxColumnLength, Collection<String> formattedRows) {
        StringBuilder headerRow = new StringBuilder("|");

        for (int i = 0; i < headers.size(); ++i) {
            headerRow.append(" ");
            headerRow.append(headers.get(i));
            headerRow.append(" ".repeat(maxColumnLength[i] - headers.get(i).length()));
            headerRow.append(" |");
        }

        formattedRows.add(headerRow.toString());
    }

    private void appendAlignmentRow(ColumnAlignment[] alignments, int headersCount, int[] maxColumnLength,
                                    Collection<String> formattedRows) {
        StringBuilder alignmentRow = new StringBuilder("|");

        int i = 0;
        for (; i < headersCount && i < alignments.length; ++i) {
            alignmentRow.append(" ");
            if (alignments[i] == ColumnAlignment.LEFT) {
                alignmentRow.append(":");
                alignmentRow.append("-".repeat(maxColumnLength[i] - 1));
            } else if (alignments[i] == ColumnAlignment.RIGHT) {
                alignmentRow.append("-".repeat(maxColumnLength[i] - 1));
                alignmentRow.append(":");
            } else if (alignments[i] == ColumnAlignment.CENTER) {
                alignmentRow.append(":");
                alignmentRow.append("-".repeat(maxColumnLength[i] - 2));
                alignmentRow.append(":");
            } else if (alignments[i] == ColumnAlignment.NOALIGNMENT) {
                alignmentRow.append("-".repeat(maxColumnLength[i]));
            }
            alignmentRow.append(" |");
        }

        for (; i < headersCount; ++i) {
            alignmentRow.append(" ");
            alignmentRow.append("-".repeat(maxColumnLength[i]));
            alignmentRow.append(" |");
        }

        formattedRows.add(alignmentRow.toString());
    }

    private void appendRow(ArrayList<ArrayList<String>> columnsData, int rowIdx, int[] maxColumnLength,
                           Collection<String> formattedRows) {
        StringBuilder row = new StringBuilder("|");

        for (int columnIdx = 0; columnIdx < columnsData.size(); ++columnIdx) {
            String dataToAdd = columnsData.get(columnIdx).get(rowIdx);

            row.append(" ");
            row.append(dataToAdd);
            row.append(" ".repeat(maxColumnLength[columnIdx] - dataToAdd.length()));
            row.append(" |");
        }

        formattedRows.add(row.toString());
    }

    @Override
    public Collection<String> printTable(Table table, ColumnAlignment... alignments) {
        ArrayList<String> headers = new ArrayList<>(table.getColumnNames());
        int[] maxColumnLength = new int[headers.size()];
        ArrayList<ArrayList<String>> columnsData = new ArrayList<>();

        setUpColumnsData(table, headers, maxColumnLength, columnsData);

        Collection<String> formattedRows = new ArrayList<>();

        appendHeadersRow(headers, maxColumnLength, formattedRows);

        appendAlignmentRow(alignments, headers.size(), maxColumnLength, formattedRows);

        for (int rowIdx = 0; rowIdx < columnsData.get(0).size(); ++rowIdx) {
            appendRow(columnsData, rowIdx, maxColumnLength, formattedRows);
        }

        return Collections.unmodifiableCollection(formattedRows);
    }
}

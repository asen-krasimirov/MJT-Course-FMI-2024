package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;

import java.util.ArrayList;
import java.util.Collection;

public class MarkdownTablePrinter implements TablePrinter {

    private static final int MIN_MAX_COLUMN_LENGTH = 3;

    @Override
    public Collection<String> printTable(Table table, ColumnAlignment... alignments) {
        ArrayList<String> headers = new ArrayList<>(table.getColumnNames());
        int[] maxColumnLength = new int[headers.size()];
        Collection<Collection<String>> columnsData = new ArrayList<>();

        for (int i = 0; i < headers.size(); ++i) {
            Collection<String> currentColumnData = table.getColumnData(headers.get(i));

            for (String row : currentColumnData) {
                maxColumnLength[i] = Math.max(MIN_MAX_COLUMN_LENGTH, Math.max(headers.get(i).length(), row.length()));
            }

            columnsData.add(currentColumnData);
        }

        Collection<String> formattedRows = new ArrayList<>();

        StringBuilder headerRow = new StringBuilder();

        for (int i = 0; i < headers.size(); ++i) {
            headerRow.append("| ");
            headerRow.append(headers.get(i));
            headerRow.append(" ".repeat(maxColumnLength[i] - headers.get(i).length()));
            headerRow.append(" |");
        }

        formattedRows.add(headerRow.toString());

        StringBuilder alignmentRow = new StringBuilder();

        int i = 0;
        for (; i < headers.size() && i < alignments.length; ++i) {
            headerRow.append("| ");

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
            }

            alignmentRow.append(" |");
        }

        for (; i < headers.size(); ++i) {
            alignmentRow.append("| ");
            alignmentRow.append("-".repeat(maxColumnLength[i] - 1));
            alignmentRow.append(" |");
        }

        formattedRows.add(alignmentRow.toString());


//        for (int columnIdx = 0; columnIdx < headers.size(); ++columnIdx) {
//            StringBuilder row = new StringBuilder();
//
//
//        }

        return formattedRows;
    }
}

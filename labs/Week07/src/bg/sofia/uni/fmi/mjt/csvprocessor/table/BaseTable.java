package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.BaseColumn;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.Column;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BaseTable implements Table {

    private final List<String> headers;
    private final Map<String, Column> columns;

    public BaseTable() {
        headers = new ArrayList<>();
        columns = new HashMap<>();
    }

    @Override
    public void addData(String[] data) throws CsvDataNotCorrectException {
        if (data == null) {
            throw new IllegalArgumentException("Value of data should not be null!");
        }

        if (columns.isEmpty()) {
            for (String str : data) {
                headers.add(str);
                columns.put(str, new BaseColumn());
            }
            return;
        }

        if (data.length != columns.size()) {
            throw new CsvDataNotCorrectException(
                "Number of data parts provided should not be different from the count of columns!");
        }

        int i = 0;
        for (String header : headers) {
            columns.get(header).addData(data[i++]);
        }
    }

    @Override
    public Collection<String> getColumnNames() {
        return Collections.unmodifiableCollection(headers);
    }

    @Override
    public Collection<String> getColumnData(String column) {
        if (column == null || column.isBlank() || column.isEmpty()) {
            throw new IllegalArgumentException("Value of data should not be null or blank!");
        } else if (!headers.contains(column)) {
            throw new IllegalArgumentException("Not column with such name found!");
        }

        return columns.get(column).getData();
    }

    @Override
    public int getRowsCount() {
        if (headers.isEmpty()) {
            return 0;
        }

        return columns.get(headers.get(0)).getData().size() + 1;
    }

}

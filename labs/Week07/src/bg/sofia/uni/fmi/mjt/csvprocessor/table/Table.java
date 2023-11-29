package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;

import java.util.Collection;

public interface Table {

    /**
     * Adds data to the table. If the table doesn't have any columns,
     * the data parameter is interpreted as the table headers.
     * If the table contains columns, then the data parameter is interpreted as a row of data.
     * @param data - the data to be added
     * @throws CsvDataNotCorrectException - if the data is in incorrect format - if the count of
     * the provided data parts is less or more than the number of columns in the table
     * @throws IllegalArgumentException if data is null
     */
    void addData(String[] data) throws CsvDataNotCorrectException;

    /**
     * Returns unmodifiable collection of the names of all columns
     */
    Collection<String> getColumnNames();

    /**
     * Returns unmodifiable collection of all strings in a specific column
     * @param column - the column for which the data will be returned
     * @throws IllegalArgumentException is column is null or blank or there is no corresponding column with that name
     * in the table
     */
    Collection<String> getColumnData(String column);

    /**
     * Returns the total count of all rows.
     */
    int getRowsCount();

}

package hr.fer.zemris.projekt.grid;

import hr.fer.zemris.projekt.Move;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Defines a 2 dimensional matrix of {@link Field}s that form a grid where the
 * robot can walk and perform defined {@link Move}s.
 *
 * @author Kristijan Vulinovic
 * @version 1.1.3
 */
public interface IGrid {
    /**
     * Returns the width of the grid.
     *
     * @return the width of the grid.
     */
    int getWidth();

    /**
     * Returns the height of the grid.
     * @return the height of the grid
     */
    int getHeight();

    /**
     * Returns the {@link Field} on the specified row and column.
     *
     * @param row the row of the field.
     * @param column the column of the field.
     *
     * @return the {@link Field} on the specified row and column.
     */
    Field getField(int row, int column);

    /**
     * Sets the {@link Field} in the grid on the specified row and column to the
     * new value.
     *
     * @param row the row of the field.
     * @param column the column of the field.
     * @param newField the new value of the field.
     */
    void setField(int row, int column, Field newField);

    /**
     * Returns the index of the current row of the robot on the grid.
     *
     * @return the index of the current row of the robot on the grid.
     */
    int getCurrentRow();

    /**
     * Returns the index of the current column of the robot on the grid.
     *
     * @return the index of the current column of the robot on the grid.
     */
    int getCurrentColumn();

    /**
     * Sets the starting position of the robot.
     *
     * @param row    index of the row to position the robot on
     * @param column index of the column to position the robot on
     */
    void setStartingPosition(int row, int column);

    /**
     * Returns true if the grid still has uncollected bottles in it,
     * false otherwise.
     *
     * @return true if the grid still has uncollected bottles in it,
     * false otherwise.
     */
    boolean hasBottlesLeft();

    /**
     * Returns the current number of bottles left on the grid.
     *
     * @return the current number of bottles left on the grid.
     */
    int getNumberOfBottles();

    /**
     * Generates a new grid with the specified characteristics.
     *
     * @param width the width of the grid.
     * @param height the height of the grid.
     * @param numberOfBottles the number of bottles that should be on the grid.
     * @param hasWalls a boolean flag that defines if there are walls inside
     *                 of the grid or not.
     */
    void generate(int width, int height, int numberOfBottles, boolean hasWalls);

    /**
     * Creates a new grid that is equal to the field given in the argument.
     *
     * @param gridField the basking matrix for the new grid.
     * @param startRow the starting row for the robot.
     * @param startColumn the starting column for the robot.
     */
    void setGrid(Field[][] gridField, int startRow, int startColumn);

    /**
     * Reads a grid from the file specified in the argument.
     *
     * @param filePath the path to the file describing the grid.
     * @throws IOException if an I/O error occurs when reading from the file.
     * @throws GridFormatException if the file is written in an incorrect format
     */
    void readFromFile(Path filePath) throws IOException;

    /**
     * Writes the grid to a file.
     *
     * @param filePath the path to the file where the grid should be written.
     * @throws IOException if an I/O error occurs when reading from the file.
     */
    void writeToFile(Path filePath) throws IOException;

    /**
     * Creates a new copy of this grid.
     *
     * @return a reference to the new copy.
     */
    IGrid copy();
}

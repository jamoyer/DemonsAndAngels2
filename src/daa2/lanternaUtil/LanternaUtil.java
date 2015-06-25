package daa2.lanternaUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.googlecode.lanterna.terminal.Terminal;

/**
 * A class for static utility methods having to do with the lanterna library.
 *
 * @author Jacob Moyer
 *
 */
public class LanternaUtil
{
    /**
     * This method prints the text string to the terminal term at position col, row.
     *
     * @param term
     *            The terminal to print to.
     * @param text
     *            The string to text to print.
     * @param col
     *            The column to begin printing at.
     * @param row
     *            The row to begin printing at.
     */
    public static void termPrint(final Terminal term, final String text, final int col, final int row)
    {
        term.moveCursor(col, row);
        for (final char c : text.toCharArray())
        {
            term.putCharacter(c);
        }
    }

    /**
     * Returns the index of the center row for the given terminal.
     *
     * @param term
     * @return
     */
    public static int getTerminalCenterRow(final Terminal term)
    {
        return (term.getTerminalSize().getRows() - 1) / 2;
    }

    /**
     * Returns the index of the center column for the given terminal.
     *
     * @param term
     * @return
     */
    public static int getTerminalCenterColumn(final Terminal term)
    {
        return (term.getTerminalSize().getColumns() - 1) / 2;
    }

    /**
     * Draws a border made of the border character around the edge of the terminal.
     *
     * @param term
     * @param border
     */
    public static void drawBorder(final Terminal term, final char border)
    {
        final int numCols = term.getTerminalSize().getColumns();
        final int numRows = term.getTerminalSize().getRows();
        if (numCols <= 0 || numRows <= 0)
        {
            return;
        }

        // draw top border
        for (int col = 0; col < numCols; col++)
        {
            term.moveCursor(col, 0);
            term.putCharacter(border);
        }

        // draw bottom border
        for (int col = 0; col < numCols; col++)
        {
            term.moveCursor(col, numRows - 1);
            term.putCharacter(border);
        }

        // draw left border
        for (int row = 1; row < numRows - 1; row++)
        {
            term.moveCursor(0, row);
            term.putCharacter(border);
        }

        // draw right border
        for (int row = 1; row < numRows - 1; row++)
        {
            term.moveCursor(numCols - 1, row);
            term.putCharacter(border);
        }
    }

    /**
     * Reads from a file and returns a char[][] of its contents. The file is assumed to be of text
     * and every line in the file should be the same length.
     *
     * @param filename
     * @return A char[][] of the file's contents.
     * @throws IOException
     */
    public static char[][] readScreenFile(final String filename) throws IOException
    {
        final BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        final List<String> lines = new LinkedList<String>();
        // read every line of the file into a list
        while ((line = reader.readLine()) != null)
        {
            lines.add(line);
        }

        // if nothing was read, do not return null, just return a char[][] with nothing
        if (lines.isEmpty())
        {
            System.out.println("Nothing read for " + filename);
            reader.close();
            return new char[0][0];
        }

        // some lines have been read, create the array to return the lines in
        final char[][] returnArray = new char[lines.size()][];
        int i = 0;
        for (final String row : lines)
        {
            returnArray[i] = row.toCharArray();
            i++;
        }
        System.out.println("Read " + i + " lines from " + filename);
        reader.close();
        return returnArray;
    }
}

package daa2.lanternaUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.googlecode.lanterna.terminal.Terminal;

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

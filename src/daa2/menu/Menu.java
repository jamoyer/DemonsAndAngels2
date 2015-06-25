package daa2.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

import daa2.lanternaUtil.LanternaUtil;

/**
 * A class for all menus to inherit. The menu will clear the for its size and display all of its
 * options vertically and left-aligned.
 *
 * @author Jacob Moyer
 *
 */
public class Menu
{
    private static final String DEFAULT_CURSOR = "->";
    private static final char SPACE = ' ';

    private final List<MenuOption> options = new ArrayList<MenuOption>();

    private String cursor = DEFAULT_CURSOR;

    private MenuOption backOption;

    private int firstColumn;
    private int lastColumn;
    private int firstRow;
    private int lastRow;

    public Menu(final int firstColumn, final int firstRow) throws IllegalArgumentException
    {
        this(firstColumn, firstRow, null, null);
    }

    public Menu(final int firstColumn, final int firstRow, final MenuOption backOption) throws IllegalArgumentException
    {
        this(firstColumn, firstRow, null, backOption);
    }

    public Menu(final int firstColumn, final int firstRow, final List<MenuOption> options, final MenuOption backOption) throws IllegalArgumentException
    {

        if (firstColumn < 0)
        {
            throw new IllegalArgumentException("First column must be non-negative.");
        }
        this.firstColumn = firstColumn;
        lastColumn = minWidth();

        if (firstRow < 0)
        {
            throw new IllegalArgumentException("First row must be non-negative.");
        }
        this.firstRow = firstRow;

        this.backOption = backOption;

        if (options != null)
        {
            for (final MenuOption option : options)
            {
                addMenuOption(option);
            }
        }
    }

    public void setBackOption(final MenuOption backOption)
    {
        this.backOption = backOption;
    }

    public int getFirstColumn()
    {
        return firstColumn;
    }

    public void setFirstColumn(int firstColumn)
    {
        this.firstColumn = firstColumn;
    }

    public int getFirstRow()
    {
        return firstRow;
    }

    public void setFirstRow(int firstRow)
    {
        this.firstRow = firstRow;
    }

    public String getCursor()
    {
        return cursor;
    }

    public void setCursor(final String cursor)
    {
        lastColumn += cursor.length() - this.cursor.length();
        this.cursor = cursor;
    }

    public List<MenuOption> getMenuOptions()
    {
        return Collections.unmodifiableList(options);
    }

    public void addMenuOption(final MenuOption option)
    {
        options.add(option);
        // the list grows by one
        lastRow++;

        // update the width of the menu if needed
        final int lastColumnOfOption = firstColumn + option.getText().length() + minWidth();
        if (lastColumn < lastColumnOfOption)
        {
            lastColumn = lastColumnOfOption;
        }
    }

    /**
     * Starts the menu by clearing space, drawing the options, and allowing the user to select an
     * option. Returns the option they selected. This method blocks until the user selects input.
     *
     * @param term
     *            The terminal to display the menu on.
     * @return The MenuOption the user selected.
     */
    public MenuOption startMenu(final Terminal term)
    {
        // draw the menu
        drawMenu(term);
        drawCursor(term, 0, 0);

        int prevOption = 0;
        int curOption = 0;

        // continuously read input until an option is selected
        Key key;
        while (true)
        {
            // look for a regular key press or the enter key
            key = term.readInput();
            if (key != null && (key.getKind() == Key.Kind.NormalKey || key.getKind() == Key.Kind.Enter))
            {
                switch (key.getCharacter())
                {
                    case 'w':
                    case 'W':
                        // go up one option
                        prevOption = curOption;
                        curOption = curOption <= 0 ? options.size() - 1 : curOption - 1;
                        drawCursor(term, prevOption, curOption);
                        break;
                    case 's':
                    case 'S':
                        // go down one option
                        prevOption = curOption;
                        curOption = curOption >= options.size() - 1 ? 0 : curOption + 1;
                        drawCursor(term, prevOption, curOption);
                        break;
                    case 'q':
                    case 'Q':
                        // go back
                        return backOption;
                    case 'e':
                    case 'E':
                    case '\n':
                        // option is executed
                        return options.get(curOption);
                }
            }

            try
            {
                // wait for 1 ms
                Thread.sleep(1);
            }
            catch (final InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Overwrites where the cursor used to be with blank spaces and draws it at its new position.
     * Options should be 0-indexed.
     *
     * @param term
     * @param prevOption
     * @param curOption
     */
    private void drawCursor(final Terminal term, int prevOption, int curOption)
    {
        // create a blank string and use it to clear where the cursor used to be
        String blank = "";
        for (int i = 0; i < cursor.length(); i++)
        {
            blank += SPACE;
        }
        LanternaUtil.termPrint(term, blank, firstColumn, firstRow + prevOption);
        LanternaUtil.termPrint(term, cursor, firstColumn, firstRow + curOption);
        term.flush();
    }

    /**
     * Saves the display where the menu will go for later, clears that space, displays all the menu
     * options.
     *
     * @param term
     *            The terminal to display the menu on.
     */
    private void drawMenu(final Terminal term)
    {

        /*
         * Clear the area for the menu.
         */
        for (int row = firstRow; row < lastRow; row++)
        {
            term.moveCursor(firstColumn, row);
            for (int col = firstColumn; col < lastColumn; col++)
            {
                term.putCharacter(SPACE);
            }
        }

        /*
         * Draw all the options
         */
        final int minWidth = minWidth();
        for (int i = 0; i < options.size(); i++)
        {
            LanternaUtil.termPrint(term, options.get(i).getText(), minWidth, i + firstRow);
        }
        term.flush();
    }

    private int minWidth()
    {
        // first column position + space + cursor
        return firstColumn + 1 + cursor.length();
    }
}

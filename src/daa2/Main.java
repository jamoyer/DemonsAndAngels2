package daa2;

import java.io.IOException;
import java.nio.charset.Charset;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.terminal.Terminal;

import daa2.lanternaUtil.LanternaUtil;
import daa2.menu.Menu;
import daa2.menu.MenuOption;

/**
 * Contains the main method and is responsible for switching the game's modes (main menu, starting a
 * game instance, etc)
 *
 * @author Jacob Moyer
 *
 */
public class Main
{
    private enum MainMenuOption
    {
        NewGame,
        LoadGame,
        About,
        Exit;
    }

    // The option variable that will continuously be changed depending on the main menu option
    // doesn't matter what we initialize it to
    private static MainMenuOption selectedOption = MainMenuOption.About;

    // only one screen per game
    private static final Terminal TERMINAL = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF8"));

    public static void main(String[] args)
    {
        final Main game = new Main();
        game.startGame();
    }

    public void startGame()
    {
        TERMINAL.enterPrivateMode();
        TERMINAL.setCursorVisible(false);

        while (true)
        {
            TERMINAL.clearScreen();
            doMainMenuScreen();
            TERMINAL.clearScreen();
            switch (selectedOption)
            {
                case About:
                    doAboutScreen();
                    break;
                case Exit:
                    System.exit(0);
                case LoadGame:
                    doLoadGameScreen();
                    break;
                case NewGame:
                    doNewGameScreen();
                    break;
            }
        }
    }

    /**
     * Begins the main menu screen. The user then selects an option, which changes the selected
     * option member variable, allowing the main loop to choose what to do next.
     */
    public void doMainMenuScreen()
    {
        final Menu menu = new Menu(0, 0);
        menu.addMenuOption(new MenuOption("New Game", () ->
        {
            this.setSelectedOption(MainMenuOption.NewGame);
        }));
        menu.addMenuOption(new MenuOption("Load Game", () ->
        {
            this.setSelectedOption(MainMenuOption.LoadGame);
        }));
        menu.addMenuOption(new MenuOption("About", () ->
        {
            this.setSelectedOption(MainMenuOption.About);
        }));
        menu.addMenuOption(new MenuOption("Exit", () ->
        {
            this.setSelectedOption(MainMenuOption.Exit);
        }));
        final MenuOption option = menu.startMenu(TERMINAL);
        option.getAction().execute();
    }

    private void setSelectedOption(final MainMenuOption option)
    {
        selectedOption = option;
    }

    public void doNewGameScreen()
    {
        System.out.println("New game!");
    }

    public void doLoadGameScreen()
    {
        System.out.println("Load game!");
    }

    public void doAboutScreen()
    {
        char[][] aboutDisplay = null;
        try
        {
            aboutDisplay = LanternaUtil.readScreenFile("src/daa2/resources/screenFiles/About.txt");
        }
        catch (final IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        if (aboutDisplay != null && aboutDisplay.length != 0)
        {
            for (int i = 0; i < aboutDisplay.length; i++)
            {
                LanternaUtil.termPrint(TERMINAL, new String(aboutDisplay[i]), 0, i);
            }
        }
        else
        {
            System.out.println("Nothing read for About screen, returning.");
            return;
        }

        final Menu menu = new Menu(0, 0);
        menu.setCursor("");
        menu.startMenu(TERMINAL);
    }
}

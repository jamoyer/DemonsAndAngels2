package daa2.menu;

/**
 * A class for selectable menu options in a menu.
 *
 * @author Jacob Moyer
 *
 */
public class MenuOption
{
    private final String text;
    private final MenuOptionAction action;

    public MenuOption(final String text, final MenuOptionAction action)
    {
        this.text = text;
        this.action = action;
    }

    public String getText()
    {
        return text;
    }

    public MenuOptionAction getAction()
    {
        return action;
    }
}
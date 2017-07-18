package stan.mym1y.clean.ui;

import stan.mym1y.clean.components.ThemeSwitcher;
import stan.mym1y.clean.cores.ui.Theme;

public class ThemeToggle
    implements ThemeSwitcher
{
    private Theme currentTheme;

    public ThemeToggle(Theme defaultTheme)
    {
        currentTheme = defaultTheme;
    }

    public void switchTheme(Theme theme)
    {
        currentTheme = theme;
    }
    public Theme theme()
    {
        return currentTheme;
    }
}
package stan.mym1y.clean.modules.ui;

import stan.mym1y.clean.cores.ui.Colors;
import stan.mym1y.clean.cores.ui.Theme;

public class ThemeData
    implements Theme
{
    private final boolean isDarkTheme;
    private final Colors colors;

    public ThemeData(boolean idt, Colors cs)
    {
        isDarkTheme = idt;
        colors = cs;
    }

    public boolean isDarkTheme()
    {
        return isDarkTheme;
    }
    public Colors colors()
    {
        return colors;
    }
}
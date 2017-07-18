package stan.mym1y.clean.modules.ui;

import stan.mym1y.clean.cores.ui.Colors;
import stan.mym1y.clean.cores.ui.Theme;

public class ThemeData
    implements Theme
{
    private final Colors colors;

    public ThemeData(Colors cs)
    {
        colors = cs;
    }

    public Colors colors()
    {
        return colors;
    }
}
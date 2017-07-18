package stan.mym1y.clean.modules.ui;

import stan.mym1y.clean.cores.ui.Colors;

public class ColorsData
    implements Colors
{
    private final int background;
    private final int foreground;
    private final int accent;
    private final int positive;
    private final int negative;
    private final int neutral;
    private final int alert;
    private final int confirm;

    public ColorsData(int background, int foreground, int accent, int positive, int negative, int neutral, int alert, int confirm)
    {
        this.background = background;
        this.foreground = foreground;
        this.accent = accent;
        this.positive = positive;
        this.negative = negative;
        this.neutral = neutral;
        this.alert = alert;
        this.confirm = confirm;
    }

    public int background()
    {
        return background;
    }
    public int foreground()
    {
        return foreground;
    }
    public int accent()
    {
        return accent;
    }
    public int positive()
    {
        return positive;
    }
    public int negative()
    {
        return negative;
    }
    public int neutral()
    {
        return neutral;
    }
    public int alert()
    {
        return alert;
    }
    public int confirm()
    {
        return confirm;
    }
}
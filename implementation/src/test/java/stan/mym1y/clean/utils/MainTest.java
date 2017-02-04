package stan.mym1y.clean.utils;

import java.nio.charset.Charset;
import java.util.Random;

public abstract class MainTest
{
    private final Random random = new Random();
    protected void log(String m)
    {
        System.err.println(getClass().getName() + "\n\t" + m);
    }
    protected boolean nextBoolean()
    {
        return random.nextBoolean();
    }
    protected int nextInt()
    {
        return nextInt(Integer.MAX_VALUE-2)+1;
    }
    protected int nextInt(int range)
    {
        return random.nextInt(range);
    }
    protected String nextString()
    {
        byte[] array = new byte[random.nextInt(99)+1];
        random.nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }
}
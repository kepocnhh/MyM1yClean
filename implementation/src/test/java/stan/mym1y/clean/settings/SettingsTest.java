package stan.mym1y.clean.settings;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import stan.mym1y.clean.di.Settings;
import stan.mym1y.clean.managers.PreferenceManager;
import stan.mym1y.clean.utils.RobolectricTest;

import static org.junit.Assert.assertEquals;

public class SettingsTest
        extends RobolectricTest
{
    private Settings settings;

    @Before
    public void before()
    {
        settings = new PreferenceManager(RuntimeEnvironment.application.getApplicationContext());
    }

    @Test
    public void checkSortingType()
    {
        assertEquals(settings.getSortingType(), -1);
        int sortingType = nextInt();
        settings.setSortyngType(sortingType);
        assertEquals(settings.getSortingType(), sortingType);
    }

    @Test
    public void checkUserToken()
    {
        assertEquals(settings.getUserToken(), null);
        String userToken = nextString();
        settings.login(userToken);
        assertEquals(settings.getUserToken(), userToken);
    }
}
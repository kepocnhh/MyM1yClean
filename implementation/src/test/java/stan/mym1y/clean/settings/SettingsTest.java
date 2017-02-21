package stan.mym1y.clean.settings;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.di.Settings;
import stan.mym1y.clean.managers.PreferenceManager;
import stan.mym1y.clean.modules.users.UserData;
import stan.mym1y.clean.utils.RobolectricTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

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
        UserPrivateData data1 = settings.getUserPrivateData();
        assertNotNull(data1);
        assertNull(data1.getUserId());
        assertNull(data1.getUserToken());
        String userId = nextString();
        String token = nextString();
        settings.login(new UserData(userId, token));
        UserPrivateData data2 = settings.getUserPrivateData();
        assertNotNull(data2);
        assertEquals(data2.getUserId(), userId);
        assertEquals(data2.getUserToken(), token);
        settings.logout();
        UserPrivateData data3 = settings.getUserPrivateData();
        assertNotNull(data3);
        assertNull(data3.getUserId());
        assertNull(data3.getUserToken());
    }
}
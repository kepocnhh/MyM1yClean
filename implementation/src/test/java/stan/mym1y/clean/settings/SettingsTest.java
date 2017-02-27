package stan.mym1y.clean.settings;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import stan.mym1y.clean.boxes.Cases;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.di.Settings;
import stan.mym1y.clean.modules.sync.SynchronizationData;
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
        settings = new Cases(RuntimeEnvironment.application.getApplicationContext().getFilesDir().getAbsolutePath());
    }

    @Test
    public void checkUserData()
    {
        UserPrivateData data1 = settings.getUserPrivateData();
        assertNotNull(data1);
        assertNull(data1.getUserId());
        assertNull(data1.getUserToken());
        assertNull(data1.getRefreshToken());
        String userId = nextString();
        String token = nextString();
        String refreshToken = nextString();
        settings.login(new UserData(userId, token, refreshToken));
        UserPrivateData data2 = settings.getUserPrivateData();
        assertNotNull(data2);
        assertEquals(data2.getUserId(), userId);
        assertEquals(data2.getUserToken(), token);
        assertEquals(data2.getRefreshToken(), refreshToken);
        settings.logout();
        UserPrivateData data3 = settings.getUserPrivateData();
        assertNotNull(data3);
        assertNull(data3.getUserId());
        assertNull(data3.getUserToken());
        assertNull(data3.getRefreshToken());
    }

    @Test
    public void checkSyncData()
    {
        SyncData data1 = settings.getSyncData();
        assertNotNull(data1);
        assertNull(data1.getHash());
        assertEquals(data1.getLastSyncTime(), -1);
        long lastSyncTime = nextInt();
        String hash = nextString();
        settings.setSyncData(new SynchronizationData(lastSyncTime, hash));
        SyncData data2 = settings.getSyncData();
        assertNotNull(data2);
        assertEquals(data2.getLastSyncTime(), lastSyncTime);
        assertEquals(data2.getHash(), hash);
    }
}
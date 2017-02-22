package stan.mym1y.clean.modules.users;

import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.utils.MainTest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserDataTest
        extends MainTest
{
    @Test
    public void checkModel()
    {
        String id = nextString();
        String token = nextString();
        String refreshToken = nextString();
        UserPrivateData userData = new UserData(
                id
                ,token
                ,refreshToken
        );
        assertEquals(userData.getUserId(), id);
        assertEquals(userData.getUserToken(), token);
        assertEquals(userData.getRefreshToken(), refreshToken);
    }
}

package stan.mym1y.clean.managers;

import android.content.Context;
import android.content.SharedPreferences;

import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.di.Settings;
import stan.mym1y.clean.modules.users.UserData;

public class PreferenceManager
    implements Settings
{
    static private final String PREFERENCE = PreferenceManager.class.getName().replace(".", "_") + "_" + "preference";
    static private final String SORTING_TYPE = PREFERENCE + "_" + "sorting_type";
    static private final String USER_ID = PREFERENCE + "_" + "user_id";
    static private final String USER_TOKEN = PREFERENCE + "_" + "user_token";

    private SharedPreferences preferences;

    public PreferenceManager(Context context)
    {
        preferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
    }

    @Override
    public int getSortingType()
    {
        return preferences.getInt(SORTING_TYPE, -1);
    }
    @Override
    public void setSortyngType(int type)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SORTING_TYPE, type);
        editor.apply();
    }

    @Override
    public UserPrivateData getUserPrivateData()
    {
        return new UserData(preferences.getString(USER_ID, null), preferences.getString(USER_TOKEN, null));
    }
    @Override
    public void login(UserPrivateData data)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_ID, data.getUserId());
        editor.putString(USER_TOKEN, data.getUserToken());
        editor.apply();
    }
    @Override
    public void logout()
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_TOKEN, null);
        editor.putString(USER_ID, null);
        editor.apply();
    }
}
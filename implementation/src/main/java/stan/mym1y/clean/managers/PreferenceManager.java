package stan.mym1y.clean.managers;

import android.content.Context;
import android.content.SharedPreferences;

import stan.mym1y.clean.di.Settings;

public class PreferenceManager
    implements Settings
{
    static private final String PREFERENCE = PreferenceManager.class.getName().replace(".", "_") + "_" + "preference";
    static private final String SORTING_TYPE = PREFERENCE + "_" + "sorting_type";
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
    public String getUserToken()
    {
        return preferences.getString(USER_TOKEN, null);
    }
    @Override
    public void login(String token)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_TOKEN, token);
        editor.apply();
    }
}
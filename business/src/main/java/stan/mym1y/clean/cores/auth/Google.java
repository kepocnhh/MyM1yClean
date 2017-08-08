package stan.mym1y.clean.cores.auth;

public class Google
{
    static public final String BASE_URL = "https://accounts.google";
    static public String getUrl(String client_id, String redirect_uri)
    {
        return BASE_URL + ".com/o/oauth2/v2/auth?" +
                "client_id="+client_id+"&" +
                "response_type=code&" +
                "redirect_uri="+redirect_uri+"&" +
                "scope=https://www.googleapis.com/auth/userinfo.email";
    }
}
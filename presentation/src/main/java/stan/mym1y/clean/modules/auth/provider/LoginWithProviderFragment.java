package stan.mym1y.clean.modules.auth.provider;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.LoginWithProviderContract;
import stan.mym1y.clean.cores.auth.Google;
import stan.mym1y.clean.cores.auth.Providers;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.data.remote.apis.AuthApi;
import stan.mym1y.clean.units.fragments.UtilFragment;

public class LoginWithProviderFragment
    extends UtilFragment
{
    static private final String TYPE = "provider_type";
    static public UtilFragment newInstance(Providers.Type type, LoginWithProviderContract.Behaviour b)
    {
        LoginWithProviderFragment fragment = new LoginWithProviderFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type.name());
        fragment.setArguments(bundle);
        fragment.behaviour = b;
        return fragment;
    }

    private LoginWithProviderContract.Presenter presenter;
    private final LoginWithProviderContract.View view = new LoginWithProviderContract.View()
    {
        public void error(ErrorsContract.NetworkException e)
        {
//            hideWaiter();
            toast("NetworkException");
        }
        public void error(ErrorsContract.UnauthorizedException e)
        {
//            hideWaiter();
            toast("UnauthorizedException");
        }
        public void error()
        {
//            hideWaiter();
            toast("UnknownErrorException");
        }
        public void success(UserPrivateData data)
        {
            behaviour.login(data);
        }
    };
    private LoginWithProviderContract.Behaviour behaviour;

    private WebView web;

    private Providers.Type providerType;

    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.back:
                back();
                break;
            case R.id.cancel:
                behaviour.exit();
                break;
        }
    }
    protected int getContentView()
    {
        return R.layout.login_with_provider_screen;
    }
    protected void initViews(View v)
    {
        web = findView(R.id.web);
        setClickListener(findView(R.id.back), findView(R.id.cancel));
    }
    protected void init()
    {
        providerType = Providers.Type.valueOf(getArguments().getString(TYPE));
        WebViewClient client = new WebViewClient()
        {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if(url.startsWith(AuthApi.REDIRECT_URI))
                {
                    web.stopLoading();
                    web.loadUrl("about:blank");
                    web.clearHistory();
                    parseUrl(url);
                    return true;
                }
                else if(!url.startsWith(Google.BASE_URL))
                {
                    log("try leave from auth!");
                    return true;
                }
                return false;
            }
            public void onPageStarted(WebView view, String url, Bitmap f)
            {
                log("started - " + url);
            }
            public void onPageFinished(WebView view, String url)
            {
                log("finished - " + url);
            }
            public void onLoadResource(WebView view, String url)
            {
                log("load resource - " + url);
            }
        };
        CookieSyncManager.createInstance(getActivity());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        web.setWebViewClient(client);
        web.getSettings().setUserAgentString("Chrome");
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setAppCacheEnabled(false);
        web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        web.clearCache(true);
        switch(providerType)
        {
            case GOOGLE:
                web.loadUrl(getGoogleUrl());
                break;
            default:
                log("provider " + providerType + " not implemented!");
                break;
        }
        presenter = new LoginWithProviderPresenter(view, new LoginWithProviderModel(App.component().dataRemote().authApi()));
    }
    private String getGoogleUrl()
    {
        return Google.getUrl(AuthApi.CLIENT_ID, AuthApi.REDIRECT_URI);
    }
    private void parseUrl(String url)
    {
        //https://mym1y.app/auth/handler?code=4/PToqIgJJQYCojR0U5z8vnuE-FGKLkepaE1NseRLNRts#
        String tmp = url.substring(AuthApi.REDIRECT_URI.length());
        if(!tmp.startsWith("?") || tmp.indexOf("=") < 2)
        {
            log("Unknown error!");
            return;
        }
        String requestType = tmp.substring(1, tmp.indexOf("="));
        switch(requestType)
        {
            case "code":
                String code = tmp.substring(tmp.indexOf("=") + 1);
                if(code.lastIndexOf("#") == code.length()-1)
                {
                    code = code.substring(0, code.length()-1);
                }
                presenter.login(providerType, code);
                break;
            default:
                log("Unknown request type: " + requestType);
        }
    }

    private void back()
    {
        if(web.canGoBack())
        {
            web.goBack();
        }
        else
        {
            behaviour.exit();
        }
    }
}
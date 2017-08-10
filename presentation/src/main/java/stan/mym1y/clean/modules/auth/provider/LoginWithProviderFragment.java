package stan.mym1y.clean.modules.auth.provider;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
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
        if(type == null)
        {
            throw new NullPointerException("Providers.Type must be not null!");
        }
        switch(type)
        {
            case GOOGLE:
                break;
            default:
                throw new RuntimeException("Providers.Type " + type + " not implemented!");
        }
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
            error();
            toast("NetworkException");
        }
        public void error(ErrorsContract.UnauthorizedException e)
        {
//            hideWaiter();
            error();
            toast("UnauthorizedException");
        }
        public void error()
        {
//            hideWaiter();
            LoginWithProviderFragment.this.error();
            toast("UnknownErrorException");
        }
        public void success(UserPrivateData data)
        {
            behaviour.login(data);
        }
    };
    private LoginWithProviderContract.Behaviour behaviour;

    private WebView web;
    private View back;
    private View waiter;
    private View login_in_process_container;
    private View error_container;

    private Providers.Type providerType;
    private final WebViewClient client = new WebViewClient()
    {
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            if(url.startsWith(AuthApi.REDIRECT_URI))
            {
                web.stopLoading();
//                web.loadUrl("about:blank");
                parseUrl(url);
                return true;
            }
            if(url.toLowerCase().contains("logout"))
            {
                log("try logout!\n" + url);
                web.stopLoading();
                web.loadUrl("about:blank");
                start();
                return true;
            }
            switch(providerType)
            {
                case GOOGLE:
                    if(!url.startsWith(Google.BASE_URL))
                    {
                        log("try leave from auth!\n" + url);
                        return true;
                    }
                    break;
            }
            return false;
        }
        public void onPageStarted(WebView view, String url, Bitmap f)
        {
            log("started - " + url);
            inProcess = true;
            waiter.setVisibility(View.VISIBLE);
            web.setOnTouchListener(new View.OnTouchListener()
            {
                public boolean onTouch(View v, MotionEvent event)
                {
                    return true;
                }
            });
        }
        public void onPageFinished(WebView view, String url)
        {
            log("finished - " + url);
            inProcess = false;
            if(url.equals("about:blank"))
            {
                web.clearHistory();
                needClearHistory = true;
                back.setVisibility(View.GONE);
                waiter.setVisibility(View.GONE);
                return;
            }
            if(url.startsWith(AuthApi.REDIRECT_URI))
            {
                web.clearHistory();
                needClearHistory = true;
                back.setVisibility(View.GONE);
                return;
            }
            if(needClearHistory)
            {
                needClearHistory = false;
                web.clearHistory();
            }
            back.setVisibility(web.canGoBack() ? View.VISIBLE : View.GONE);
            waiter.setVisibility(View.GONE);
            web.setOnTouchListener(new View.OnTouchListener()
            {
                public boolean onTouch(View v, MotionEvent event)
                {
                    return false;
                }
            });
        }
        public void onLoadResource(WebView view, String url)
        {
//            log("load resource - " + url);
        }
        public void onReceivedError(final WebView view, int errorCode, String description, final String failingUrl)
        {
            super.onReceivedError(view, errorCode, description, failingUrl);
            log("error"
                    +"\n\t"+"code " +errorCode
                    +"\n\t"+"description " +description
                    +"\n\t"+"failingUrl " +failingUrl);
            web.stopLoading();
            web.loadUrl("about:blank");
            needClearHistory = true;
            error();
        }
    };
    private volatile boolean needClearHistory = false;
    private volatile boolean inProcess = false;

    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.back:
                if(!inProcess)
                {
                    back();
                }
                break;
            case R.id.cancel:
                behaviour.exit();
                break;
            case R.id.try_again:
                start();
                web.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
                waiter.setVisibility(View.GONE);
                login_in_process_container.setVisibility(View.GONE);
                error_container.setVisibility(View.GONE);
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
        back = findView(R.id.back);
        waiter = findView(R.id.waiter);
        login_in_process_container = findView(R.id.login_in_process_container);
        error_container = findView(R.id.error_container);
        setClickListener(back, findView(R.id.cancel), findView(R.id.try_again));
    }
    protected void init()
    {
        providerType = Providers.Type.valueOf(getArguments().getString(TYPE));
        back.setVisibility(View.GONE);
        waiter.setVisibility(View.GONE);
        login_in_process_container.setVisibility(View.GONE);
        error_container.setVisibility(View.GONE);
        presenter = new LoginWithProviderPresenter(view, new LoginWithProviderModel(App.component().dataRemote().authApi()));
        start();
    }

    private void start()
    {
        CookieSyncManager.createInstance(getActivity());
        CookieManager.getInstance().removeAllCookie();
        web.setWebViewClient(client);
        web.getSettings().setUserAgentString("Chrome");
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setAppCacheEnabled(false);
        web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        web.stopLoading();
        switch(providerType)
        {
            case GOOGLE:
                web.loadUrl(getGoogleUrl());
                break;
        }
    }
    private String getGoogleUrl()
    {
        return Google.getUrl(AuthApi.CLIENT_ID, AuthApi.REDIRECT_URI);
    }
    private void parseUrl(String url)
    {
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
//                web.stopLoading();
                process();
                presenter.login(providerType, code);
                break;
            case "error":
                web.loadUrl("about:blank");
                log("error: " + url);
                error();
                break;
            default:
                web.loadUrl("about:blank");
                log("Unknown request type: " + requestType);
                error();
        }
    }
    private void process()
    {
        web.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        waiter.setVisibility(View.VISIBLE);
        login_in_process_container.setVisibility(View.VISIBLE);
        error_container.setVisibility(View.GONE);
    }
    private void error()
    {
        web.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        waiter.setVisibility(View.GONE);
        login_in_process_container.setVisibility(View.GONE);
        error_container.setVisibility(View.VISIBLE);
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
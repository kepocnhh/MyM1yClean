package stan.mym1y.clean.modules.work;

import android.app.Fragment;
import android.view.View;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.work.MainContract;
import stan.mym1y.clean.contracts.work.MenuContract;
import stan.mym1y.clean.contracts.work.UserInfoCreateContract;
import stan.mym1y.clean.contracts.work.WorkContract;
import stan.mym1y.clean.cores.ui.Theme;
import stan.mym1y.clean.cores.users.UserInfo;
import stan.mym1y.clean.modules.main.MainFragment;
import stan.mym1y.clean.modules.menu.MenuFragment;
import stan.mym1y.clean.modules.settings.SettingsFragment;
import stan.mym1y.clean.modules.userinfo.UserInfoCreateFragment;
import stan.mym1y.clean.units.fragments.UtilFragment;
import stan.mym1y.clean.units.views.DrawerContainer;
import stan.mym1y.clean.utils.ValueAnimator;

public class WorkFragment
        extends UtilFragment
{
    static public UtilFragment newInstance(WorkContract.Behaviour b)
    {
        WorkFragment fragment = new WorkFragment();
        fragment.behaviour = b;
        return fragment;
    }

    private WorkContract.Presenter presenter;
    private final WorkContract.View view = new WorkContract.View()
    {
        public void error(ErrorsContract.UnauthorizedException e)
        {
            behaviour.logout();
        }
        public void error()
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    user_info_frame.setVisibility(View.GONE);
                    drawer_container.setVisibility(View.INVISIBLE);
                    error_container.setVisibility(View.VISIBLE);
                }
            });
        }
    };
    private final WorkContract.Router router = new WorkContract.Router()
    {
        public void toMain()
        {
            log("to -> main");
            clear(R.id.user_info_frame);
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    user_info_frame.setVisibility(View.GONE);
                    drawer_container.setVisibility(View.VISIBLE);
                    error_container.setVisibility(View.GONE);
                }
            });
//            replace(R.id.main_frame, mainFragment);
            replace(R.id.menu_frame, MenuFragment.newInstance(new MenuContract.Behaviour()
            {
                public void screen(MenuContract.Screen screen)
                {
                    log("to -> " + screen);
                    switch(screen)
                    {
                        case TRANSACTIONS:
                            drawer_container.closeDrawer(new DrawerContainer.AnimationEndListener()
                            {
                                public void onAnimationEnd()
                                {
                                    replaceMain(mainFragment);
                                }
                            });
                            break;
                        case SETTINGS:
                            drawer_container.closeDrawer(new DrawerContainer.AnimationEndListener()
                            {
                                public void onAnimationEnd()
                                {
                                    replaceMain(settingsFragment);
                                }
                            });
                            break;
                        default:
                            throw new RuntimeException("screen " + screen + " not implemented!");
                    }
                }
                public void logout()
                {
                    behaviour.logout();
                }
            }));
        }
        public void toUserInfo()
        {
            log("to -> user info");
            clear(R.id.menu_frame);
            clear(R.id.main_frame);
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    drawer_container.setVisibility(View.GONE);
                    user_info_frame.setVisibility(View.VISIBLE);
                    error_container.setVisibility(View.GONE);
                }
            });
            replace(R.id.user_info_frame, UserInfoCreateFragment.newInstance(new UserInfoCreateContract.Behaviour()
            {
                public void success(UserInfo info)
                {
                    presenter.setUserInfo(info);
                }
                public void unauthorized()
                {
                    behaviour.logout();
                }
            }));
        }
    };
    private WorkContract.Behaviour behaviour;

    private final Fragment mainFragment = MainFragment.newInstance(new MainContract.Behaviour()
    {
        public void unauthorized()
        {
            behaviour.logout();
        }
    });
    private final Fragment settingsFragment = new SettingsFragment();

    private View background;
    private DrawerContainer drawer_container;
    private View main_container;
    private View main_frame;
    private View user_info_frame;
    private View error_container;

    private Theme currentTheme;
    private ValueAnimator.Animator replaceAnimator;

    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.try_again:
                error_container.setVisibility(View.GONE);
                presenter.start();
                break;
        }
    }
    protected int getContentView()
    {
        return R.layout.work_screen;
    }
    protected void initViews(View v)
    {
        background = findView(R.id.background);
        drawer_container = findView(R.id.drawer_container);
        main_container = findView(R.id.main_container);
        main_frame = findView(R.id.main_frame);
        user_info_frame = findView(R.id.user_info_frame);
        error_container = findView(R.id.error_container);
        setClickListener(findView(R.id.try_again));
    }
    protected void init()
    {
        drawer_container.setIosStyle(true);
        drawer_container.setIosOffset(2);
        drawer_container.setEdge(false);
        drawer_container.setPadSize(px(56));
        drawer_container.setEdgePadSize(px(16));
        drawer_container.setMovePadSize(px(16));
        drawer_container.setSpeedFactor(2);
        drawer_container.setTweaking(5);
        drawer_container.setScaleStyle(false);
        drawer_container.setScrimFactor(1.5f);
        drawer_container.setDividerWidth(px(1));
        setTheme(App.component().themeSwitcher().theme());
        user_info_frame.setVisibility(View.GONE);
        drawer_container.setVisibility(View.INVISIBLE);
        error_container.setVisibility(View.GONE);
        presenter = new WorkPresenter(view, new WorkModel(App.component().settings(),
                App.component().dataRemote().authApi(),
                App.component().dataRemote().privateDataApi()), router);
        presenter.start();
    }
    private void setTheme(Theme theme)
    {
        currentTheme = theme;
        background.setBackgroundColor(theme.colors().background());
        setStatusBarColor(theme.colors().background());
        setSystemUiVisibilityLight(!theme.isDarkTheme());
        setNavigationBarColor(theme.isDarkTheme() ? theme.colors().background() : theme.colors().foreground());
        drawer_container.setScrimColor(theme.colors().background());
        drawer_container.setDividerColor(theme.colors().foreground());
//        drawer_container.setScrimColor(theme.colors().accent());
//        drawer_container.setBackgroundColor(theme.colors().background());
        main_container.setBackgroundColor(theme.colors().background());
    }

    private void replaceMain(final Fragment fragment)
    {
        if(replaceAnimator != null)
        {
            replaceAnimator.cancel();
        }
        replaceAnimator = ValueAnimator.create(250, 1, 0, new ValueAnimator.Updater<Float>()
        {
            public void update(final Float value)
            {
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        main_frame.setAlpha(value);
                    }
                });
            }
        });
        replaceAnimator.setAnimationListener(new ValueAnimator.AnimationListener()
        {
            public void begin()
            {
            }
            public void end()
            {
                replace(R.id.main_frame, fragment);
                replaceEnd();
            }
            public void cancel()
            {
            }
        });
        replaceAnimator.animate();
    }
    private void replaceEnd()
    {
        if(replaceAnimator != null)
        {
            replaceAnimator.cancel();
        }
        replaceAnimator = ValueAnimator.create(250, 0, 1, new ValueAnimator.Updater<Float>()
        {
            public void update(final Float value)
            {
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        main_frame.setAlpha(value);
                    }
                });
            }
        });
        replaceAnimator.animate();
    }
}
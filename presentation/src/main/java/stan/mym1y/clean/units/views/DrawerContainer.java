package stan.mym1y.clean.units.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import stan.mym1y.clean.utils.ValueAnimator;

public class DrawerContainer
        extends FrameLayout
{
    private View drawerLayout;

    private float density;
    private boolean iosStyle = true;
    private boolean scaleStyle = true;
    private boolean edge = true;
    private float pad = 0;
    private float edgePad = 0;
    private float speedFactor = 1;
    private float iosOffset = 2;
    private float tweaking = 2;
    private int scrimColor = 0;
    private float scrimFactor = 1;

    private int durationAnimation = 150;
    private float drawerPosition;
    private float oldPosition = 0;
    private int drawerWidth;

    private boolean drawerOpened;
    private boolean startedTouch;
    private boolean moveProcess;
    private boolean findScrollHorizontally;
    private boolean multitouch;
    private float startedTrackingX;
    private float startedTrackingY;

    private ValueAnimator.Animator currentAnimator;

    private Paint scrimPaint = new Paint();

    public DrawerContainer(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        density = context.getResources().getDisplayMetrics().density;
        scrimColor = Color.BLACK;
        post(new Runnable()
        {
            public void run()
            {
                findDrawer();
            }
        });
    }

    private void findDrawer()
    {
        for(int i = 0; i < getChildCount(); i++)
        {
            View v = getChildAt(i);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)v.getLayoutParams();
            if(lp.gravity == Gravity.START)
            {
                setDrawerLayout(v);
                return;
            }
        }
    }

    public void setDrawerPosition(float value)
    {
        if(drawerLayout == null)
        {
            return;
        }
        drawerPosition = value - drawerWidth;
        if(drawerPosition > 0)
        {
            drawerPosition = 0;
        }
        else if(drawerPosition < -drawerWidth)
        {
            drawerPosition = -drawerWidth;
        }
        if(iosStyle)
        {
            float mainPosition = drawerPosition + drawerWidth;
            for(int i=0; i<getChildCount(); i++)
            {
                View v = getChildAt(i);
                if(v.equals(drawerLayout))
                {
                    continue;
                }
                if(scaleStyle)
                {
                    float scalingFactor = 1 - ((drawerPosition + drawerWidth)/drawerWidth)/2;
                    Log.e(getClass().getName(), "scalingFactor " + scalingFactor);
                    v.setScaleX(scalingFactor);
                    v.setScaleY(scalingFactor);
                    v.setTranslationX(mainPosition - (getWidth()*(1-scalingFactor))/2);
                }
                else
                {
                    v.setTranslationX(mainPosition);
                }
            }
            drawerLayout.setTranslationX(drawerPosition/getIosOffset());
        }
        else
        {
            drawerLayout.setTranslationX(drawerPosition);
        }
        invalidate();
    }

    public void cancelCurrentAnimation()
    {
        if(currentAnimator != null)
        {
            currentAnimator.cancel();
            currentAnimator = null;
        }
    }

    public void openDrawer()
    {
        moveDrawer(durationAnimation, drawerPosition + drawerWidth, drawerWidth, new ValueAnimator.AnimationListener()
        {
            public void begin()
            {
            }
            public void end()
            {
                drawerOpened = true;
                oldPosition = drawerWidth;
            }
            public void cancel()
            {
            }
        });
    }
    public void moveDrawer(int duration, float start, float end, ValueAnimator.AnimationListener listener)
    {
        if(drawerLayout == null)
        {
            return;
        }
        cancelCurrentAnimation();
        currentAnimator = ValueAnimator.create(duration, start, end, new ValueAnimator.Updater<Float>()
        {
            public void update(final Float value)
            {
                post(new Runnable()
                {
                    public void run()
                    {
                        setDrawerPosition(value);
                    }
                });
            }
        });
        currentAnimator.setAnimationListener(listener);
        currentAnimator.animate();
    }

    public void closeDrawer(final AnimationEndListener listener)
    {
        if(drawerLayout == null)
        {
            return;
        }
        cancelCurrentAnimation();
        moveDrawer(durationAnimation, drawerPosition + drawerWidth, 0, new ValueAnimator.AnimationListener()
        {
            public void begin()
            {
            }
            public void end()
            {
                drawerOpened = false;
                if(listener != null)
                {
                    listener.onAnimationEnd();
                }
                oldPosition = 0;
            }
            public void cancel()
            {
            }
        });
    }

    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if(ev.getAction() == MotionEvent.ACTION_DOWN)
        {
            for(int i=0; i<getChildCount(); i++)
            {
                if(getChildAt(i) == drawerLayout)
                {
                    continue;
                }
                findScrollHorizontally = getChildAt(i) instanceof ViewGroup
                        ? findScrollHorizontally((ViewGroup)getChildAt(i), ev.getX(), ev.getY())
                        : findView(getChildAt(i), ev.getX(), ev.getY());
                if(findScrollHorizontally)
                {
                    break;
                }
            }
        }
//        if(ev.getAction() == MotionEvent.ACTION_DOWN)
//        {
//            Rect rect = new Rect();
//            getGlobalVisibleRect(rect);
//            for(int i=0; i<getChildCount(); i++)
//            {
//                if(getChildAt(i) == drawerLayout)
//                {
//                    continue;
//                }
//                if(getChildAt(i) instanceof ViewGroup)
//                {
//                    if(findScrollHorizontally((ViewGroup)getChildAt(i), ev.getX(), ev.getY() + rect.top))
//                    {
//                        findScrollHorizontally = true;
//                        break;
//                    }
//                }
//                else
//                {
//                    if(findView(getChildAt(i), ev.getX(), ev.getY() + rect.top))
//                    {
//                        findScrollHorizontally = true;
//                        break;
//                    }
//                }
//            }
//        }
//        if(!findScrollHorizontally && !moveProcess && !drawerOpened)
//        {
//            for(int i=0; i<getChildCount(); i++)
//            {
//                if(getChildAt(i) == drawerLayout)
//                {
//                    continue;
//                }
//                findScrollHorizontally = getChildAt(i) instanceof ViewGroup
//                        ? findScrollHorizontally((ViewGroup)getChildAt(i), ev.getX(), ev.getY())
//                        : findView(getChildAt(i), ev.getX(), ev.getY());
//                if(findScrollHorizontally)
//                {
//                    break;
//                }
//            }
//        }
        if(ev.getAction() != MotionEvent.ACTION_MOVE)
        {
            Log.e(getClass().getName(), "intercept touch " + ev.getAction() + " find scroll " + findScrollHorizontally);
        }
//        if(findScrollHorizontally)
//        {
//            super.onInterceptTouchEvent(ev);
//            return false;
//        }
//        Log.e(getClass().getName(), "intercept touch " + ev.getAction());
        if(drawerOpened && ev.getX() > drawerPosition + drawerWidth)
        {
            if(ev.getAction() == MotionEvent.ACTION_UP && !moveProcess)
            {
                onTouchEvent(ev);
            }
            return true;
        }
        switch(ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                onTouchEvent(ev);
                return super.onInterceptTouchEvent(ev);
            }
            case MotionEvent.ACTION_MOVE:
            {
                boolean intercept = super.onInterceptTouchEvent(ev);
                if(intercept)
                {
                    return true;
                }
                else if(startedTouch && !moveProcess)
                {
                    float x = ev.getX() - startedTrackingX;
                    float y = ev.getY() - startedTrackingY;
                    if(!drawerOpened && x < 0)
                    {
                        return super.onInterceptTouchEvent(ev);
                    }
                    if(drawerOpened && x > 0)
                    {
                        return super.onInterceptTouchEvent(ev);
                    }
                    if(Math.abs(x) < 2)
                    {
                    }
                    else if(Math.abs(y) < Math.abs(x)-1)
                    {
                        Log.e(getClass().getName(), "move");
                        onTouchEvent(ev);
                    }
                    else
                    {
                        startedTouch = false;
                        moveProcess = false;
                        if(!drawerOpened)
                        {
                            closeDrawer(null);
                        }
                    }
                }
                else if(moveProcess)
                {
                    onTouchEvent(ev);
                    return true;
                }
//                onTouchEvent(ev);
//                return true;
                return false;
//                return super.onInterceptTouchEvent(ev);
            }
            case MotionEvent.ACTION_CANCEL:
            {
                onTouchEvent(ev);
                return super.onInterceptTouchEvent(ev);
            }
            case MotionEvent.ACTION_UP:
            {
                onTouchEvent(ev);
                return super.onInterceptTouchEvent(ev);
            }
        }
        return super.onInterceptTouchEvent(ev);
    }
    private boolean findScrollHorizontally(ViewGroup viewGroup, float x, float y)
    {
        for(int i=0; i<viewGroup.getChildCount(); i++)
        {
            if(viewGroup.getChildAt(i) instanceof ViewGroup)
            {
                if(findView(viewGroup.getChildAt(i), x, y))
                {
                    return true;
                }
                else if(findScrollHorizontally((ViewGroup)viewGroup.getChildAt(i), x, y))
                {
                    return true;
                }
            }
            else if(findView(viewGroup.getChildAt(i), x, y))
            {
                return true;
            }
        }
        return false;
    }
    private boolean findView(View view, float x, float y)
    {
        if(!view.canScrollHorizontally(LAYOUT_DIRECTION_LTR))
        {
            if(view instanceof RecyclerView)
            {
                RecyclerView recyclerView = (RecyclerView) view;
                Log.e(getClass().getName(), view.getId() + " LAYOUT_DIRECTION_LTR false " + recyclerView.getLayoutManager().canScrollHorizontally());
//                return recyclerView.getLayoutManager().canScrollHorizontally();
            }
            else
            {
                return false;
            }
        }
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        boolean find = rect.left < x && rect.right > x && rect.top < y && rect.bottom > y;
        if(view instanceof RecyclerView)
        {
            RecyclerView recyclerView = (RecyclerView) view;
            Log.e(getClass().getName(), "find RecyclerView " + view.getId() + " xy " +x+ " " +y+
                    " rect " + rect.left + " " + rect.right + " " + rect.top + " " + rect.bottom +
                    " scroll " + recyclerView.getLayoutManager().canScrollHorizontally());
            if(find)
            {
                return recyclerView.getLayoutManager().canScrollHorizontally();
            }
        }
        if(find)
        {
//            if(view instanceof RecyclerView)
//            {
//                RecyclerView recyclerView = (RecyclerView) view;
//                Log.e(getClass().getName(), "find RecyclerView " + view.getClass().getName() + " xy " +x+ " " +y+ " scroll " + recyclerView.getLayoutManager().canScrollHorizontally());
//            }
            Log.e(getClass().getName(), "find scroll horizontally " + view + " xy " +x+ " " +y+ " rect " + rect.left + " " + rect.right + " " + rect.top + " " + rect.bottom);
        }
        else
        {
            if(view instanceof RecyclerView)
            {
                Log.e(getClass().getName(), "miss");
            }
        }
        return find;
    }

    public boolean onTouchEvent(MotionEvent ev)
    {
        if(ev.getAction() != MotionEvent.ACTION_MOVE)
        {
            Log.e(getClass().getName(), "touch " + ev.getAction());
        }
        if(ev.getPointerCount() > 1)
        {
            if(!multitouch)
            {
                multitouch = true;
            }
        }
        if(ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL)
        {
            multitouch = false;
            findScrollHorizontally = false;
        }
        if(drawerOpened && ev.getX() > drawerPosition + drawerWidth && !moveProcess)
        {
            if(ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL)
            {
                startedTouch = false;
                moveProcess = false;
                closeDrawer(null);
                return true;
            }
        }
        if(ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL)
        {
            startedTouch = false;
            if(moveProcess)
            {
                moveProcess = false;
                float x = (ev.getX() - startedTrackingX)*getSpeedFactor();
                if(drawerOpened)
                {
                    x += (drawerWidth / tweaking)*2;
                }
                Log.e(getClass().getName(), "x " + x + " t " + (drawerWidth / tweaking));
                if(x > (drawerWidth / tweaking))
                {
                    openDrawer();
                }
                else
                {
                    closeDrawer(null);
                }
            }
            return false;
        }
        if(ev.getAction() == MotionEvent.ACTION_DOWN)
        {
            if(multitouch || findScrollHorizontally)
            {
                startedTouch = false;
                moveProcess = false;
                closeDrawer(null);
                return false;
            }
//            Log.e(getClass().getName(), "MotionEvent.ACTION_DOWN x " + ev.getX() + " y " + ev.getY());
            if(edge && !drawerOpened && ev.getX() > edgePad)
            {
                return false;
            }
            startedTouch = true;
            startedTrackingX = ev.getX();
            startedTrackingY = ev.getY();
            return true;
        }
        if(ev.getAction() == MotionEvent.ACTION_MOVE && startedTouch)
        {
            if(multitouch || findScrollHorizontally)
            {
                startedTouch = false;
                moveProcess = false;
                closeDrawer(null);
                return false;
            }
            moveProcess = true;
            float newPosition = (ev.getX() - startedTrackingX)*getSpeedFactor();
            if(drawerOpened)
            {
                newPosition += drawerWidth;
            }
            float diff = newPosition - oldPosition;
            if(Math.abs(diff) < drawerWidth/2)
            {
                setDrawerPosition(newPosition);
            }
            else
            {
                moveDrawer((int)Math.abs(diff) / 3, oldPosition, newPosition, null);
            }
            oldPosition = newPosition;
            return true;
        }
        return false;
    }

    protected boolean drawChild(Canvas canvas, View child, long drawingTime)
    {
        if(drawerLayout == null)
        {
            return super.drawChild(canvas, child, drawingTime);
        }
        if(child == getChildAt(0))
        {
            drawChilds(canvas);
        }
        return true;
    }

    private void drawChilds(Canvas canvas)
    {
        if(iosStyle)
        {
            super.drawChild(canvas, drawerLayout, 0);
        }
        for(int i=0; i<getChildCount(); i++)
        {
            if(getChildAt(i) == drawerLayout)
            {
                continue;
            }
            super.drawChild(canvas, getChildAt(i), 0);
        }
        float dpos = drawerPosition + drawerWidth;
        float scrimOpacity = dpos / drawerWidth;
        scrimPaint.setColor(adjustAlpha(scrimColor, scrimOpacity/scrimFactor));
        if(scaleStyle)
        {
            float scalingFactor = 1 - ((drawerPosition + drawerWidth)/drawerWidth)/2;
            canvas.drawRect(dpos, (getHeight() - getHeight()*scalingFactor)/2, getWidth(), getHeight() - (getHeight() - getHeight()*scalingFactor)/2, scrimPaint);
        }
        else
        {
            canvas.drawRect(dpos, 0, getWidth(), getHeight(), scrimPaint);
        }
        if(!iosStyle)
        {
            super.drawChild(canvas, drawerLayout, 0);
        }
    }
    private int adjustAlpha(int color, float factor)
    {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    private void setDrawerLayout(View d)
    {
        this.drawerLayout = d;
        this.drawerLayout.post(new Runnable()
        {
            public void run()
            {
                ViewGroup.LayoutParams lp = drawerLayout.getLayoutParams();
                drawerWidth = getMeasuredWidth() - (int)pad;
                lp.width = drawerWidth;
                Log.e(getClass().getName(), "drawerWidth " + drawerWidth);
                Log.e(getClass().getName(), "drawerLayout " + drawerLayout);
                drawerLayout.setLayoutParams(lp);
                setDrawerPosition(0);
            }
        });
        try
        {
            ColorDrawable color = (ColorDrawable) drawerLayout.getBackground();
            setBackgroundColor(color.getColor());
        }
        catch(Exception e)
        {
            Log.e(getClass().getName(), "setDrawerLayout " + e.getMessage());
        }
    }

    public void setIosStyle(boolean i)
    {
        iosStyle = i;
        if(!iosStyle)
        {
            scaleStyle = false;
        }
        setDrawerPosition(0);
    }
    public void setScaleStyle(boolean s)
    {
        scaleStyle = s;
        if(s)
        {
            setIosStyle(true);
        }
    }
    public void setEdge(boolean e)
    {
        edge = e;
    }
    public void setPadSize(int size)
    {
        pad = size;
    }
    public void setEdgePadSize(int size)
    {
        edgePad = size;
    }
    public void setScrimColor(int color)
    {
        scrimColor = color;
    }
    public void setScrimFactor(float factor)
    {
        if(factor < 1)
        {
            scrimFactor = 1;
        }
        else if(factor > 10)
        {
            scrimFactor = 10;
        }
        else
        {
            scrimFactor = factor;
        }
    }

    public void setSpeedFactor(float f)
    {
        speedFactor = f;
    }
    private float getSpeedFactor()
    {
        return speedFactor;
    }

    public void setIosOffset(float o)
    {
        iosOffset = o;
        if(iosOffset < 1)
        {
            iosOffset = 1;
        }
        else if(iosOffset > 5)
        {
            iosOffset = 5;
        }
    }
    private float getIosOffset()
    {
        if(scaleStyle)
        {
            return 1f;
        }
        return iosOffset;
    }
    public void setTweaking(float t)
    {
        tweaking = t;
        if(tweaking < 1)
        {
            tweaking = 1;
        }
        else if(tweaking > 10)
        {
            tweaking = 10;
        }
    }

    private interface AnimationEndListener
    {
        void onAnimationEnd();
    }
}
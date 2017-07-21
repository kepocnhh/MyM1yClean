package stan.mym1y.clean.utils;

public class ValueAnimator
{
    static final public Interpolator<Integer> linearIntegerInterpolator = new Interpolator<Integer>()
    {
        public Integer interpolate(long time, long timePassed, Integer begin, Integer end)
        {
            return begin + (int)(timePassed*(end - begin)/time);
        }
    };
    static final public Interpolator<Double> linearDoubleInterpolator = new Interpolator<Double>()
    {
        public Double interpolate(long time, long timePassed, Double begin, Double end)
        {
            return begin + timePassed*(end - begin)/time;
        }
    };
    static final public Interpolator<Float> linearFloatInterpolator = new Interpolator<Float>()
    {
        public Float interpolate(long time, long timePassed, Float begin, Float end)
        {
            return begin + timePassed*(end - begin)/time;
        }
    };
    static public Animator create(long time, int begin, int end, Updater<Integer> updater)
    {
        return create(time, begin, end, updater, linearIntegerInterpolator);
    }
    static public Animator create(long time, int begin, int end, Updater<Integer> updater, Interpolator<Integer> interpolator)
    {
        return new Animator<>(time, begin, end, updater, interpolator);
    }
    static public Animator create(int time, double begin, double end, Updater<Double> updater)
    {
        return create(time, begin, end, updater, linearDoubleInterpolator);
    }
    static public Animator create(int time, double begin, double end, Updater<Double> updater, Interpolator<Double> interpolator)
    {
        return new Animator<>(time, begin, end, updater, interpolator);
    }
    static public Animator create(int time, float begin, float end, Updater<Float> updater)
    {
        return create(time, begin, end, updater, linearFloatInterpolator);
    }
    static public Animator create(int time, float begin, float end, Updater<Float> updater, Interpolator<Float> interpolator)
    {
        return new Animator<>(time, begin, end, updater, interpolator);
    }

    private ValueAnimator()
    {

    }

    public interface Updater<T>
    {
        void update(T t);
    }
    public interface Interpolator<T>
    {
        T interpolate(long time, long timePassed, T begin, T end);
    }
    public interface AnimationListener
    {
        void begin();
        void end();
        void cancel();
    }

    static public class AccelerateDoubleInterpolator
            implements Interpolator<Double>
    {
        private double factor;

        public AccelerateDoubleInterpolator(double f)
        {
            factor = f;
        }

        public Double interpolate(long time, long timePassed, Double begin, Double end)
        {
            return begin + (end - begin)*Math.pow(timePassed*(end - begin)/time, factor);
        }
    }
    static public class DecelerateDoubleInterpolator
            implements Interpolator<Double>
    {
        private double factor;

        public DecelerateDoubleInterpolator(double f)
        {
            factor = f;
        }

        public Double interpolate(long time, long timePassed, Double begin, Double end)
        {
            return begin + (end - begin)*(1-Math.pow((1-Math.abs(timePassed*(end - begin))/time), factor));
        }
    }
    static public class AccelerateDecelerateDoubleInterpolator
            implements Interpolator<Double>
    {
        private double factor;

        public AccelerateDecelerateDoubleInterpolator(double f)
        {
            factor = f;
        }

        public Double interpolate(long time, long timePassed, Double begin, Double end)
        {
            return begin + (end - begin)*Math.pow(Math.sin(Math.PI*(timePassed/(double)time)-Math.PI/2)/2+0.5, factor);
        }
    }
    static public class BounceDoubleInterpolator
            implements Interpolator<Double>
    {
        public Double interpolate(long time, long timePassed, Double begin, Double end)
        {
            double l = end - begin;
            double t = timePassed;
            t /= time;
            double d = l;
            if(t<0.31489)
            {
                d *= 8*(1.1226*t)*(1.1226*t);
            }
            else if(t<0.65990)
            {
                d *= 8*(1.1226*t-0.54719)*(1.1226*t-0.54719) + 0.7;
            }
            else if(t<0.85908)
            {
                d *= 8*(1.1226*t-0.8526)*(1.1226*t-0.8526) + 0.9;
            }
            else
            {
                d *= 8*(1.1226*t-1.0435)*(1.1226*t-1.0435) + 0.95;
            }
            return begin + d;
        }
    }
    static public class Animator<T>
    {
        volatile private boolean isCancel;
        private long time;
        private T beginValue;
        private T endValue;
        private Updater<T> updater;
        private Interpolator<T> interpolator;
        private AnimationListener animationListener;

        private long timeBegin;
        //        volatile private long timePassed;
        volatile private long timeNow;
        private long timeEnd;

        private Animator(long t, T b, T e, Updater<T> u, Interpolator<T> inter)
        {
            isCancel = false;
            time = t;
            beginValue = b;
            endValue = e;
            updater = u;
            interpolator = inter;
        }

        public void animate()
        {
            timeBegin = System.currentTimeMillis();
            timeEnd = timeBegin + time;
            timeNow = timeBegin;
            new Thread(new Runnable()
            {
                public void run()
                {
                if(animationListener != null)
                {
                    animationListener.begin();
                }
                while(timeNow < timeEnd && !isCancel)
                {
//                    long tP = timePassed;
                    T oldValue = interpolator.interpolate(time, timeNow-timeBegin, beginValue, endValue);
                    updater.update(oldValue);
                    timeNow = System.currentTimeMillis();
//                    timePassed = timeNow-timeBegin;
//                    while((oldValue.equals(interpolator.interpolate(time, timePassed, beginValue, endValue)) || timePassed - tP < 50) && timeNow < timeEnd && !isCancel)
//                    {
//                        timeNow = System.currentTimeMillis();
//                        timePassed = timeNow-timeBegin;
//                    }
                    while(oldValue.equals(interpolator.interpolate(time, timeNow-timeBegin, beginValue, endValue)) && timeNow < timeEnd && !isCancel)
                    {
                        timeNow = System.currentTimeMillis();
                    }
                    //System.out.println("tP " + (timePassed - tP));
                }
                if(isCancel)
                {
                    if(animationListener != null)
                    {
                        animationListener.cancel();
                    }
                    return;
                }
                updater.update(endValue);
                if(animationListener != null)
                {
                    animationListener.end();
                }
            }}).start();
        }
        public void cancel()
        {
            isCancel = true;
        }

        public Animator<T> setAnimationListener(AnimationListener listener)
        {
            animationListener = listener;
            return this;
        }
    }
}
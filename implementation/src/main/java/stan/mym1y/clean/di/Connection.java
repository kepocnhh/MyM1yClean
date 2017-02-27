package stan.mym1y.clean.di;

import java.util.Map;

import stan.reactive.single.SingleObservable;

public interface Connection
{
    SingleObservable<Answer> get(String url, Map<String, String> params);
    SingleObservable<Answer> post(String url, Map<String, String> params, String body);
    SingleObservable<Answer> put(String url, Map<String, String> params, String body);

    class Answer
    {
        private String data;
        private int code;
        public Answer(String d, int c)
        {
            data = d;
            code = c;
        }

        public String getData()
        {
            return data;
        }
        public int getCode()
        {
            return code;
        }
    }
}
package stan.mym1y.clean.di;

import java.util.Map;

import stan.reactive.Observable;

public interface Connection
{
    Observable<Answer> get(String url);
    Observable<Answer> get(String url, Map<String, String> params);
    Observable<Answer> post(String url, String body);
    Observable<Answer> post(String url, Map<String, String> params, String body);

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
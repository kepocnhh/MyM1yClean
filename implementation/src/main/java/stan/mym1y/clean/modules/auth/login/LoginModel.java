package stan.mym1y.clean.modules.auth.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import stan.json.JSONParser;
import stan.json.JSONWriter;
import stan.json.ParseException;
import stan.mym1y.clean.connection.API;
import stan.mym1y.clean.contracts.auth.AuthContract;
import stan.mym1y.clean.contracts.auth.LoginContract;
import stan.mym1y.clean.di.Connection;
import stan.reactive.Observable;
import stan.reactive.Observer;

class LoginModel
    implements LoginContract.Model
{
    private Connection connection;

    LoginModel(Connection cn)
    {
        connection = cn;
    }

    @Override
    public void checkData(String login, String password)
            throws LoginContract.ValidateDataException
    {
        if(login == null || login.length() == 0
                || password == null || password.length() == 0)
        {
            throw new LoginContract.ValidateDataException();
        }
    }

    @Override
    public Observable<String> login(String login, String password)
    {
        final Map<String, String> params = new HashMap<>();
        params.put("key", API.SERVER_KEY);
        final Map<String, String> body = new HashMap<>();
        body.put("email", login);
        body.put("password", password);
        return new Observable<String>()
        {
            @Override
            public void subscribe(final Observer<String> o)
            {
                try
                {
                    login(o, params, JSONWriter.write(body));
                }
                catch(IOException e)
                {
                    o.error(new AuthContract.UnknownErrorException());
                }
            }
        };
    }
    private void login(final Observer<String> o, Map<String, String> params, String body)
    {
        connection.post(API.Auth.LOGIN, params, body).subscribe(new Observer<Connection.Answer>()
        {
            @Override
            public void next(Connection.Answer response)
            {
                login(o, response);
            }
            @Override
            public void error(Throwable t)
            {
                o.error(new AuthContract.NetworkErrorException());
            }
            @Override
            public void complete()
            {
            }
        });
    }
    private void login(Observer<String> o, Connection.Answer response)
    {
        Map responseBody;
        try
        {
            responseBody = (Map)JSONParser.read(response.getData());
        }
        catch(ParseException | IOException e)
        {
            o.error(new AuthContract.UnknownErrorException());
            return;
        }
        if(response.getCode() == 200)
        {
            o.next((String)responseBody.get("localId"));
        }
        else if(response.getCode() == 400)
        {
            o.error(new AuthContract.UnauthorizedException());
        }
        else
        {
            o.error(new AuthContract.UnknownErrorException());
        }
    }
}

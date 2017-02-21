package stan.mym1y.clean.modules.auth.login;

import java.util.Map;

import stan.json.JSONParser;
import stan.json.JSONWriter;
import stan.mym1y.clean.connection.API;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.LoginContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.di.Connection;
import stan.mym1y.clean.modules.users.UserData;
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
    public Observable<UserPrivateData> login(String login, String password)
    {
        final String jsonBody = JSONWriter.write(API.Auth.Params.getAuthBody(login, password));
        return new Observable<UserPrivateData>()
        {
            @Override
            public void subscribe(Observer<UserPrivateData> o)
            {
                login(o, jsonBody);
            }
        };
    }
    private void login(final Observer<UserPrivateData> o, String body)
    {
        connection.post(API.Auth.LOGIN, API.Auth.Params.getAuthParams(), body).subscribe(new Observer<Connection.Answer>()
        {
            @Override
            public void next(Connection.Answer response)
            {
                login(o, response);
            }
            @Override
            public void error(Throwable t)
            {
                o.error(new ErrorsContract.NetworkErrorException(API.Auth.LOGIN));
            }
            @Override
            public void complete()
            {
            }
        });
    }
    private void login(Observer<UserPrivateData> o, Connection.Answer response)
    {
        if(response.getCode() == 200)
        {
            Map responseBody = (Map)JSONParser.read(response.getData());
            o.next(new UserData((String)responseBody.get("localId"), (String)responseBody.get("idToken")));
            o.complete();
        }
        else if(response.getCode() == 400)
        {
            o.error(new ErrorsContract.UnauthorizedException("login\n" + response.getData() + "\n" + response.getCode()));
        }
        else
        {
            o.error(new ErrorsContract.UnknownErrorException(getClass().getName() + "\nlogin\n" + response.getData()));
        }
    }
}

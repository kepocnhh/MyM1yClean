package stan.mym1y.clean.modules.auth.registration;

import java.util.Map;

import stan.json.JSONParser;
import stan.json.JSONWriter;
import stan.mym1y.clean.connection.API;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.RegistrationContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.di.Connection;
import stan.mym1y.clean.modules.users.UserData;
import stan.reactive.Observable;
import stan.reactive.Observer;

class RegistrationModel
        implements RegistrationContract.Model
{
    private Connection connection;

    RegistrationModel(Connection cn)
    {
        connection = cn;
    }

    @Override
    public void checkData(String login, String password)
            throws RegistrationContract.ValidateDataException
    {
        if(login == null || login.length() == 0
                || password == null || password.length() == 0)
        {
            throw new RegistrationContract.ValidateDataException();
        }
    }

    @Override
    public Observable<UserPrivateData> login(String login, String password)
    {
        final String jsonBody = JSONWriter.write(API.Auth.Params.getAuthBody(login, password));
        return new Observable<UserPrivateData>()
        {
            @Override
            public void subscribe(final Observer<UserPrivateData> o)
            {
                connection.post(API.Auth.REGISTRATION, API.Auth.Params.getAuthParams(), jsonBody).subscribe(new Observer<Connection.Answer>()
                {
                    @Override
                    public void next(Connection.Answer response)
                    {
                        try
                        {
                            checkResponseCode(response.getCode());
                            o.next(getUserPrivateData(response.getData()));
                            o.complete();
                        }
                        catch(ErrorsContract.UnauthorizedException | ErrorsContract.UnknownErrorException e)
                        {
                            o.error(e);
                        }
                        catch(Exception e)
                        {
                            o.error(new ErrorsContract.UnknownErrorException(getClass().getName() + "\nnext " + response));
                        }
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
            private void checkResponseCode(int code)
                    throws ErrorsContract.UnauthorizedException, ErrorsContract.UnknownErrorException
            {
                if(code == 400)
                {
                    throw new ErrorsContract.UnauthorizedException("code: " + code);
                }
                else if(code != 200)
                {
                    throw new ErrorsContract.UnknownErrorException(getClass().getName() + "\ncheckResponseCode \n" + code);
                }
            }
            private UserPrivateData getUserPrivateData(String data)
            {
                Map responseBody = (Map)JSONParser.read(data);
                return new UserData((String)responseBody.get("localId"), (String)responseBody.get("idToken"), (String)responseBody.get("refreshToken"));
            }
        };
    }
}
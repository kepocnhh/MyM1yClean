package stan.mym1y.clean.modules.auth.registration;

import stan.mym1y.clean.connection.API;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.RegistrationContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.di.Connection;
import stan.mym1y.clean.di.JsonConverter;
import stan.reactive.single.SingleObservable;
import stan.reactive.single.SingleObserver;

class RegistrationModel
        implements RegistrationContract.Model
{
    private final Connection connection;
    private final JsonConverter jsonConverter;

    RegistrationModel(Connection cn, JsonConverter jc)
    {
        connection = cn;
        jsonConverter = jc;
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
    public SingleObservable<UserPrivateData> login(String login, String password)
    {
        final String jsonBody = jsonConverter.convertAuthBody(login, password);
        return new SingleObservable<UserPrivateData>()
        {
            @Override
            public void subscribe(final SingleObserver<UserPrivateData> o)
            {
                connection.post(API.Auth.REGISTRATION, API.Auth.Params.getAuthParams(), jsonBody).subscribe(new SingleObserver<Connection.Answer>()
                {
                    @Override
                    public void success(Connection.Answer response)
                    {
                        try
                        {
                            checkResponseCode(response.getCode());
                            o.success(jsonConverter.parseUserPrivateData(response.getData()));
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
        };
    }
}
package stan.mym1y.clean.modules.auth.login;

import java.util.regex.Pattern;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.LoginContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.users.UserSecretData;
import stan.mym1y.clean.data.remote.apis.AuthApi;

class LoginModel
    implements LoginContract.Model
{
    private final AuthApi authApi;

    LoginModel(AuthApi a)
    {
        authApi = a;
    }

    public void checkData(String login, String password)
            throws LoginContract.ValidateDataException
    {
        if(login == null || login.length() == 0)
        {
            throw  new LoginContract.ValidateDataException(LoginContract.ValidateDataException.Error.EMPTY_LOGIN);
        }
        if(password == null || password.length() == 0)
        {
            throw  new LoginContract.ValidateDataException(LoginContract.ValidateDataException.Error.EMPTY_PASSWORD);
        }
        if(password.length() < 6)
        {
            throw  new LoginContract.ValidateDataException(LoginContract.ValidateDataException.Error.PASSWORD_LENGTH);
        }
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        if(!Pattern.compile(ePattern).matcher(login).matches())
        {
            throw  new LoginContract.ValidateDataException(LoginContract.ValidateDataException.Error.LOGIN_VALID);
        }
    }
    public UserPrivateData login(UserSecretData data)
            throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException
    {
        return authApi.postLogin(data);
    }
}
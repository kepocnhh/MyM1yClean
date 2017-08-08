package stan.mym1y.clean.modules.auth.registration;

import java.util.regex.Pattern;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.RegistrationContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.users.UserSecretData;
import stan.mym1y.clean.data.remote.apis.AuthApi;

class RegistrationModel
    implements RegistrationContract.Model
{
    private final AuthApi authApi;

    RegistrationModel(AuthApi a)
    {
        authApi = a;
    }

    public void checkData(String login, String password)
            throws RegistrationContract.ValidateDataException
    {
        if(login == null || login.length() == 0)
        {
            throw  new RegistrationContract.ValidateDataException(RegistrationContract.ValidateDataException.Error.EMPTY_LOGIN);
        }
        if(password == null || password.length() == 0)
        {
            throw  new RegistrationContract.ValidateDataException(RegistrationContract.ValidateDataException.Error.EMPTY_PASSWORD);
        }
        if(password.length() < 6)
        {
            throw  new RegistrationContract.ValidateDataException(RegistrationContract.ValidateDataException.Error.PASSWORD_LENGTH);
        }
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        if(!Pattern.compile(ePattern).matcher(login).matches())
        {
            throw  new RegistrationContract.ValidateDataException(RegistrationContract.ValidateDataException.Error.LOGIN_VALID);
        }
    }
    public UserPrivateData registration(UserSecretData data)
            throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException
    {
        return authApi.postRegistration(data);
    }
}
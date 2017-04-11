package stan.mym1y.clean.modules.auth.registration;

import stan.mym1y.clean.contracts.auth.RegistrationContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.users.UserSecretData;
import stan.mym1y.clean.data.remote.apis.AuthApi;
import stan.reactive.single.SingleObservable;

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
        if(login == null || login.length() == 0
                || password == null || password.length() == 0)
        {
            throw  new RegistrationContract.ValidateDataException();
        }
    }
    public SingleObservable<UserPrivateData> login(UserSecretData data)
    {
        return authApi.postRegistration(data);
    }
}
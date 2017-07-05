package stan.mym1y.clean.modules.cashaccounts;

import java.util.List;

import stan.mym1y.clean.contracts.cashaccounts.AddNewCashAccountContract;
import stan.mym1y.clean.cores.cashaccounts.CashAccountViewModel;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.data.local.models.CurrenciesModels;

class AddNewCashAccountModel
    implements AddNewCashAccountContract.Model
{
    private final CurrenciesModels.Currencies currencies;
    private CashAccountViewModel cashAccountViewModel;

    AddNewCashAccountModel(CurrenciesModels.Currencies cs)
    {
        currencies = cs;
        cashAccountViewModel = new CashAccountView(null, null);
    }

    public List<Currency> getCurrencies()
    {
        return currencies.getAll();
    }
    public void setTitle(String title)
    {
        cashAccountViewModel = new CashAccountView(title, cashAccountViewModel.currencyCodeNumber());
    }
    public void setCurrency(Currency currency)
    {
        cashAccountViewModel = new CashAccountView(cashAccountViewModel.title(), currency.codeNumber());
    }
    public void checkNewCashAccount()
            throws AddNewCashAccountContract.ValidateDataException
    {
        if(cashAccountViewModel.title() == null || cashAccountViewModel.title().isEmpty())
        {
            throw new AddNewCashAccountContract.ValidateDataException(AddNewCashAccountContract.ValidateDataException.Error.EMPTY_TITLE);
        }
    }
    public CashAccountViewModel getNewCashAccount()
    {
        return cashAccountViewModel;
    }
}

package stan.mym1y.clean.modules.main;

import java.util.ArrayList;
import java.util.List;

import stan.mym1y.clean.components.JsonConverter;
import stan.mym1y.clean.components.Security;
import stan.mym1y.clean.components.Settings;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.work.MainContract;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.cashaccounts.CashAccountViewModel;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.network.requests.CashAccountRequest;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.data.Pair;
import stan.mym1y.clean.data.local.models.CashAccountsModels;
import stan.mym1y.clean.data.local.models.CurrenciesModels;
import stan.mym1y.clean.data.local.models.TransactionsModels;
import stan.mym1y.clean.data.remote.apis.AuthApi;
import stan.mym1y.clean.data.remote.apis.PrivateDataApi;
import stan.mym1y.clean.modules.cashaccounts.CashAccountData;
import stan.mym1y.clean.modules.cashaccounts.CashAccountExtra;
import stan.mym1y.clean.modules.data.PairData;
import stan.mym1y.clean.modules.network.requests.CashAccountRequestData;
import stan.mym1y.clean.modules.sync.SynchronizationData;
import stan.mym1y.clean.modules.transactions.TransactionData;
import stan.mym1y.clean.modules.transactions.TransactionExtra;

class MainModel
        implements MainContract.Model
{
    private final TransactionsModels.Transactions transactions;
    private final CashAccountsModels.CashAccounts cashAccounts;
    private final CurrenciesModels.Currencies currencies;
    private final Security security;
    private final Settings settings;
    private final JsonConverter jsonConverter;
    private final AuthApi authApi;
    private final PrivateDataApi privateDataApi;

    MainModel(TransactionsModels.Transactions ts, CashAccountsModels.CashAccounts cas, CurrenciesModels.Currencies crc, Security sm, Settings ss, JsonConverter j, AuthApi aa, PrivateDataApi pda)
    {
        transactions = ts;
        cashAccounts = cas;
        currencies = crc;
        security = sm;
        settings = ss;
        jsonConverter = j;
        authApi = aa;
        privateDataApi = pda;
    }

    public List<Pair<Transaction, Transaction.Extra>> getAllTransactions()
    {
        List<Transaction> ts = transactions.getAll();
        List<Pair<Transaction, Transaction.Extra>> list = new ArrayList<>(ts.size());
        for(Transaction transaction: ts)
        {
            CashAccount cashAccount = cashAccounts.get(transaction.cashAccountId());
            list.add(PairData.create(transaction, TransactionExtra.create(cashAccount.title(), currencies.get(cashAccount.currencyCodeNumber()))));
        }
        return list;
    }

    public List<Pair<CashAccount, CashAccount.Extra>> getAllCashAccounts()
    {
        List<CashAccount> cs = cashAccounts.getAll();
        List<Pair<CashAccount, CashAccount.Extra>> list = new ArrayList<>(cs.size());
        for(CashAccount cashAccount: cs)
        {
            Currency currency = currencies.get(cashAccount.currencyCodeNumber());
            int count = 0;
            int minorCount = 0;
            for(Transaction t : transactions.getAllFromCashAccountId(cashAccount.id()))
            {
                count = t.income() ? count + t.count() : count - t.count();
                minorCount = t.income() ? minorCount + t.minorCount() : minorCount - t.minorCount();
            }
            switch(currency.minorUnitType())
            {
                case TEN:
                    int t1 = count * 10 + minorCount;
                    count = t1 / 10;
                    minorCount = t1 - count * 10;
                    break;
                case HUNDRED:
                    int t2 = count * 100 + minorCount;
                    count = t2 / 100;
                    minorCount = t2 - count * 100;
                    break;
            }
            list.add(PairData.create(cashAccount, CashAccountExtra.create(!(count < 0 || minorCount < 0), Math.abs(count), Math.abs(minorCount), currency)));
        }
        return list;
    }

    public void updateAll()
            throws ErrorsContract.NetworkException, ErrorsContract.DataNotExistException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException
    {
        try
        {
            syncData(privateDataApi.getSyncData(settings.getUserPrivateData()));
        }
        catch(ErrorsContract.UnauthorizedException unauthorizedError)
        {
            settings.login(authApi.postRefreshToken(settings.getUserPrivateData().refreshToken()));
            syncData(privateDataApi.getSyncData(settings.getUserPrivateData()));
        }
    }
    private void syncData(SyncData syncData)
            throws ErrorsContract.UnauthorizedException, ErrorsContract.NetworkException, ErrorsContract.DataNotExistException, UnknownError, ErrorsContract.UnknownException
    {
        if(syncData.lastSyncTime() > settings.getSyncData().lastSyncTime())
        {
            List<CashAccountRequest> cashAccountRequests = privateDataApi.getTransactions(settings.getUserPrivateData());
            cashAccounts.clear();
            transactions.clear();
            for(CashAccountRequest cashAccountRequest: cashAccountRequests)
            {
                cashAccounts.add(cashAccountRequest.cashAccount());
                transactions.addAll(cashAccountRequest.transactions());
            }
        }
        else if(syncData.lastSyncTime() == settings.getSyncData().lastSyncTime() && syncData.hash().equals(settings.getSyncData().hash()))
        {
        }
        else
        {
            privateDataApi.putTransactions(settings.getUserPrivateData(), getCashAccountRequests());
            privateDataApi.putSyncData(settings.getUserPrivateData(), settings.getSyncData());
        }
    }

    public void sendAllUpdatings()
            throws ErrorsContract.NetworkException, ErrorsContract.UnknownException, ErrorsContract.UnauthorizedException
    {
        try
        {
            privateDataApi.putTransactions(settings.getUserPrivateData(), getCashAccountRequests());
            privateDataApi.putSyncData(settings.getUserPrivateData(), settings.getSyncData());
        }
        catch(ErrorsContract.UnauthorizedException unauthorizedError)
        {
            settings.login(authApi.postRefreshToken(settings.getUserPrivateData().refreshToken()));
            privateDataApi.putTransactions(settings.getUserPrivateData(), getCashAccountRequests());
            privateDataApi.putSyncData(settings.getUserPrivateData(), settings.getSyncData());
        }
    }
    public void sendUpdatingsCashAccount(final long cashAccountId)
            throws ErrorsContract.NetworkException, ErrorsContract.UnknownException, ErrorsContract.UnauthorizedException
    {
        try
        {
            privateDataApi.putTransactions(settings.getUserPrivateData(), getCashAccountRequest(cashAccounts.get(cashAccountId)));
            privateDataApi.putSyncData(settings.getUserPrivateData(), settings.getSyncData());
        }
        catch(ErrorsContract.UnauthorizedException unauthorizedError)
        {
            settings.login(authApi.postRefreshToken(settings.getUserPrivateData().refreshToken()));
            privateDataApi.putTransactions(settings.getUserPrivateData(), getCashAccountRequest(cashAccounts.get(cashAccountId)));
            privateDataApi.putSyncData(settings.getUserPrivateData(), settings.getSyncData());
        }
    }
    public CashAccount.Extra getBalance(Currency currency)
    {
        int count = 0;
        int minorCount = 0;
        for(CashAccount cashAccount : cashAccounts.getAllFromCurrencyCodeNumber(currency.codeNumber()))
        {
            for(Transaction t : transactions.getAllFromCashAccountId(cashAccount.id()))
            {
                count = t.income() ? count + t.count() : count - t.count();
                minorCount = t.income() ? minorCount + t.minorCount() : minorCount - t.minorCount();
            }
        }
        switch(currency.minorUnitType())
        {
            case TEN:
                int t1 = count * 10 + minorCount;
                count = t1 / 10;
                minorCount = t1 - count * 10;
                break;
            case HUNDRED:
                int t2 = count * 100 + minorCount;
                count = t2 / 100;
                minorCount = t2 - count * 100;
                break;
        }
        return CashAccountExtra.create(!(count < 0 || minorCount < 0), Math.abs(count), Math.abs(minorCount), currency);
    }
    private CashAccountRequest getCashAccountRequest(CashAccount cashAccount)
    {
        return new CashAccountRequestData(cashAccount, transactions.getAllFromCashAccountId(cashAccount.id()));
    }
    private List<CashAccountRequest> getCashAccountRequests()
    {
        List<CashAccountRequest> list = new ArrayList<>();
        for(CashAccount cashAccount: cashAccounts.getAll())
        {
            list.add(getCashAccountRequest(cashAccount));
        }
        return list;
    }
    public void delete(CashAccount cashAccount)
    {
        cashAccounts.remove(cashAccount.id());
        transactions.removeAllFromCashAccountId(cashAccount.id());
        updateSyncData();
    }
    public void delete(Transaction transaction)
    {
        transactions.remove(transaction.id());
        updateSyncData();
    }

    public CashAccount add(final CashAccountViewModel cashAccountViewModel)
    {
        CashAccount newCashAccount = new CashAccountData(security.newUniqueId(), security.newUUID(), cashAccountViewModel.currencyCodeNumber(), cashAccountViewModel.title());
        cashAccounts.add(newCashAccount);
        updateSyncData();
        return newCashAccount;
    }
    public Transaction add(final TransactionViewModel transactionViewModel)
    {
        Transaction newTransaction = new TransactionData(security.newUniqueId(),
                transactionViewModel.cashAccountId(),
                transactionViewModel.date(),
                transactionViewModel.income(),
                transactionViewModel.count(),
                transactionViewModel.minorCount());
        transactions.add(newTransaction);
        updateSyncData();
        return newTransaction;
    }
    public void sync()
    {
        updateSyncData();
    }

    private void updateSyncData()
    {
        List<Transaction> list = transactions.getAll();
        settings.setSyncData(new SynchronizationData(System.currentTimeMillis(), security.sha512(System.currentTimeMillis() + "" + list.size() + "" + list.hashCode() + jsonConverter.get(list))));
    }
}
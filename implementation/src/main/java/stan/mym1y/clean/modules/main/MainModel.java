package stan.mym1y.clean.modules.main;

import java.util.ArrayList;
import java.util.List;

import stan.mym1y.clean.components.JsonConverter;
import stan.mym1y.clean.components.Security;
import stan.mym1y.clean.components.Settings;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.cashaccounts.CashAccountViewModel;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.network.requests.CashAccountRequest;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.data.local.models.CashAccountsModels;
import stan.mym1y.clean.data.local.models.CurrenciesModels;
import stan.mym1y.clean.data.local.models.TransactionsModels;
import stan.mym1y.clean.data.remote.apis.AuthApi;
import stan.mym1y.clean.data.remote.apis.PrivateDataApi;
import stan.mym1y.clean.modules.cashaccounts.CashAccountData;
import stan.mym1y.clean.modules.cashaccounts.CashAccountExtra;
import stan.mym1y.clean.modules.network.requests.CashAccountRequestData;
import stan.mym1y.clean.modules.sync.SynchronizationData;
import stan.mym1y.clean.modules.transactions.TransactionData;
import stan.mym1y.clean.modules.transactions.TransactionExtra;
import stan.reactive.Tuple;
import stan.reactive.functions.Action;
import stan.reactive.functions.Apply;
import stan.reactive.notify.NotifyObservable;
import stan.reactive.single.SingleObservable;

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

    public List<Tuple<Transaction, Transaction.Extra>> getAllTransactions()
    {
        List<Transaction> ts = transactions.getAll();
        List<Tuple<Transaction, Transaction.Extra>> list = new ArrayList<>(ts.size());
        for(Transaction transaction: ts)
        {
            CashAccount cashAccount = cashAccounts.get(transaction.cashAccountId());
            list.add(new FullTransaction(transaction, new TransactionExtra(cashAccount.title(), currencies.get(cashAccount.currencyCodeNumber()))));
        }
        return list;
    }
    private class FullTransaction
            implements Tuple<Transaction, Transaction.Extra>
    {
        private final Transaction first;
        private final Transaction.Extra second;

        FullTransaction(Transaction f, Transaction.Extra s)
        {
            first = f;
            second = s;
        }

        public Transaction first()
        {
            return first;
        }
        public Transaction.Extra second()
        {
            return second;
        }
    }

    public List<Tuple<CashAccount, CashAccount.Extra>> getAllCashAccounts()
    {
        List<CashAccount> cs = cashAccounts.getAll();
        List<Tuple<CashAccount, CashAccount.Extra>> list = new ArrayList<>(cs.size());
        for(CashAccount cashAccount: cs)
        {
            Currency currency = currencies.get(cashAccount.currencyCodeNumber());
            int count = 0;
            int minorCount = 0;
            for(Transaction t : transactions.getAllFromCashAccountId(cashAccount.id()))
            {
                count += t.count();
                minorCount = t.count() < 0 ? minorCount - t.minorCount() : minorCount + t.minorCount();
            }
            switch(currency.minorUnitType())
            {
                case TEN:
                    int t1 = minorCount / 10;
                    count += t1;
                    minorCount -= t1;
                    break;
                case HUNDRED:
                    int t2 = minorCount / 100;
                    count += t2;
                    minorCount -= t2;
                    break;
            }
            list.add(new FullCashAccount(cashAccount, new CashAccountExtra(count, Math.abs(minorCount), currency)));
        }
        return list;
    }
    private class FullCashAccount
            implements Tuple<CashAccount, CashAccount.Extra>
    {
        private final CashAccount first;
        private final CashAccount.Extra second;

        FullCashAccount(CashAccount f, CashAccount.Extra s)
        {
            first = f;
            second = s;
        }

        public CashAccount first()
        {
            return first;
        }
        public CashAccount.Extra second()
        {
            return second;
        }
    }

    public NotifyObservable updateAll()
    {
        return NotifyObservable.create(new Action()
        {
            public void run()
                    throws Throwable
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
        });
    }
    private void syncData(SyncData syncData)
            throws ErrorsContract.UnauthorizedException, ErrorsContract.NetworkException, ErrorsContract.DataNotExistException, UnknownError
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

    public NotifyObservable sendAllUpdatings()
    {
        return NotifyObservable.create(new Action()
        {
            public void run()
                    throws Throwable
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
        });
    }
    public NotifyObservable sendUpdatingsCashAccount(final long cashAccountId)
    {
        return NotifyObservable.create(new Action()
        {
            public void run()
                    throws Throwable
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
        });
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
    public int getBalance()
    {
        int b = 0;
        for(Transaction t : transactions.getAll())
        {
            b += t.count();
        }
        return b;
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

    public SingleObservable<CashAccount> add(final CashAccountViewModel cashAccountViewModel)
    {
        return SingleObservable.create(new Apply<CashAccount>()
        {
            public CashAccount apply()
            {
                CashAccount newCashAccount = new CashAccountData(security.newUniqueId(), security.newUUID(), cashAccountViewModel.currencyCodeNumber(), cashAccountViewModel.title());
                cashAccounts.add(newCashAccount);
                updateSyncData();
                return newCashAccount;
            }
        });
    }
    public SingleObservable<Transaction> add(final TransactionViewModel transactionViewModel)
    {
        return SingleObservable.create(new Apply<Transaction>()
        {
            public Transaction apply()
            {
                Transaction newTransaction = new TransactionData(security.newUniqueId(), transactionViewModel.cashAccountId(), transactionViewModel.date(), transactionViewModel.count(), transactionViewModel.minorCount());
                transactions.add(newTransaction);
                updateSyncData();
                return newTransaction;
            }
        });
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
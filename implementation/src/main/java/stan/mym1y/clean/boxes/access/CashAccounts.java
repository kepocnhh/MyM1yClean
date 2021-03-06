package stan.mym1y.clean.boxes.access;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stan.boxes.Box;
import stan.boxes.ORM;
import stan.boxes.Query;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.data.local.access.CashAccountsAccess;
import stan.mym1y.clean.data.local.models.CashAccountsModels;
import stan.mym1y.clean.modules.cashaccounts.CashAccountData;

public class CashAccounts
    implements CashAccountsAccess
{
    private final Box<CashAccount> cashAccountsBox;
    private final CashAccountsModels.CashAccounts cashAccounts = new CashAccountsModels.CashAccounts()
    {
        public List<CashAccount> getAll()
        {
            return cashAccountsBox.getAll();
        }
        public List<CashAccount> getAllFromCurrencyCodeNumber(final String codeNumber)
        {
            return cashAccountsBox.get(new Query<CashAccount>()
            {
                public boolean query(CashAccount cashAccount)
                {
                    return cashAccount.currencyCodeNumber().equals(codeNumber);
                }
            });
        }
        public CashAccount get(final long id)
        {
            List<CashAccount> transactions = cashAccountsBox.get(new Query<CashAccount>()
            {
                public boolean query(CashAccount cashAccount)
                {
                    return cashAccount.id() == id;
                }
            });
            return !transactions.isEmpty() ? transactions.get(0) : null;
        }
        public void remove(final long id)
        {
            cashAccountsBox.removeFirst(new Query<CashAccount>()
            {
                public boolean query(CashAccount cashAccount)
                {
                    return cashAccount.id() == id;
                }
            });
        }
        public void add(CashAccount cashAccount)
        {
            cashAccountsBox.add(cashAccount);
        }
        public void clear()
        {
            cashAccountsBox.clear();
        }
    };

    public CashAccounts(String path)
    {
        cashAccountsBox = new Box<>(new ORM<CashAccount>()
        {
            public Map write(CashAccount data)
            {
                Map map = new HashMap();
                map.put("id", data.id());
                map.put("uuid", data.uuid());
                map.put("currencyCodeNumber", data.currencyCodeNumber());
                map.put("title", data.title());
                return map;
            }
            public CashAccount read(Map map)
            {
                return new CashAccountData((Long)map.get("id"),
                        (String)map.get("uuid"),
                        (String)map.get("currencyCodeNumber"),
                        (String)map.get("title"));
            }
        }, path + "/cashAccountsBox");
    }

    public CashAccountsModels.CashAccounts cashAccounts()
    {
        return cashAccounts;
    }
}
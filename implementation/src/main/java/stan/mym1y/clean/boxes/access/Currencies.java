package stan.mym1y.clean.boxes.access;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stan.boxes.Box;
import stan.boxes.ORM;
import stan.boxes.Query;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.data.local.access.CurrenciesAccess;
import stan.mym1y.clean.data.local.models.CurrenciesModels;
import stan.mym1y.clean.modules.currencies.CurrencyData;

public class Currencies
    implements CurrenciesAccess
{
    private final Box<Currency> currenciesBox;
    private final CurrenciesModels.Currencies currencies = new CurrenciesModels.Currencies()
    {
        public List<Currency> getAll()
        {
            return currenciesBox.getAll();
        }
        public Currency get(final String codeNumber)
        {
            List<Currency> transactions = currenciesBox.get(new Query<Currency>()
            {
                public boolean query(Currency currency)
                {
                    return currency.codeNumber().equals(codeNumber);
                }
            });
            return !transactions.isEmpty() ? transactions.get(0) : null;
        }
        public void remove(final String codeNumber)
        {
            currenciesBox.removeFirst(new Query<Currency>()
            {
                public boolean query(Currency currency)
                {
                    return currency.codeNumber().equals(codeNumber);
                }
            });
        }
        public void add(Currency currency)
        {
            currenciesBox.add(currency);
        }
        public void add(List<Currency> currencies)
        {
            currenciesBox.add(currencies);
        }
        public void clear()
        {
            currenciesBox.clear();
        }
    };

    public Currencies(String path)
    {
        currenciesBox = new Box<>(new ORM<Currency>()
        {
            public Map write(Currency currency)
            {
                Map map = new HashMap();
                map.put("codeNumber", currency.codeNumber());
                map.put("codeName", currency.codeName());
                map.put("name", currency.name());
                map.put("minorUnitType", currency.minorUnitType().name());
                return map;
            }
            public Currency read(Map map)
            {
                return new CurrencyData((String)map.get("codeName"),
                        (String)map.get("codeNumber"),
                        (String)map.get("name"),
                        Currency.MinorUnitType.valueOf((String)map.get("codeNumber")));
            }
        }, path + "/currenciesBox");
    }

    public CurrenciesModels.Currencies currencies()
    {
        return currencies;
    }
}

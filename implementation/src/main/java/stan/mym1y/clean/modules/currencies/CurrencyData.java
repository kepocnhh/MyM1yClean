package stan.mym1y.clean.modules.currencies;

import stan.mym1y.clean.cores.currencies.Currency;

public class CurrencyData
    implements Currency
{
    private final String codeName;
    private final String codeNumber;
    private final String name;
    private final MinorUnitType minorUnitType;

    public CurrencyData(String codeName, String codeNumber, String name, MinorUnitType minorUnitType)
    {
        this.codeName = codeName;
        this.codeNumber = codeNumber;
        this.name = name;
        this.minorUnitType = minorUnitType;
    }

    public String codeName()
    {
        return codeName;
    }
    public String codeNumber()
    {
        return codeNumber;
    }
    public String name()
    {
        return name;
    }
    public MinorUnitType minorUnitType()
    {
        return minorUnitType;
    }
}
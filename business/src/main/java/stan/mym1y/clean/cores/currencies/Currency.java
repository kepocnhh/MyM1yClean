package stan.mym1y.clean.cores.currencies;

public interface Currency
{
    String codeName();
    String codeNumber();
    String name();
    MinorUnitType minorUnitType();

    enum MinorUnitType
    {
        NONE,
        TEN,
        HUNDRED,
    }
}
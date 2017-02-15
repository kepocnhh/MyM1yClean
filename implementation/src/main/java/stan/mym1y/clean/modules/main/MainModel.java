package stan.mym1y.clean.modules.main;

import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.dao.ListModel;
import stan.mym1y.clean.dao.Models;

class MainModel
    implements MainContract.Model
{
    private Models.Transactions transactions;

    MainModel(Models.Transactions trnsctns)
    {
        transactions = trnsctns;
    }

    @Override
    public ListModel<TransactionModel> getAll(int sortingType)
    {
        return transactions.getAllWithSort(sortingType);
    }

    @Override
    public int getBalance()
    {
        int balance = 0;
        ListModel<TransactionModel> allTransactions = transactions.getAll();
        for(int i=0; i<allTransactions.size(); i++)
        {
            balance += allTransactions.get(i).getCount();
        }
        return balance;
    }

    @Override
    public void add(TransactionModel transaction)
    {
        transactions.add(transaction);
    }

    @Override
    public void delete(int id)
    {
        transactions.remove(id);
    }
}
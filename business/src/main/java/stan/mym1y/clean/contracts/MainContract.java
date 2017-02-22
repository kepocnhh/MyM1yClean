package stan.mym1y.clean.contracts;

import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.dao.ListModel;
import stan.reactive.Observable;

public interface MainContract
{
    interface Model
    {
        Observable<ListModel<TransactionModel>> getAll();
        Observable<ListModel<TransactionModel>> updateAll();
        Observable<Integer> getBalance();
        void add(TransactionModel transaction);
        void delete(int id);
    }
    interface View
    {
        void error(ErrorsContract.NetworkErrorException exception);
        void error(ErrorsContract.UnauthorizedException exception);
        void error(ErrorsContract.InvalidDataException exception);
        void error(ErrorsContract.ServerErrorException exception);
        void error(ErrorsContract.UnknownErrorException exception);
        void update(ListModel<TransactionModel> transactions);
        void update(int balance);
    }
    interface Presenter
    {
        void update();
        void newTransaction(TransactionViewModel transaction);
        void deleteTransaction(int id);
    }

    interface Behaviour
    {
        void logout();
    }
}
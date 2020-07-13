package pl.mjurek.banking.db;

import pl.mjurek.banking.Account;

public interface CardDAO {
    long create(Account account);

    Account read(String cardNumber);

    void update(Account account);

    void delete(long id);

    int readBalance(long id);

    void increaseMoneyInCardAccount(String cardNumber, int money);
}

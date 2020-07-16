package pl.mjurek.banking.db;

import pl.mjurek.banking.Account;

import java.util.Optional;

public interface CardDAO {
    long create(Account account);

    Optional<Account> read(String cardNumber);

    void update(Account account);

    void delete(long id);

    int readBalance(long id);

    void increaseMoneyInCardAccount(String cardNumber, int money);
}

package pl.mjurek.banking.db;

import pl.mjurek.banking.model.Account;

import java.util.Optional;

public interface CardDAO {
    long create(Account account);

    Optional<Account> read(String cardNumber, String pin);

    void update(Account account);

    void delete(long id);

    boolean isCardExist(String cardNumber);

    int readBalance(long id);

    void increaseMoneyInCardAccount(String cardNumber, int money);
}

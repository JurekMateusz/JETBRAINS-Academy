package pl.mjurek.banking.db;

import pl.mjurek.banking.Account;

public interface CardDAO {
    long create(Account account);

    Account read(String cardNumber);
}

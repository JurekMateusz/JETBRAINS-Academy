package pl.mjurek.banking.service;

import pl.mjurek.banking.db.CardDAO;
import pl.mjurek.banking.db.CardDAOImpl;
import pl.mjurek.banking.model.Account;

import java.util.Optional;

public class AccountService {
    private CardDAO cardDAO;

    public AccountService(String dbName) {
        cardDAO = new CardDAOImpl(dbName);
    }

    public boolean isCardExits(String cardNumber) {
        return cardDAO.isCardExist(cardNumber);
    }

    public int getAccountBalance(long id) {
        return cardDAO.readBalance(id);
    }

    public void put(Account account) {
        cardDAO.create(account);
    }

    public void update(Account account) {
        cardDAO.update(account);
    }

    public void transferMoneyToAccount(String cardNumber, int money) {
        cardDAO.increaseMoneyInCardAccount(cardNumber, money);
    }

    public void delete(long id) {
        cardDAO.delete(id);
    }

    public Optional<Account> getIfCorrectCredential(String cardNumber, String pin) {
        return cardDAO.read(cardNumber, pin);
    }
}

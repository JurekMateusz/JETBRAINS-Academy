package pl.mjurek.banking;

import pl.mjurek.banking.db.CardDAO;
import pl.mjurek.banking.db.CardDAOImpl;

import java.util.Objects;
import java.util.Random;


public class Bank {
    private CardDAO accountDB;

    public Bank(String dbName) {
        accountDB = new CardDAOImpl(dbName);
    }

    public Account createAccount() {
        String cardNumber = "400000"; // bank id

        Random random = new Random(System.currentTimeMillis());
        int accountIdentifier = random.nextInt(1000000000);
        String accountId = String.valueOf(accountIdentifier);
        cardNumber += accountId;
        String checkSum = String.valueOf(LuhnAlgorithm.createCheckSum(cardNumber));
        cardNumber += checkSum;

        long resultNumber = Long.parseLong(cardNumber);
        int pin = random.nextInt(10000);

        String cardNumString = String.valueOf(resultNumber);
        String cardPinString = String.valueOf(pin);

        Account account = new Account(cardNumString, cardPinString);
        return putIfAbsent(account) ? account : createAccount();
    }

    private boolean putIfAbsent(Account account) {
        if (!isCardExist(account.getCardNumber())) {
            accountDB.create(account);
            return true;
        }
        return false;
    }

    public boolean isCardExist(Long cardNumber) {
        return isCardExist(String.valueOf(cardNumber));
    }

    public boolean isCardExist(String cardNumber) {
        return accountDB.read(cardNumber) == null ? false : true;
    }


    public Account getAccountIfCorrectCredential(long cardNumber, int pin) {
        if (!LuhnAlgorithm.isCardNumberCorrect(cardNumber)) return null;
        Account account = accountDB.read(String.valueOf(cardNumber));
        return Objects.equals(account.getPin(), String.valueOf(pin)) ? account : null;
    }

    public int getBalance(long id) {
        return accountDB.readBalance(id);
    }

    public void updateAccount(Account account) {
        accountDB.update(account);
    }

    public void transferMoneyToAccount(String cardNumber, int money) {
        accountDB.increaseMoneyInCardAccount(cardNumber, money);
    }

    public void deleteAccount(long id) {
        accountDB.delete(id);
    }
}

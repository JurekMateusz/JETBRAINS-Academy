package pl.mjurek.banking;

import pl.mjurek.banking.db.CardDAO;
import pl.mjurek.banking.db.CardDAOImpl;
import pl.mjurek.banking.luhn.LuhnAlgorithm;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

public class Bank {
    private CardDAO accountDB;

    public Bank(String dbName) {
        accountDB = new CardDAOImpl(dbName);
    }

    public Account createAccount() {
        String cardNumber = "400000"; // bank id

        String accountIdentifier = generateNumbers(9);
        cardNumber += accountIdentifier;
        String checkSum = String.valueOf(LuhnAlgorithm.createCheckSum(cardNumber));
        cardNumber += checkSum;

        String cardPin = generateNumbers(4);

        Account account = new Account(cardNumber, cardPin);
        return putIfAbsent(account) ? account : createAccount();
    }

    private String generateNumbers(int amount) {
        Random random = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        Arrays.stream(IntStream.generate(() -> random.nextInt(10))
                .limit(amount)
                .toArray())
                .forEach(sb::append);
        return sb.toString();
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
        return accountDB.read(cardNumber).isPresent();
    }

    public Optional<Account> getAccountIfCorrectCredential(long cardNumber, int pin) {
        if (!LuhnAlgorithm.isCardNumberCorrect(cardNumber)) return Optional.empty();
        Optional<Account> accountOpt = accountDB.read(String.valueOf(cardNumber));

        return accountOpt.isPresent() && passwordEqual(accountOpt, pin) ? accountOpt : Optional.empty();
    }

    public int getBalance(long id) {
        return accountDB.readBalance(id);
    }

    private boolean passwordEqual(Optional<Account> accountOpt, int pin) {
        return Objects.equals(accountOpt.get().getPin(), String.valueOf(pin));
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

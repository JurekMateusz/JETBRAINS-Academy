package pl.mjurek.banking;

import pl.mjurek.banking.luhn.LuhnAlgorithm;
import pl.mjurek.banking.model.Account;
import pl.mjurek.banking.service.AccountService;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

public class Bank {
    private AccountService accountService;

    public Bank(String dbName) {
        accountService = new AccountService(dbName);
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
            accountService.put(account);
            return true;
        }
        return false;
    }

    public boolean isCardExist(String cardNumber) {
        return accountService.isCardExits(cardNumber);
    }

    public Optional<Account> getAccountIfCorrectCredential(String cardNumber, String pin) {
        if (!LuhnAlgorithm.isCardNumberCorrect(cardNumber)) return Optional.empty();

        return accountService.getIfCorrectCredential(cardNumber,pin);
    }

    public int getBalance(long id) {
        return accountService.getAccountBalance(id);
    }

    public void updateAccount(Account account) {
        accountService.update(account);
    }

    public void transferMoneyToAccount(String cardNumber, int money) {
        accountService.transferMoneyToAccount(cardNumber, money);
    }

    public void deleteAccount(long id) {
        accountService.delete(id);
    }
}

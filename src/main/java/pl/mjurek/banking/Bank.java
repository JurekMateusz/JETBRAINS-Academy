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
        String cardNumber = "400000";

        Random random = new Random(System.currentTimeMillis());
        int accountIdentifier = random.nextInt(1000000000);
        String accountId = String.valueOf(accountIdentifier);
        cardNumber += accountId;
        cardNumber += createCheckSum(cardNumber);

        long resultNumber = Long.parseLong(cardNumber);
        int pin = random.nextInt(10000);

        String cardNumString = String.valueOf(resultNumber);
        String cardPinString = String.valueOf(pin);

        Account account = new Account(cardNumString, cardPinString);
        return putIfAbsent(account) ? account : createAccount();
    }

    private boolean putIfAbsent(Account account) {
        Account acc = accountDB.read(account.getCardNumber());
        if (acc != null) {
            return false;
        }
        accountDB.create(account);
        return true;
    }

    private String createCheckSum(String cardNumber) {
        int[] card = parseTableToInt(cardNumber);
        int sum = 0;
        for (int i = 0; i < card.length; i++) {
            if (isOdd(i + 1)) {
                card[i] *= 2;
            }
            if (card[i] > 9) {
                card[i] -= 9;
            }
            sum += card[i];
        }
        int checkSum = 0;
        while (((checkSum + sum) % 10) != 0) checkSum++;

        return String.valueOf(checkSum);
    }


    private int[] parseTableToInt(String text) {
        int[] result = new int[text.length()];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(String.valueOf(text.charAt(i)));
        }
        return result;
    }

    private boolean isOdd(int number) {
        return number % 2 == 1;
    }

    public Account getAccountIfCorrectCredential(long cardNumber, int pin) {
        if (!isCardNumberCorrect(cardNumber)) return null;
        Account account = accountDB.read(String.valueOf(cardNumber));
        return Objects.equals(account.getPin(), String.valueOf(pin)) ? account : null;
    }

    private boolean isCardNumberCorrect(long cardNumber) {
        String card = String.valueOf(cardNumber);
        if (card.length() != 16) return false;
        char checkSum = card.charAt(card.length() - 1);
        card = card.substring(0, card.length() - 1);
        char countedCheckSum = createCheckSum(card).charAt(0);

        return Objects.equals(checkSum, countedCheckSum);
    }

}

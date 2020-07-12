package banking;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;


public class Bank {
    private Map<Long, Account> accounts;

    public Bank() {
        accounts = new HashMap<>();
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

        String cardNumString =String.valueOf(resultNumber);
        String cardPinString =String.valueOf(pin);

        Account account = new Account(cardNumString, cardPinString);
        return accounts.putIfAbsent(resultNumber, account) != null ? account : createAccount();
    }

    public Account getAccountIfCorrectCredential(long cardNumber, int pin) {
        Account account = accounts.get(cardNumber);
        if (account == null) {
            return null;
        }
        return Objects.equals(account.getPin(),String.valueOf(pin)) ? account : null;
    }

    private String createCheckSum(String cardNumber) {
        int[] card = parseTableToInt(cardNumber);

        int sum = 0;
        for (int i = 0; i < card.length; i++) {
            if (isOdd(i+1)) {
                card[i] *= 2;
            }
            if (card[i] > 9) {
                card[i] -= 9;
            }
            sum += card[i];
        }
        int checkSum = 0;
        while (((checkSum + sum )% 10) != 0) checkSum++;

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
}

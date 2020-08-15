package pl.mjurek.banking.luhn;

import java.util.stream.Stream;

public class LuhnAlgorithm {
    public static int createCheckSum(String cardNumber) {
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

        return checkSum;
    }


    private static int[] parseTableToInt(String text) {
        return Stream.of(text.split(""))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private static boolean isOdd(int number) {
        return number % 2 == 1;
    }

    public static boolean isCardNumberCorrect(String cardNumber) {
        if (cardNumber.length() != 16) return false;
        char checkSum = cardNumber.charAt(cardNumber.length() - 1);
        cardNumber = cardNumber.substring(0, cardNumber.length() - 1);
        int countedCheckSum = createCheckSum(cardNumber);

        return countedCheckSum == Integer.parseInt(String.valueOf(checkSum));
    }
}

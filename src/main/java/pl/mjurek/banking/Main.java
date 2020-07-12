package banking;

import java.util.Scanner;

public class Main {
    private static Bank bank;
    private static String lineSeparator;

    public static void printHelloScreen() {
        System.out.println("1. Create an account" + lineSeparator +
                "2. Log into account" + lineSeparator +
                "0. Exit");
    }

    public static void printBankOptions() {
        System.out.println("1. Balance" + lineSeparator +
                "2. Log out" + lineSeparator +
                "0. Exit");
    }

    public static void printCardCredential(String cardNumber, String cardPin) {
        System.out.println("Your card number:" + lineSeparator + cardNumber + lineSeparator +
                "Your card PIN:" + lineSeparator + cardPin);
    }

    public static void createAccount() {
        Account account = bank.createAccount();
        printCardCredential(account.getCardNumber(), account.getPin());
    }

    public static boolean logIntoAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your card number:");
        long cardNumber = scanner.nextLong();
        System.out.println("Enter your PIN:");
        int pin = scanner.nextInt();
        Account account = bank.getAccountIfCorrectCredential(cardNumber, pin);
        if (account != null) {
            System.out.println("You have successfully logged in!");
            while (true) {
                printBankOptions();
                int option = scanner.nextInt();
                if (option == 0) return true;
                if (option == 2) return false;
                System.out.println("Balance: " + account.getBalance());
            }
        } else {
            System.out.println("Wrong card number or PIN!");
        }
        return false;
    }

    public static void main(String[] args) {
        bank = new Bank();
        lineSeparator = System.getProperty("line.separator");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            printHelloScreen();
            int choose = scanner.nextInt();
            if (choose == 0) {
                break;
            }
            if (choose == 1) {
                createAccount();
            } else {
                boolean exit = logIntoAccount();

                if (exit) break;
            }
        }
        System.out.println("Bye!");
    }
}

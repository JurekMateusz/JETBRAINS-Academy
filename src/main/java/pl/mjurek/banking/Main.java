package pl.mjurek.banking;

import pl.mjurek.banking.db.CreateDB;

import java.util.Arrays;
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

    public static String findDbName(String[] tab) {
        int index = find(tab, "-fileName");
        if (index == -1 && index + 1 >= tab.length) {
            throw new IllegalArgumentException("Don't have -fileName param");
        }
        return tab[index + 1];
    }

    public static int find(String[] a, String target) {
        return Arrays.asList(a).indexOf(target);
    }

    public static void main(String[] args) {
        lineSeparator = System.getProperty("line.separator");
        String dbName = findDbName(args);
        CreateDB.createNewDatabase(dbName);
        bank = new Bank(dbName);

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

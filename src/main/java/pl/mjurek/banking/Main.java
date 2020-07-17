package pl.mjurek.banking;

import pl.mjurek.banking.db.CreateDB;
import pl.mjurek.banking.luhn.LuhnAlgorithm;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static Bank bank;
    private static String lineSeparator;
    private static Scanner scanner;

    public static void main(String[] args) {
        lineSeparator = System.getProperty("line.separator");
        scanner = new Scanner(System.in);
        String dbName = findDbName(args);
        CreateDB.createNewDatabase(dbName);
        bank = new Bank(dbName);

        boolean exit = false;
        do {
            printHelloScreen();
            int choice = scanner.nextInt();

            exit = consumeInputFirstStage(choice);
        } while (!exit);

        System.out.println("Bye!");
    }

    private static void printHelloScreen() {
        System.out.println("1. Create an account" + lineSeparator +
                "2. Log into account" + lineSeparator +
                "0. Exit");
    }

    private static boolean consumeInputFirstStage(int choose) {
        boolean exit = switch (choose) {
            case 1 -> {
                createAccount();
                yield false;
            }
            case 2 -> logIntoAccount();
            default -> true;
        };
        return exit;
    }

    private static void createAccount() {
        Account account = bank.createAccount();
        printCardCredential(account.getCardNumber(), account.getPin());
    }

    private static boolean logIntoAccount() {
        System.out.println("Enter your card number:");
        long cardNumber = scanner.nextLong();
        System.out.println("Enter your PIN:");
        int pin = scanner.nextInt();

        Optional<Account> accountOpt = bank.getAccountIfCorrectCredential(cardNumber, pin);

        if (accountOpt.isEmpty()) {
            System.out.println("Wrong card number or PIN!");
            return false;
        }

        Account account = accountOpt.get();
        System.out.println("You have successfully logged in!");

       boolean exit = false;
        while (!exit) {
            printBankOptions();
            int choice = scanner.nextInt();
            if (choice == 0) return true; //exit form bank

            exit = consumeInputSecondStage(account, choice);
        }

        return false;
    }

    private static void printBankOptions() {
        System.out.println(
                "1. Balance" + lineSeparator +
                        "2. Add income" + lineSeparator +
                        "3. Do transfer" + lineSeparator +
                        "4. Close account" + lineSeparator +
                        "5. Log out" + lineSeparator +
                        "0. Exit");
    }

    private static boolean consumeInputSecondStage(Account account, int choice) {
        return switch (choice) {
            case 1 -> {
                printAccountBalance(account);
                yield false;
            }
            case 2 -> {
                addIncome(account);
                yield false;
            }
            case 3 -> {
                doTransfer(account);
                yield false;
            }
            case 4 -> {
                bank.deleteAccount(account.getId());
                yield true;
            }
            case 5 -> true; //Logout
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        };
    }

    private static void printAccountBalance(Account account) {
        int balance = bank.getBalance(account.getId());
        System.out.println("Balance: " + balance);
    }

    private static void addIncome(Account account) {
        System.out.println("Enter income:");
        int income = scanner.nextInt();
        account.setBalance(account.getBalance() + income);

        bank.updateAccount(account);
        System.out.println("Income was added!");
    }

    private static void doTransfer(Account account) {
        System.out.println("Transfer" + lineSeparator +
                "Enter card number:");
        long cardNumberToTransfer = scanner.nextLong();

        if (!LuhnAlgorithm.isCardNumberCorrect(cardNumberToTransfer)) {
            System.out.println("Probably you made mistake in the card number. Please try again!");
            return;
        }
        if (isUserTryToTransferToHisAccount(account, cardNumberToTransfer)) {
            System.out.println("You can't transfer money to the same account!");
            return;
        }
        if (!bank.isCardExist(cardNumberToTransfer)) {
            System.out.println("Such a card does not exist.");
            return;
        }
        System.out.println("Enter how much money you want to transfer:");
        int moneyToTransfer = scanner.nextInt();

        if (moneyToTransfer > account.getBalance()) {
            System.out.println("Not enough money!");
        } else {
            account.setBalance(account.getBalance() - moneyToTransfer);
            bank.updateAccount(account);
            String toTransfer = String.valueOf(cardNumberToTransfer);
            bank.transferMoneyToAccount(toTransfer, moneyToTransfer);
            System.out.println("Success!");
        }
    }

    private static void printCardCredential(String cardNumber, String cardPin) {
        System.out.println("Your card number:" + lineSeparator + cardNumber + lineSeparator +
                "Your card PIN:" + lineSeparator + cardPin);
    }

    private static String findDbName(String[] tab) {
        int index = find(tab, "-fileName");
        if (index == -1 && index + 1 >= tab.length) {
            throw new IllegalArgumentException("Don't have -fileName param");
        }
        return tab[index + 1];
    }

    private static int find(String[] a, String target) {
        return Arrays.asList(a).indexOf(target);
    }

    private static boolean isUserTryToTransferToHisAccount(Account account, long cardNumber) {
        return Objects.equals(String.valueOf(cardNumber), account.getCardNumber());
    }
}

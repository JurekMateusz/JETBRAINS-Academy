package banking;

public class Account {
    private String cardNumber;
    private String pin;
    private double balance;

    public Account(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        if (pin.length() < 4) {
            while (pin.length() < 4) pin += "0";
        }
        this.pin = pin;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }
}

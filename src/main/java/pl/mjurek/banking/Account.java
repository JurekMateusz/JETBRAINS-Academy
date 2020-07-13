package pl.mjurek.banking;

public class Account {
    private long id;
    private String cardNumber;
    private String pin;
    private int balance;

    public Account() {
    }

    public Account(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        if (pin.length() < 4) {
            while (pin.length() < 4) pin += "0";
        }
        this.pin = pin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

}

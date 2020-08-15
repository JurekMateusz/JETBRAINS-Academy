package pl.mjurek.banking.db;

import pl.mjurek.banking.model.Account;

import java.sql.*;
import java.util.Optional;

public class CardDAOImpl implements CardDAO {
    private String dbName;
    private final String create = "INSERT INTO card(number,pin) VALUES(?,?)";
    private final String read = "SELECT id,number,pin,balance FROM card WHERE number = ? AND pin = ?";
    private final String update = "UPDATE card SET number = ? ,pin = ? , balance = ? WHERE id = ?";
    private final String delete = "DELETE FROM card WHERE id = ?";
    private final String isExist = "SELECT COUNT() FROM card WHERE NUMBER = ? LIMIT 1";
    private final String readBalance = "SELECT balance FROM card WHERE id=?";
    private final String transferMoney = "UPDATE card SET balance = balance + ?  WHERE number = ?";

    public CardDAOImpl(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public long create(Account account) {
        long idGenerated = 0;
        Connection connection = ConnectionProvider.getConnection(dbName);
        try (PreparedStatement statement = connection.prepareStatement(create, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, account.getCardNumber());
            statement.setString(2, account.getPin());
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                idGenerated = rs.getInt(1);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return idGenerated;
    }

    @Override
    public Optional<Account> read(String cardNumber, String pin) {
        Connection connection = ConnectionProvider.getConnection(dbName);
        try (PreparedStatement statement = connection.prepareStatement(read)) {
            statement.setString(1, cardNumber);
            statement.setString(2, pin);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Account account = new Account();
                account.setId(rs.getInt("id"));
                account.setCardNumber(rs.getString("number"));
                account.setPin(rs.getString("pin"));
                account.setBalance(rs.getInt("balance"));
                return Optional.of(account);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void update(Account account) {
        Connection connection = ConnectionProvider.getConnection(dbName);
        try (PreparedStatement statement = connection.prepareStatement(update)) {
            statement.setString(1, account.getCardNumber());
            statement.setString(2, account.getPin());
            statement.setInt(3, account.getBalance());
            statement.setLong(4, account.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        Connection connection = ConnectionProvider.getConnection(dbName);
        try (PreparedStatement statement = connection.prepareStatement(delete)) {
            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isCardExist(String cardNumber) {
        Connection connection = ConnectionProvider.getConnection(dbName);
        try (PreparedStatement statement = connection.prepareStatement(isExist)) {
            statement.setString(1, cardNumber);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) return rs.getInt(1) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int readBalance(long id) {
        int result = 0;
        Connection connection = ConnectionProvider.getConnection(dbName);
        try (PreparedStatement statement = connection.prepareStatement(readBalance)) {
            statement.setLong(1, id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                result = rs.getInt("balance");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void increaseMoneyInCardAccount(String cardNumber, int money) {
        Connection connection = ConnectionProvider.getConnection(dbName);
        try (PreparedStatement statement = connection.prepareStatement(transferMoney)) {
            statement.setInt(1, money);
            statement.setString(2, cardNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

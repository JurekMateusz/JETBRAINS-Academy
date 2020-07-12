package pl.mjurek.banking.db;

import pl.mjurek.banking.Account;

import java.sql.*;

public class CardDAOImpl implements CardDAO {
    private String dbName;
    final String create = "INSERT INTO card(number,pin) VALUES(?,?)";
    final String read = "SELECT id,number,pin,balance FROM card WHERE number=?";

    public CardDAOImpl(String dbName){
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
    public Account read(String cardNumber) {
        Account account = null;
        Connection connection = ConnectionProvider.getConnection(dbName);
        try (PreparedStatement statement = connection.prepareStatement(read)) {
            statement.setString(1, cardNumber);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                account = new Account();
                account.setId(rs.getInt("id"));
                account.setCardNumber(rs.getString("number"));
                account.setPin(rs.getString("pin"));
                account.setBalance(rs.getInt("balance"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }
}

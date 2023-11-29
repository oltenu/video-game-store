package repository.user;

import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.AbstractRepository;
import repository.security.RightsRolesRepository;

import java.sql.*;

import static database.Constants.Tables.USER;

public class UserRepositoryMySQL extends AbstractRepository<User> implements UserRepository {
    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;

    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        super(connection, User.class);

        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    public boolean save(User user, String salt) {
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.setInt(3, 100);
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            long userId = rs.getLong(1);
            user.setId(userId);

            addUserSalt(user.getId(), salt);
            rightsRolesRepository.addRolesToUser(user, user.getRoles());

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean update(User user){
        String query = "UPDATE `user` " +
                " SET username = ?, password = ?, money = ? " +
                " WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setDouble(3, user.getMoney());
            preparedStatement.setLong(4, user.getId());

            rightsRolesRepository.addRolesToUser(user, user.getRoles());
        }catch (SQLException e){
            e.printStackTrace();

            return false;
        }

        return true;
    }

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {
        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();
        try {
            PreparedStatement preparedStatement;

            String query =
                    "Select * from `" + USER + "` where `username`= ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Long userId = resultSet.getLong("id");
            String salt = getUserSalt(userId);
            String saltedPassword = password + salt;

            String query2 = "Select * from `" + USER + "` where `username`= ? and `password` = ?";
            PreparedStatement second = connection.prepareStatement(query2);
            second.setString(1, username);
            second.setString(2, saltedPassword);

            ResultSet userResultSet = preparedStatement.executeQuery();

            if (userResultSet.next()) {
                User user = new UserBuilder()
                        .setId(userResultSet.getLong("id"))
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setMoney(userResultSet.getDouble("money"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();

                findByUsernameAndPasswordNotification.setResult(user);
            } else {
                findByUsernameAndPasswordNotification.addError("Invalid username or password!");
            }

        } catch (SQLException e) {
            findByUsernameAndPasswordNotification.addError("Something is wrong with the Database!");
        }

        return findByUsernameAndPasswordNotification;
    }

    @Override
    public boolean existsByUsername(String username) {
        try {
            String query =
                    "Select * from `" + USER + "` where `username`= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            ResultSet userResultSet = preparedStatement.executeQuery();
            return userResultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void addUserSalt(Long userId, String salt) {
        String query = "INSERT IGNORE INTO `salt` VALUES (null, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, userId);
            preparedStatement.setString(2, salt);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getUserSalt(Long userId) {
        String query = "SELECT * FROM `salt` WHERE `user_id` = ?";
        String salt = "";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            salt = resultSet.getString("user_salt");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salt;
    }
}

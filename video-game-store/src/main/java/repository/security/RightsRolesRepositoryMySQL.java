package repository.security;

import model.Right;
import model.Role;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.RIGHT;
import static database.Constants.Tables.ROLE;
import static database.Constants.Tables.ROLE_RIGHT;
import static database.Constants.Tables.USER_ROLE;

public class RightsRolesRepositoryMySQL implements RightsRolesRepository {

    private final Connection connection;

    public RightsRolesRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addRole(String role) {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO " + ROLE + " values (null, ?)");
            insertStatement.setString(1, role);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addRight(String right) {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO `" + RIGHT + "` values (null, ?)");
            insertStatement.setString(1, right);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Role findRoleByTitle(String role) {
        PreparedStatement preparedStatement;
        try {
            String query = "Select * from " + ROLE + " where `role`= ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, role);

            ResultSet roleResultSet = preparedStatement.executeQuery();
            roleResultSet.next();
            Long roleId = roleResultSet.getLong("id");
            String roleTitle = roleResultSet.getString("role");

            return new Role(roleId, roleTitle, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Role findRoleById(Long roleId) {
        PreparedStatement preparedStatement;
        try {
            String query = "Select * from " + ROLE + " where `id`= ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, roleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String roleTitle = resultSet.getString("role");

            return new Role(roleId, roleTitle, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Right> findRightsByRole(Long roleId) {
        PreparedStatement preparedStatement;
        List<Right> rights = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + ROLE_RIGHT + " WHERE role_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, roleId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long rightId = resultSet.getLong("id");
                String rightTitle = resultSet.getString("right");

                rights.add(new Right(rightId, rightTitle));
            }

            return rights;
        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public Right findRightByTitle(String right) {
        try {
            String query = "SELECT * FROM `" + RIGHT + "` WHERE `right` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, right);
            ResultSet rightResultSet = preparedStatement.executeQuery();

            rightResultSet.next();
            Long rightId = rightResultSet.getLong("id");
            String rightTitle = rightResultSet.getString("right");
            return new Right(rightId, rightTitle);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addRolesToUser(User user, List<Role> roles) {
        try {
            for (Role role : roles) {
                PreparedStatement insertUserRoleStatement = connection
                        .prepareStatement("INSERT INTO " + USER_ROLE + " values (null, ?, ?)");
                insertUserRoleStatement.setLong(1, user.getId());
                insertUserRoleStatement.setLong(2, role.getId());
                insertUserRoleStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Role> findRolesForUser(Long userId) {
        try {
            List<Role> roles = new ArrayList<>();
            String query = "SELECT * FROM " + USER_ROLE + " WHERE `user_id`= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, userId);

            ResultSet userRoleResultSet = preparedStatement.executeQuery();

            while (userRoleResultSet.next()) {
                long roleId = userRoleResultSet.getLong("role_id");
                roles.add(findRoleById(roleId));
            }

            return roles;
        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public void addRoleRight(Long roleId, Long rightId) {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO " + ROLE_RIGHT + " values (null, ?, ?)");
            insertStatement.setLong(1, roleId);
            insertStatement.setLong(2, rightId);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
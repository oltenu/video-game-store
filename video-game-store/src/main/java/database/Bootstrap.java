package database;

import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static database.Constants.Rights.RIGHTS;
import static database.Constants.Roles.ROLES;
import static database.Constants.Schemas.*;
import static database.Constants.getRolesRights;

// Script - code that automates some steps or processes

public class Bootstrap {

    private static RightsRolesRepository rightsRolesRepository;

    public static void main(String[] args) throws SQLException {
        dropAll();

        bootstrapTables();

        bootstrapUserData();
    }

    private static void dropAll() throws SQLException {
        for (String schema : SCHEMAS) {
            System.out.println("Dropping all tables in schema: " + schema);

            Connection connection;

            if (schema.equals(TEST)) {
                connection = DatabaseSingleton.getConnection(true);
            } else {
                connection = DatabaseSingleton.getConnection(false);
            }

            Statement statement = connection.createStatement();

            String[] dropStatements = {
                    "DROP TABLE `role_right`;",
                    "DROP TABLE `right`;",
                    "DROP TABLE `user_role`;",
                    "DROP TABLE `order`;",
                    "DROP TABLE `video_game`;",
                    "DROP TABLE `role`;",
                    "DROP TABLE `user`;"
            };

            Arrays.stream(dropStatements).forEach(dropStatement -> {
                try {
                    statement.execute(dropStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }

        System.out.println("Done table bootstrap");
    }

    private static void bootstrapTables() throws SQLException {
        SQLTableCreationFactory sqlTableCreationFactory = new SQLTableCreationFactory();

        for (String schema : SCHEMAS) {
            System.out.println("Bootstrapping " + schema + " schema");


            Connection connection;

            if (schema.equals(TEST)) {
                connection = DatabaseSingleton.getConnection(true);
            } else {
                connection = DatabaseSingleton.getConnection(false);
            }

            Statement statement = connection.createStatement();

            for (String table : Constants.Tables.ORDERED_TABLES_FOR_CREATION) {
                String createTableSQL = sqlTableCreationFactory.getCreateSQLForTable(table);
                statement.execute(createTableSQL);
            }
        }

        System.out.println("Done table bootstrap");
    }

    private static void bootstrapUserData() {
        for (String schema : SCHEMAS) {
            System.out.println("Bootstrapping user data for " + schema);

            Connection connection;

            if (schema.equals(TEST)) {
                connection = DatabaseSingleton.getConnection(true);
            } else {
                connection = DatabaseSingleton.getConnection(false);
            }
            rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);

            bootstrapRoles();
            bootstrapRights();
            bootstrapRoleRight();
            bootstrapUserRoles();
        }
    }

    private static void bootstrapRoles() {
        for (String role : ROLES) {
            rightsRolesRepository.addRole(role);
        }
    }

    private static void bootstrapRights() {
        for (String right : RIGHTS) {
            rightsRolesRepository.addRight(right);
        }
    }

    private static void bootstrapRoleRight() {
        Map<String, List<String>> rolesRights = getRolesRights();

        for (String role : rolesRights.keySet()) {
            Long roleId = rightsRolesRepository.findRoleByTitle(role).getId();

            for (String right : rolesRights.get(role)) {
                Long rightId = rightsRolesRepository.findRightByTitle(right).getId();

                rightsRolesRepository.addRoleRight(roleId, rightId);
            }
        }
    }

    private static void bootstrapUserRoles() {

    }
}
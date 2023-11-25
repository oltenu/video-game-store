package database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static database.Constants.Rights.*;
import static database.Constants.Roles.ADMINISTRATOR;
import static database.Constants.Roles.CUSTOMER;
import static database.Constants.Roles.EMPLOYEE;
import static database.Constants.Roles.ROLES;

public class Constants {
    public static Map<String, List<String>> getRolesRights() {
        Map<String, List<String>> rolesRights = new HashMap<>();
        for (String role : ROLES) {
            rolesRights.put(role, new ArrayList<>());
        }
        rolesRights.get(ADMINISTRATOR).addAll(Arrays.asList(RIGHTS));

        rolesRights.get(EMPLOYEE).addAll(Arrays.asList(CREATE_VIDEOGAME, DELETE_VIDEOGAME, UPDATE_VIDEOGAME, BUY_VIDEOGAME));

        rolesRights.get(CUSTOMER).add(BUY_VIDEOGAME);

        return rolesRights;
    }

    public static class Schemas {
        public static final String TEST = "test_store";
        public static final String PRODUCTION = "store";

        public static final String[] SCHEMAS = new String[]{TEST, PRODUCTION};
    }

    public static class Tables {
        public static final String VIDEOGAME = "video_game";
        public static final String USER = "user";
        public static final String ROLE = "role";
        public static final String RIGHT = "right";
        public static final String ROLE_RIGHT = "role_right";
        public static final String USER_ROLE = "user_role";
        public static final String ORDER = "order";

        public static final String[] ORDERED_TABLES_FOR_CREATION = new String[]{USER, ROLE, RIGHT, ROLE_RIGHT, USER_ROLE,
                VIDEOGAME, ORDER};
    }

    public static class Roles {
        public static final String ADMINISTRATOR = "administrator";
        public static final String EMPLOYEE = "employee";
        public static final String CUSTOMER = "customer";

        public static final String[] ROLES = new String[]{ADMINISTRATOR, EMPLOYEE, CUSTOMER};
    }

    public static class Rights {
        public static final String CREATE_USER = "create_user";
        public static final String DELETE_USER = "delete_user";
        public static final String UPDATE_USER = "update_user";

        public static final String CREATE_VIDEOGAME = "create_video_game";
        public static final String DELETE_VIDEOGAME = "delete_video_game";
        public static final String UPDATE_VIDEOGAME = "update_video_game";

        public static final String BUY_VIDEOGAME = "buy_video_game";

        public static final String[] RIGHTS = new String[]{CREATE_USER, DELETE_USER, UPDATE_USER, CREATE_VIDEOGAME,
                DELETE_VIDEOGAME, UPDATE_VIDEOGAME, BUY_VIDEOGAME};
    }
}

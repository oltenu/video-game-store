package repository;


import database.DatabaseSingleton;
import model.annotations.DateType;
import model.annotations.Id;
import model.annotations.Ignore;
import model.annotations.MapToDatabase;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AbstractRepository<T> {
    private final Class<T> type;
    private final Connection connection;

    public AbstractRepository(Class<T> classType) {
        this.connection = DatabaseSingleton.getConnection(true);
        this.type = classType;
    }

    public AbstractRepository(Connection connection, Class<T> classType) {
        this.connection = connection;
        this.type = classType;
    }

    public List<T> findAll() {
        String table = type.getSimpleName();

        if (type.isAnnotationPresent(MapToDatabase.class)) {
            table = type.getAnnotation(MapToDatabase.class).columnName();
        }

        Statement statement;
        ResultSet resultSet;
        String query = "SELECT " +
                " * " +
                " FROM " +
                table;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            return createObjects(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Optional<T> findById(Long id) {
        String table = type.getSimpleName();

        if (type.isAnnotationPresent(MapToDatabase.class)) {
            table = type.getAnnotation(MapToDatabase.class).columnName();
        }

        PreparedStatement statement;
        ResultSet resultSet;
        String query = "SELECT " +
                " * " +
                " FROM " +
                table +
                " WHERE id = ?";
        try {
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            List<T> list = createObjects(resultSet);
            if (list.isEmpty()) {
                return Optional.empty();
            }

            return Optional.ofNullable(list.get(0));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public boolean save(T t) {
        String table = type.getSimpleName();

        if (type.isAnnotationPresent(MapToDatabase.class)) {
            table = type.getAnnotation(MapToDatabase.class).columnName();
        }

        try {
            StringBuilder names = new StringBuilder();
            StringBuilder questionMarks = new StringBuilder();
            List<Object> values = new ArrayList<>();
            Field[] fields = type.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                if (!field.isAnnotationPresent(Id.class)) {
                    if (field.isAnnotationPresent(MapToDatabase.class)) {
                        names.append(field.getAnnotation(MapToDatabase.class).columnName()).append(",");
                    } else {
                        names.append(field.getName()).append(",");
                    }

                    questionMarks.append("?,");
                    values.add(field.get(t));
                }
            }
            names.deleteCharAt(names.length() - 1);
            questionMarks.deleteCharAt(questionMarks.length() - 1);

            String query = "INSERT INTO "
                    + table
                    + "("
                    + names
                    + ")"
                    + " VALUES "
                    + "("
                    + questionMarks
                    + ")";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            int index = 1;
            for (Object value : values) {
                preparedStatement.setObject(index++, value);
            }

            return preparedStatement.executeUpdate() == 1;

        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void deleteById(Long id) {
        PreparedStatement preparedStatement;
        String table = type.getSimpleName();

        if (type.isAnnotationPresent(MapToDatabase.class)) {
            table = type.getAnnotation(MapToDatabase.class).columnName();
        }

        String query = "DELETE FROM " +
                table +
                " WHERE id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean update(T t) {
        try {
            StringBuilder updateQuery = new StringBuilder();
            String table = type.getSimpleName();
            List<Object> values = new ArrayList<>();
            Field[] fields = type.getDeclaredFields();
            Long id = null;

            if (type.isAnnotationPresent(MapToDatabase.class)) {
                table = type.getAnnotation(MapToDatabase.class).columnName();
            }

            for (Field field : fields) {
                field.setAccessible(true);

                if (field.isAnnotationPresent(Id.class)) {
                    id = (Long) field.get(t);
                }

                if (field.isAnnotationPresent(MapToDatabase.class)) {
                    updateQuery.append(field.getAnnotation(MapToDatabase.class).columnName()).append(" = ?,");
                } else {
                    updateQuery.append(field.getName()).append(" = ?,");
                }
                values.add(field.get(t));
            }

            updateQuery.deleteCharAt(updateQuery.length() - 1);

            String query = "UPDATE " +
                    table +
                    " SET " +
                    updateQuery +
                    " WHERE id = " +
                    id;

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            int index = 1;
            for (Object value : values) {
                preparedStatement.setObject(index++, value);
            }

            return preparedStatement.executeUpdate() == 1;

        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void removeAll() {
        Statement statement;
        String table = type.getSimpleName();

        if (type.isAnnotationPresent(MapToDatabase.class)) {
            table = type.getAnnotation(MapToDatabase.class).columnName();
        }

        String query = "TRUNCATE TABLE "
                + table;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<T> createObjects(ResultSet resultSet) {
        List<T> objects = new ArrayList<>();
        Constructor<T> constructor = null;

        try {
            constructor = type.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            while (resultSet.next()) {
                Objects.requireNonNull(constructor).setAccessible(true);
                T instance = constructor.newInstance();

                for (Field field : type.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Ignore.class)) {
                        continue;
                    }
                    String fieldName;
                    if (field.isAnnotationPresent(MapToDatabase.class)) {
                        fieldName = field.getAnnotation(MapToDatabase.class).columnName();
                    } else {
                        fieldName = field.getName();
                    }
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method setMethod = propertyDescriptor.getWriteMethod();
                    if (field.isAnnotationPresent(DateType.class)) {
                        setMethod.invoke(instance, ((LocalDateTime) value).toLocalDate());
                    } else {
                        setMethod.invoke(instance, value);
                    }
                }

                objects.add(instance);
            }
        } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 IntrospectionException e) {
            e.printStackTrace();
        }

        return objects;
    }
}

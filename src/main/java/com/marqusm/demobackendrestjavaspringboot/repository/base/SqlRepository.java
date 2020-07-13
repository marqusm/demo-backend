package com.marqusm.demobackendrestjavaspringboot.repository.base;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.marqusm.demobackendrestjavaspringboot.annotation.Id;
import com.marqusm.demobackendrestjavaspringboot.annotation.Table;
import com.marqusm.demobackendrestjavaspringboot.configuration.AppConfig;
import com.marqusm.demobackendrestjavaspringboot.exception.DuplicateException;
import com.marqusm.demobackendrestjavaspringboot.repository.connection.DbConnectionManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/** @author : Marko Mišković */
@Slf4j
@Repository
public abstract class SqlRepository<T> {

  private static final String CREATE_TEMPLATE = "INSERT INTO %s(%s) VALUES (%s)";
  private static final String READ_TEMPLATE = "SELECT * FROM %s";
  private static final String READ_WHERE_TEMPLATE = "SELECT * FROM %s WHERE %s";

  static {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (Exception e) {
      log.error("Database connection error.", e);
    }
  }

  protected final String tableName;
  private final Class<T> recordClass;
  private final Field idField;
  private final List<Field> fields;
  private final List<Field> nonIdFields;
  @Autowired private AppConfig appConfig;
  @Autowired private DbConnectionManager connManager;

  @SuppressWarnings("unchecked")
  public SqlRepository() {
    Preconditions.checkArgument(getClass().getGenericSuperclass() instanceof ParameterizedType);
    var genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
    Preconditions.checkArgument(genericSuperclass.getActualTypeArguments()[0] instanceof Class);
    recordClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];

    tableName = recordClass.getAnnotation(Table.class).value();
    fields =
        Arrays.stream(recordClass.getDeclaredFields())
            .peek(o -> o.setAccessible(true))
            .collect(Collectors.toUnmodifiableList());
    nonIdFields =
        fields.stream()
            .filter(o -> o.getAnnotation(Id.class) == null)
            .collect(Collectors.toUnmodifiableList());
    idField =
        fields.stream().filter(o -> o.getAnnotation(Id.class) != null).findFirst().orElse(null);
  }

  @SneakyThrows
  public Connection getNonAutoTxConnection() {
    Class.forName("org.postgresql.Driver");
    var connection =
        DriverManager.getConnection(
            appConfig.getDbConnectionString(),
            appConfig.getDatabaseUsername(),
            appConfig.getDatabasePassword());
    connection.setAutoCommit(false);
    return connection;
  }

  public T create(T object) {
    return createInBatch(Collections.singletonList(object)).get(0);
  }

  @SneakyThrows
  public List<T> createInBatch(List<T> objects) {
    var fields =
        nonIdFields.stream()
            .map(field -> camelCaseToSnakeCase(field.getName()))
            .collect(Collectors.joining(","));
    var values = nonIdFields.stream().map(o -> "?").collect(Collectors.joining(","));
    var sqlTemplate = String.format(CREATE_TEMPLATE, tableName, fields, values);
    for (T object : objects) {
      var index = 1;
      try (var statement =
          connManager
              .getConnection()
              .prepareStatement(sqlTemplate, Statement.RETURN_GENERATED_KEYS)) {
        for (Field field : nonIdFields) {
          setParam(statement, index++, field, object);
        }
        try {
          statement.executeUpdate();
        } catch (PSQLException e) {
          if (e.getMessage().contains("duplicate key value violates unique constraint")) {
            throw new DuplicateException("Username already exists");
          } else {
            throw e;
          }
        }
        if (idField != null) {
          statement.getGeneratedKeys().next();
          UUID id = UUID.fromString(statement.getGeneratedKeys().getString(1));
          idField.set(object, id);
        }
      }
    }
    return objects;
  }

  @SneakyThrows
  public List<T> findAll(Connection conn) {
    try (var statement = conn.createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(READ_TEMPLATE, tableName));
      return getAllFromResultSet(resultSet);
    }
  }

  @SneakyThrows
  public List<T> findByIds(Collection<UUID> ids) {
    try (var statement = connManager.getConnection().createStatement()) {
      var whereClause =
          String.format(
              "id IN (%s)",
              ids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(" AND ")));
      ResultSet resultSet =
          statement.executeQuery(String.format(READ_WHERE_TEMPLATE, tableName, whereClause));
      return getAllFromResultSet(resultSet);
    }
  }

  public Optional<T> findOneByExample(T example) {
    var results = findByExample(example);
    if (results.size() > 1) {
      throw new IllegalStateException("There are more than one results: " + results.size());
    } else if (results.size() == 1) {
      return Optional.of(results.get(0));
    } else {
      return Optional.empty();
    }
  }

  @SneakyThrows
  public List<T> findByExample(T example) {
    var whereClauseParts = new LinkedList<String>();
    for (Field field : fields) {
      Object value = field.get(example);
      if (value != null) {
        String adaptedValue;
        if (value instanceof String
            || value instanceof UUID
            || value instanceof Boolean
            || value instanceof Enum) {
          adaptedValue = "'" + value.toString() + "'";
        } else {
          adaptedValue = value.toString();
        }
        whereClauseParts.add(camelCaseToSnakeCase(field.getName()) + "=" + adaptedValue);
      }
    }
    var template = whereClauseParts.isEmpty() ? READ_TEMPLATE : READ_WHERE_TEMPLATE;
    var whereClause = String.join(" AND ", whereClauseParts);
    try (var statement = connManager.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(template, tableName, whereClause));
      return getAllFromResultSet(resultSet);
    }
  }

  protected Optional<T> getOneFromResultSet(ResultSet resultSet) throws SQLException {
    resultSet.next();
    return Optional.ofNullable(getFromResultSet(resultSet));
  }

  protected List<T> getAllFromResultSet(ResultSet resultSet) throws SQLException {
    var results = new LinkedList<T>();
    while (resultSet.next()) {
      results.add(getFromResultSet(resultSet));
    }
    return results;
  }

  protected String joinIds(Collection<UUID> ids) {
    return ids.stream().map(id -> String.format("'%s'", id)).collect(Collectors.joining(","));
  }

  @SneakyThrows
  @SuppressWarnings("unchecked")
  private T getFromResultSet(ResultSet resultSet) {
    var method =
        recordClass.getDeclaredMethod(
            "of",
            fields.stream()
                .map(Field::getType)
                .collect(Collectors.toList())
                .toArray(new Class[] {}));
    var result =
        method.invoke(
            null,
            fields.stream()
                .map(
                    field ->
                        getObjectFromResultSet(
                            resultSet,
                            camelCaseToSnakeCase(field.getName()),
                            (Class) field.getType()))
                .collect(Collectors.toList())
                .toArray(new Object[] {}));
    if (recordClass.isInstance(result)) {
      return recordClass.cast(result);
    } else {
      throw new IllegalStateException("Class of wrong type: " + result.getClass().getName());
    }
  }

  private void setParam(PreparedStatement statement, int index, Field field, Object obj)
      throws IllegalAccessException, SQLException {
    var value = field.get(obj);
    if (value instanceof UUID) {
      statement.setObject(index, value);
    } else if (value instanceof String) {
      statement.setString(index, (String) value);
    } else if (value instanceof Long) {
      statement.setLong(index, (Long) value);
    } else if (value instanceof byte[]) {
      statement.setBytes(index, (byte[]) value);
    } else if (value instanceof Enum) {
      statement.setString(index, value.toString());
    } else if (value instanceof Boolean) {
      statement.setBoolean(index, (Boolean) value);
    } else {
      throw new IllegalArgumentException("Object type not supported: " + field.getType());
    }
  }

  private String camelCaseToSnakeCase(String input) {
    return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, input);
  }

  @SneakyThrows
  @SuppressWarnings("unchecked")
  private Object getObjectFromResultSet(ResultSet resultSet, String key, Class<T> type) {
    var obj = resultSet.getObject(key);
    if (type.isEnum()) {
      return Enum.valueOf((Class) type, obj.toString());
    } else {
      return obj;
    }
  }
}

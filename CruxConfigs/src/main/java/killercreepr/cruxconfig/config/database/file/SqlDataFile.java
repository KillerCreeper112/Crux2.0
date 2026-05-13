package killercreepr.cruxconfig.config.database.file;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParser;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.regex.Pattern;

public class SqlDataFile implements DataFile {
  public static boolean checkValidTableID(String tableID) {
    return tableID.matches("[a-zA-Z0-9_]+");
  }

  public static Builder builder() {
    return new Builder();
  }

  protected final String tableID;
  protected final Connection connection;
  protected final Gson gson;
  protected final FileRegistry registry;
  protected String pathPrefix = null;
  protected char pathSeparator = '.';

  public SqlDataFile(String tableID, Connection connection, Gson gson, FileRegistry registry) {
    Preconditions.checkArgument(checkValidTableID(tableID), "Invalid table ID! " + tableID);
    this.tableID = tableID;
    this.connection = connection;
    this.gson = gson;
    this.registry = registry;
    createTableIfNeeded();
  }

  public String fullPath(String path){
    return pathPrefix == null ? path : (pathPrefix + path);
  }

  public String getPathPrefix() {
    return pathPrefix;
  }

  public void setPathPrefix(String pathPrefix) {
    this.pathPrefix = pathPrefix;
  }

  @Override
  public @Nullable FileElement getRoot() {
    try (PreparedStatement stmt = connection.prepareStatement("""
        SELECT * FROM %s
        """.formatted(tableID))) {

      ResultSet rs = stmt.executeQuery();

      FileObject root = new FileObject();

      while (rs.next()) {
        String path = rs.getString("path");
        String value = rs.getString("value");

        String[] parts = path.split(Pattern.quote(String.valueOf(pathSeparator)));

        FileObject current = root;

        for (int i = 0; i < parts.length; i++) {
          String key = parts[i];
          boolean isLeaf = (i == parts.length - 1);

          if (isLeaf) {
            JsonElement json = JsonParser.parseString(value);
            current.add(key, FileElement.fromJson(json));
          } else {
            FileElement child = current.get(key);

            FileObject next;

            if (child instanceof FileObject fo) {
              next = fo;
            } else {
              next = new FileObject();
              current.add(key, next);
            }

            current = next;
          }
        }
      }

      return root;

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public @Nullable FileElement getElement(@NotNull String path) {
    path = fullPath(path);
    try (PreparedStatement stmt = connection.prepareStatement("""
      SELECT value FROM %s WHERE path = ?
      """.formatted(tableID))) {

      stmt.setString(1, path);

      ResultSet rs = stmt.executeQuery();

      if (!rs.next()) return null;

      var json = JsonParser.parseString(rs.getString("value"));
      if (json == null) return null;
      return FileElement.fromJson(json);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void createTableIfNeeded() {
    try (PreparedStatement stmt = connection.prepareStatement("""
      CREATE TABLE IF NOT EXISTS %s (
          path VARCHAR(255) PRIMARY KEY,
          value MEDIUMTEXT
      )
      """.formatted(tableID))) {
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void remove(String path){
    path = fullPath(path);
    try (PreparedStatement stmt = connection.prepareStatement("""
      DELETE FROM %s WHERE path = ?
      """.formatted(tableID))) {

      stmt.setString(1, path);
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void serialize(@NotNull String path, @Nullable Object value) {
    if(value == null){
      remove(path);
      return;
    }
    path = fullPath(path);
    var serialized = gson.toJson(registry.serializeToFile(value).toJson());

    try (PreparedStatement stmt = connection.prepareStatement("""
      INSERT INTO %s(path, value)
      VALUES (?, ?)
      ON DUPLICATE KEY UPDATE
      value = VALUES(value)
      """.formatted(tableID))) {

      stmt.setString(1, path);
      stmt.setString(2, serialized);
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T> T deserialize(@NotNull String path, @NotNull Type type) {
    path = fullPath(path);
    try (PreparedStatement stmt = connection.prepareStatement("""
      SELECT value FROM %s WHERE path = ?
      """.formatted(tableID))) {

      stmt.setString(1, path);

      ResultSet rs = stmt.executeQuery();

      if (!rs.next()) return null;

      var json = JsonParser.parseString(rs.getString("value"));
      if (json == null || JsonNull.INSTANCE.equals(json)) return null;
      return registry.deserializeFromFile(type, FileElement.fromJson(json));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T> T deserializeOrDefault(@NotNull String path, @NotNull Type type, @org.jspecify.annotations.Nullable T fallback) {
    try {
      T value = deserialize(path, type);
      if (value == null) return fallback;
      return value;
    } catch (RuntimeException e) {
      return fallback;
    }
  }

  @Override
  public @NotNull FileRegistry fileRegistry() {
    return registry;
  }

  @Override
  public char getPathSeparator() {
    return pathSeparator;
  }

  @Override
  public void setPathSeparator(char separator) {
    this.pathSeparator = separator;
  }

  @Override
  public void close() {
    closeConnection();
  }

  public boolean closeConnection(){
    try {
      connection.close();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean save() {
    return closeConnection();
  }

  @Override
  public boolean delete() {
    try (PreparedStatement stmt = connection.prepareStatement("""
      DROP TABLE IF EXISTS %s
      """.formatted(tableID))) {

      stmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  @Override
  public @NotNull File file() {
    throw new UnsupportedOperationException("SQL database contains no file!");
  }

  public static class Builder {
    public String url;
    public String user;
    public String password;

    protected String tableID;
    public Gson gson = new Gson();
    public FileRegistry registry = CfgRegistries.JSON;

    public String pathPrefix;

    public Builder pathPrefix(String pathPrefix) {
      this.pathPrefix = pathPrefix;
      return this;
    }

    public Builder url(String url) {
      this.url = url;
      return this;
    }

    public Builder user(String user) {
      this.user = user;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder tableID(String tableID) {
      Preconditions.checkArgument(checkValidTableID(tableID), "Table ID not valid! " + tableID);
      this.tableID = tableID;
      return this;
    }

    public String tableID() {
      return this.tableID;
    }

    public Builder gson(Gson gson) {
      this.gson = gson;
      return this;
    }

    public Builder registry(FileRegistry registry) {
      this.registry = registry;
      return this;
    }

    public SqlDataFile build() {
      Preconditions.checkArgument(url != null, "URL not set!");
      Preconditions.checkArgument(user != null, "User not set!");
      Preconditions.checkArgument(password != null, "Password not set!");
      Preconditions.checkArgument(tableID != null, "Table ID not set!");

      try {
        var file = new SqlDataFile(
          tableID,
          DriverManager.getConnection(url, user, password),
          gson, registry
        );
        file.setPathPrefix(pathPrefix);
        return file;
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }
}

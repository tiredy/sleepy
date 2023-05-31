package me.tiredy.sleepy.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.tiredy.sleepy.callback.ResultCallback;
import me.tiredy.sleepy.callback.VoidCallback;
import me.tiredy.sleepy.data.schema.SchemaBasic;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.UUID;

@SuppressWarnings("unused")
public class Database<T extends SchemaBasic> {
    private final Dao<T, Integer> dataDao;
    private final JavaPlugin instance;
    public Database(JavaPlugin plugin, Class<T> schemaClass) {
        this.instance = plugin;
        String databasePath = "jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath().concat("/sleepy/db.sqlite");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(databasePath);

        try (HikariDataSource dataSource = new HikariDataSource(config);
             ConnectionSource connectionSource = new JdbcConnectionSource(dataSource.getJdbcUrl())) {

            TableUtils.createTableIfNotExists(connectionSource, schemaClass);
            this.dataDao = DaoManager.createDao(connectionSource, schemaClass);

        } catch (SQLException e) {
            throw new RuntimeException("Exception initializing database: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Exception closing the connection source: " + e.getMessage(), e);
        }
    }

    private Dao<T, Integer> getDataDao() {
        return dataDao;
    }

    public T get(UUID uuid) {
        try {
            Dao<T, Integer> dataDao = getDataDao();
            QueryBuilder<T, Integer> queryBuilder = dataDao.queryBuilder();
            PreparedQuery<T> preparedQuery = queryBuilder.where().eq(SchemaBasic.UUID_FIELD, uuid).prepare();
            return dataDao.queryForFirst(preparedQuery);
        } catch (SQLException e) {
            instance.getLogger().severe("Exception retrieving data from database: " + e.getMessage());
            return null;
        }
    }
    public void getAsync(UUID uuid, ResultCallback<T> callback) {
        try {
            Dao<T, Integer> dataDao = getDataDao();
            QueryBuilder<T, Integer> queryBuilder = dataDao.queryBuilder();
            PreparedQuery<T> preparedQuery = queryBuilder.where().eq(SchemaBasic.UUID_FIELD, uuid).prepare();
            callback.onSuccess(dataDao.queryForFirst(preparedQuery));
        } catch (SQLException e) {
            callback.onFailure(e);
        }
    }

    public boolean contains(UUID uuid) {
        try {
            Dao<T, Integer> dataDao = getDataDao();
            QueryBuilder<T, Integer> queryBuilder = dataDao.queryBuilder();
            queryBuilder.setCountOf(true);
            queryBuilder.where().eq(SchemaBasic.UUID_FIELD, uuid);
            long count = dataDao.countOf(queryBuilder.prepare());
            return count > 0;
        } catch (SQLException e) {
            instance.getLogger().severe("Exception checking data presence in database: " + e.getMessage());
            return false;
        }
    }
    public void containsAsync(UUID uuid, ResultCallback<Boolean> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(instance,() -> {
            try {
                Dao<T, Integer> dataDao = getDataDao();
                QueryBuilder<T, Integer> queryBuilder = dataDao.queryBuilder();
                queryBuilder.setCountOf(true);
                queryBuilder.where().eq(SchemaBasic.UUID_FIELD, uuid);
                long count = dataDao.countOf(queryBuilder.prepare());
                callback.onSuccess(count > 0);
            } catch (SQLException e) {
                callback.onFailure(e);
            }
        });
    }

    public void set(T data) {
        try {
            getDataDao().createOrUpdate(data);
        } catch (SQLException e) {
            instance.getLogger().severe("Exception inserting data into the database: " + e.getMessage());
        }
    }
    public void setAsync(T data, VoidCallback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(instance,() -> {
            try {
                getDataDao().createOrUpdate(data);
                callback.onSuccess();
            } catch (SQLException e) {
                callback.onFailure(e);
            }
        });
    }
}

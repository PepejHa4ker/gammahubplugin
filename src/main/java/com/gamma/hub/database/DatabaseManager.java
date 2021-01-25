package com.gamma.hub.database;

import com.gamma.hub.GammaHubPlugin;
import com.pepej.papi.config.ConfigFactory;
import com.pepej.papi.config.configurate.ConfigurationNode;
import com.pepej.papi.terminable.Terminable;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager implements Terminable {
    private final HikariDataSource hikari;

    public DatabaseManager() {
        final ConfigurationNode databaseConfig = ConfigFactory.gson().load(new File(GammaHubPlugin.getInstance().getDataFolder(), "config.json")).getNode("MySQL");
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(databaseConfig.getNode("Maximum-Pool-Size").getInt());
        config.setMinimumIdle(databaseConfig.getNode("Minimum-Pool-Idle").getInt());
        config.setMaxLifetime(databaseConfig.getNode("Maximum-Pool-Lifetime").getInt());
        config.setConnectionTimeout(databaseConfig.getNode("Maximum-Pool-Timeout").getInt());
        config.setPoolName("hubMySQL Connection Pool");
        config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        config.addDataSourceProperty("serverName", databaseConfig.getNode("Address").getString());
        config.addDataSourceProperty("port", databaseConfig.getNode("Port").getInt());
        config.addDataSourceProperty("databaseName", databaseConfig.getNode("Database").getString());
        config.addDataSourceProperty("user", databaseConfig.getNode("Username").getString());
        config.addDataSourceProperty("password", databaseConfig.getNode("Password").getString());
        config.addDataSourceProperty("useSSL", databaseConfig.getNode("Use-SSL").getBoolean());
        this.hikari = new HikariDataSource(config);

    }

    @SneakyThrows
    public final boolean isConnected() {
        return hikari != null && hikari.isRunning() && !hikari.getConnection().isClosed();
    }

    public HikariDataSource getHikari() {
        return hikari;
    }

    public Connection getConnection() throws SQLException {
        return hikari.getConnection();
    }

    @Override
    public void close() throws Exception {
        hikari.close();
    }
}

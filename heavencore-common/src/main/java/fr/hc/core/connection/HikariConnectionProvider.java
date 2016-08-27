package fr.hc.core.connection;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariConnectionProvider implements ConnectionProvider
{
	private final DataSource dataSource;

	HikariConnectionProvider(String database, String username, String password)
	{
		final HikariConfig config = new HikariConfig();
		config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
		config.addDataSourceProperty("serverName", "localhost");
		config.addDataSourceProperty("port", "3306");
		config.addDataSourceProperty("zeroDateTimeBehavior", "convertToNull");
		config.addDataSourceProperty("autoReconnect", "true");

		// Performance settings
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "500");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

		config.addDataSourceProperty("databaseName", database);
		config.setUsername(username);
		config.setPassword(password);
		config.setMaximumPoolSize(2); // 1 for bukkit main thread, 1 for async tasks

		dataSource = new HikariDataSource(config);
	}

	@Override
	public Connection getConnection() throws SQLException
	{
		return dataSource.getConnection();
	}
}
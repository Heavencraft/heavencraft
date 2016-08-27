package fr.hc.core.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider
{
	Connection getConnection() throws SQLException;
}
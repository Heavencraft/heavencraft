package fr.hc.core.connection;

public interface ConnectionProviderFactory
{
	ConnectionProvider newConnectionProvider(String database, String username, String password);
}
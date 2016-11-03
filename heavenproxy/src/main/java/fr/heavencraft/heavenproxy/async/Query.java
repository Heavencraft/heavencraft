package fr.heavencraft.heavenproxy.async;

import java.sql.SQLException;

import fr.heavencraft.heavenproxy.exceptions.HeavenException;

public interface Query
{
    void executeQuery() throws HeavenException, SQLException;

    void onSuccess();

    void onHeavenException(HeavenException ex);

    void onSQLException(SQLException ex);
}
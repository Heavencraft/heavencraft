package fr.heavencraft.heavenproxy.async;

import java.sql.SQLException;

import fr.hc.core.exceptions.HeavenException;

public interface Query
{
    void executeQuery() throws HeavenException, SQLException;

    void onSuccess();

    void onHeavenException(HeavenException ex);

    void onSQLException(SQLException ex);
}
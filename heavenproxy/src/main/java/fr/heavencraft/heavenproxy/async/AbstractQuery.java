package fr.heavencraft.heavenproxy.async;

import java.sql.SQLException;

import fr.heavencraft.heavenproxy.exceptions.HeavenException;

public abstract class AbstractQuery implements Query
{
    @Override
    public void onSuccess()
    {
    }

    @Override
    public void onHeavenException(HeavenException ex)
    {
    }

    @Override
    public void onSQLException(SQLException ex)
    {
    }
}
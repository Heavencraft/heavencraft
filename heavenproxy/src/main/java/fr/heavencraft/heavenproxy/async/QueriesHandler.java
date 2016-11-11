package fr.heavencraft.heavenproxy.async;

import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import fr.heavencraft.heavenproxy.HeavenProxy;
import fr.hc.core.exceptions.HeavenException;
import net.md_5.bungee.api.ProxyServer;

public class QueriesHandler implements Runnable
{
    private static Queue<Query> queries = new ConcurrentLinkedQueue<Query>();

    public QueriesHandler()
    {
        ProxyServer.getInstance().getScheduler().schedule(HeavenProxy.getInstance(), this, 1, 1, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run()
    {
        if (queries.isEmpty())
            return;

        Query tmp;

        while ((tmp = queries.poll()) != null)
        {
            final Query query = tmp; // Gruge final

            try
            {
                query.executeQuery();
                query.onSuccess();
            }
            catch (final HeavenException ex)
            {
                query.onHeavenException(ex);
            }
            catch (final SQLException ex)
            {
                ex.printStackTrace();

                query.onSQLException(ex);
            }
        }
    }

    public static void addQuery(Query query)
    {
        queries.add(query);
    }
}
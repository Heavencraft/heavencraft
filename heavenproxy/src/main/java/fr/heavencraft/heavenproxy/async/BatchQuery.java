package fr.heavencraft.heavenproxy.async;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import fr.hc.core.exceptions.HeavenException;

public class BatchQuery extends AbstractQuery
{
    private final List<Query> queries;

    public BatchQuery(Query... queries)
    {
        this.queries = Arrays.asList(queries);
    }

    public BatchQuery(List<Query> queries)
    {
        this.queries = queries;
    }

    @Override
    public void executeQuery() throws HeavenException, SQLException
    {
        for (final Query query : queries)
            query.executeQuery();
    }
}
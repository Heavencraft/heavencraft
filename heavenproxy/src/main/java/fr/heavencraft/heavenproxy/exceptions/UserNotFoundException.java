package fr.heavencraft.heavenproxy.exceptions;

import java.util.UUID;

public class UserNotFoundException extends HeavenException
{
    private static final long serialVersionUID = 1L;

    public UserNotFoundException(UUID uniqueId)
    {
        super("Le joueur {%1$s} n'existe pas.", uniqueId);
    }

    public UserNotFoundException(String name)
    {
        super("Le joueur {%1$s} n'existe pas.", name);
    }
}
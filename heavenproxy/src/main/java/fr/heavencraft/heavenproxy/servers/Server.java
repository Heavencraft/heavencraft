package fr.heavencraft.heavenproxy.servers;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;

public enum Server
{
    Nexus("nexus", "Nex", "Nexus"),
    SemiRP("semirp", "SRP", "Monde Semi-RP"),
    Origines("origines", "Ori", "Monde Origines"),
    Creatif("creative", "Créa", "Monde Créatif"),
    Build("build", "Créa", "Build"),
    Fun("fun", "Fun", "Monde Fun"),
    Infected("infected", "Inf", "Infected"),
    Musee("musee", "Mus", "Musée"),
    MarioKart("mariokart", "MK", "Mario Kart"),
    TNTRun("tntrun", "TNT", "TNT Run"),
    UltraHardcore("ultrahard", "UH", "Monde Ultra-Hardcore"),
    Paintball("paintball", "PB", "Paintball"),
    HungerGames("hungergames", "HG", "Hunger Games"),
    Skyblock("skyblock", "Sky", "Skyblock"),
    Robinson("robinson", "1.9", "Robinson"),
    Event("event", "Evt", "Evènement"),
    UnknownServer("", "???", "Monde ???");

    private final String name;
    private final String prefix;
    private final Title title;

    Server(String name, String prefix, String title)
    {
        this.name = name;
        this.prefix = prefix;

        this.title = ProxyServer.getInstance().createTitle();
        this.title.title(new TextComponent(title));
    }

    public String getPrefix()
    {
        return prefix;
    }

    public Title getTitle()
    {
        return title;
    }

    public static Server getUniqueInstanceByName(String name)
    {
        for (final Server server : Server.values())
        {
            if (server.name.equals(name))
            {
                return server;
            }
        }

        return UnknownServer;
    }
}
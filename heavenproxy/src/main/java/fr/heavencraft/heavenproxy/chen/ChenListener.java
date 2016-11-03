package fr.heavencraft.heavenproxy.chen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import fr.heavencraft.heavenproxy.AbstractListener;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.event.EventHandler;

public class ChenListener extends AbstractListener
{
    private static final int WARMUP_ITERATIONS = 3000;
    private static final String WARMUP_SENTENCE = "Ceci, est une phrase de merde.";

    private final StringBuilder builder = new StringBuilder();
    private final Random random = new Random();

    private final Collection<String> bannedWords = new HashSet<String>();
    private final List<String> replaceWords = new ArrayList<String>();

    public ChenListener()
    {
        super();

        // Insultes
        bannedWords.add("batard");
        bannedWords.add("biatch");
        bannedWords.add("betch");
        bannedWords.add("betsch");
        bannedWords.add("betsh");
        bannedWords.add("bitch");
        bannedWords.add("bite");
        bannedWords.add("bounioul");
        bannedWords.add("catin");
        bannedWords.add("ciboire");
        bannedWords.add("con");
        bannedWords.add("conasse");
        bannedWords.add("connard");
        bannedWords.add("connasse");
        bannedWords.add("dumbass");
        bannedWords.add("encule");
        bannedWords.add("enculé");
        bannedWords.add("enculer");
        bannedWords.add("fdp");
        bannedWords.add("fuck");
        bannedWords.add("fucking");
        bannedWords.add("fucktard");
        bannedWords.add("gtfo");
        bannedWords.add("katin");
        bannedWords.add("lopette");
        bannedWords.add("marde");
        bannedWords.add("merde");
        bannedWords.add("merdeux");
        bannedWords.add("motherfucker");
        bannedWords.add("niquer");
        bannedWords.add("pd");
        bannedWords.add("pédé");
        bannedWords.add("putain");
        bannedWords.add("putin");
        bannedWords.add("pute");
        bannedWords.add("salaud");
        bannedWords.add("salop");
        bannedWords.add("salopard");
        bannedWords.add("salope");
        bannedWords.add("stfu");
        bannedWords.add("tg");
        bannedWords.add("tageule");
        bannedWords.add("tayeul");
        bannedWords.add("tayeule");

        // Serveurs
        bannedWords.add("desticraft");
        bannedWords.add("historycraft");
        bannedWords.add("minefield");
        bannedWords.add("thecraft");

        // Mots de remplacement
        replaceWords.add("poney");
        replaceWords.add("poulette");
        replaceWords.add("fraise");
        replaceWords.add("lait demi-écrémé");
        replaceWords.add("pika");
        replaceWords.add("[censuré]");
        replaceWords.add("cuniculiculture");
        replaceWords.add("compote");
        replaceWords.add("balançoire");
        replaceWords.add("pastèque");

        warmup();
    }

    private void warmup()
    {
        log.info("Performing warmup");

        for (int i = 0; i != WARMUP_ITERATIONS; i++)
            censor(WARMUP_SENTENCE);
    }

    public String censor(final String message)
    {
        final String[] words = message.split("\\b");

        for (final String word : words)
        {
            if (bannedWords.contains(word.toLowerCase()))
                builder.append(replaceWords.get(random.nextInt(replaceWords.size())));
            else
                builder.append(word);
        }

        try
        {
            return builder.toString();
        }
        finally
        {
            builder.setLength(0);
        }
    }

    @EventHandler()
    public void onPlayerChat(ChatEvent event)
    {
        if (event.isCancelled())
            return;

        event.setMessage(censor(event.getMessage()));
    }
}
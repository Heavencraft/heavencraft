package fr.hc.core.chat;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import fr.hc.core.chat.ChatMessage.MessageColors;

public class TextualTooltip
{
	// Generic
	private final MessageColors color;
	private final String text;
	private boolean bold = false;
	private boolean underlined = false;
	private boolean italic = false;
	private boolean strikethrough = false;
	private boolean obfuscated = false;
	private String insertion = null;
	private List<MessageChunk> messageChunks = new ArrayList<>();

	public TextualTooltip(String text, MessageColors color, boolean bold, boolean underlined, boolean italic,
			boolean strikethrough, boolean obfuscated, String insertion)
	{
		this.color = color;
		this.text = text;
		this.bold = bold;
		this.underlined = underlined;
		this.italic = italic;
		this.strikethrough = strikethrough;
		this.obfuscated = obfuscated;
		this.insertion = insertion;
	}

	public TextualTooltip addChunk(MessageChunk chk)
	{
		this.messageChunks.add(chk);
		return this;
	}

	protected JSONObject getJson()
	{
		JSONObject raw = new JSONObject();
		if (this.text != null)
			raw.put("text", this.text);
		if (this.color != null)
			raw.put("color", this.color.getColor());
		if (this.bold)
			raw.put("bold", "true");
		if (this.underlined)
			raw.put("underlined", "true");
		if (this.italic)
			raw.put("italic", "true");
		if (this.strikethrough)
			raw.put("strikethrough", "true");
		if (this.obfuscated)
			raw.put("obfuscated", "true");
		if (this.insertion != null)
			raw.put("insertion", this.insertion);

		JSONArray extras = new JSONArray();
		for (MessageChunk ch : this.messageChunks)
		{
			extras.put(ch.getJson(false));
		}
		raw.put("extra", extras);

		return raw;
	}

}

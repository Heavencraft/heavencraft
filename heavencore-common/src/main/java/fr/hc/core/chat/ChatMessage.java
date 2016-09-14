package fr.hc.core.chat;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChatMessage
{

	private final String baseText;
	private MessageColors baseColor;
	private List<MessageChunk> messageChunks = new ArrayList<>();
	private boolean bold = false;
	private boolean underlined = false;
	private boolean italic = false;
	private boolean strikethrough = false;
	private boolean obfuscated = false;
	private String insertion = null;

	/**
	 * Creates a new chat Message. Use PlayerUtil.sendMessage to send those. Supports unicode with notation \\uXXXX
	 * where XXXX is the hex number of the char.
	 * 
	 * @param text
	 */
	public ChatMessage(final String text)
	{
		this.baseText = text;
	}

	public ChatMessage addChunk(MessageChunk chk)
	{
		this.messageChunks.add(chk);
		return this;
	}

	public ChatMessage color(MessageColors color)
	{
		this.baseColor = color;
		return this;
	}

	public ChatMessage bold()
	{
		this.bold = true;
		return this;
	}

	public ChatMessage underlined()
	{
		this.underlined = true;
		return this;
	}

	public ChatMessage italic()
	{
		this.italic = true;
		return this;
	}

	public ChatMessage strikethrough()
	{
		this.strikethrough = true;
		return this;
	}

	public ChatMessage obfuscated()
	{
		this.obfuscated = true;
		return this;
	}

	/**
	 * When the text is shift-clicked, this string will be inserted in their chat input.
	 * 
	 * @return
	 */
	public ChatMessage insertion(String text)
	{
		this.insertion = text;
		return this;
	}

	public String build()
	{
		JSONObject raw = new JSONObject();
		raw.put("text", this.baseText);
		if (this.baseColor != null)
			raw.put("color", this.baseColor.getColor());

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
			extras.put(ch.getJson(true));
		}

		raw.put("extra", extras);

		return raw.toString().replace("\\ ", " ").replace("\\", "");
	}

	public enum ClickEventActions
	{
		OPENURL("open_url"),
		OPENFILE("open_file"),
		RUNCMD("run_command"),
		CHANGE_PARGE("change_page"),
		SUGGEST_COMMAND("suggest_command");

		private final String cmd;

		ClickEventActions(String command)
		{
			this.cmd = command;
		}

		public String getCommand()
		{
			return cmd;
		}
	}

	public enum MessageColors
	{
		BLACK("black"),
		DARK_BLUE("dark_blue"),
		DARK_GREEN("dark_green"),
		DARK_AQUA("dark_aqua"),
		DARK_RED("dark_red"),
		DARK_PURPLE("dark_purple"),
		GOLD("gold"),
		GRAY("gray"),
		DARK_GRAY("dark_gray"),
		BLUE("blue"),
		GREEN("green"),
		AQUA("aqua"),
		RED("red"),
		LIGHT_PURPLE("light_purple"),
		YELLOW("yellow"),
		WHITE("white"),
		RESET("reset");
		private final String cmd;

		MessageColors(String color)
		{
			this.cmd = color;
		}

		public String getColor()
		{
			return cmd;
		}
	}

	// ********************************************** USAGE **********************************************
	//
	// ChatMessage msg = new ChatMessage("This is a sample ").color(ChatMessage.MessageColors.WHITE);
	//
	// msg.addChunk(new MessageChunk("Hover over me ").color(ChatMessage.MessageColors.GOLD).bold().italic()
	// .underlined().addClickEvent(ChatMessage.ClickEventActions.RUNCMD, "/t PlayerName yolo")
	// .plaintextTooltip(new PlainTextTooltip("Howdy ", null, false, false, false, false, false, null)
	// .addChunk(new MessageChunk("COWBOY").bold().color(ChatMessage.MessageColors.RED))));
	//
	// msg.addChunk(new MessageChunk("(Open Webpage)").color(ChatMessage.MessageColors.BLUE)
	// .addClickEvent(ChatMessage.ClickEventActions.OPENURL, "http://www.heavencraft.fr/"));
	//
	// msg.addChunk(new MessageChunk(" (Shift click to insert) ").insertion("/bourse")
	// .color(ChatMessage.MessageColors.GREEN));
	//
	// msg.addChunk(new MessageChunk("(Display) ").entityTooltip("HERE NBT COMPUND"));
	//
	// ChatUtil.sendMessage(player, msg);
	//
	// ********************************************** USAGE **********************************************

}

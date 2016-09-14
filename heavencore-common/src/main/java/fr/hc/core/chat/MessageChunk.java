package fr.hc.core.chat;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import fr.hc.core.chat.ChatMessage.MessageColors;

public class MessageChunk
{

	private final HashMap<ChatMessage.ClickEventActions, String> clickEvents = new HashMap<ChatMessage.ClickEventActions, String>();

	// Generic
	private MessageColors color;
	private final String text;
	private boolean bold = false;
	private boolean underlined = false;
	private boolean italic = false;
	private boolean strikethrough = false;
	private boolean obfuscated = false;
	private String insertion = null;

	// Hover Actions
	private String itemTooltip = null;
	private String entityTooltip = null;
	private String archievementTooltip = null;
	private TextualTooltip textualTooltip = null;

	public MessageChunk(String text)
	{
		this.text = text;
	}

	/**
	 * Sets the color of this part
	 * 
	 * @param color
	 * @return
	 */
	public MessageChunk color(MessageColors color)
	{
		this.color = color;
		return this;
	}

	/**
	 * Displays on hovering an item.
	 * 
	 * @param text
	 *            String in NBT data format, containing 'id' optionally 'damage' & 'tag' tags. (Like /give)
	 * @return
	 */
	public MessageChunk itemTooltip(String text)
	{
		this.itemTooltip = text;
		this.archievementTooltip = null;
		this.entityTooltip = null;
		this.textualTooltip = null;
		return this;
	}

	public MessageChunk archievementTooltip(String text)
	{
		this.itemTooltip = null;
		this.archievementTooltip = text;
		this.entityTooltip = null;
		this.textualTooltip = null;
		return this;
	}

	public MessageChunk entityTooltip(String text)
	{
		this.itemTooltip = null;
		this.archievementTooltip = null;
		this.entityTooltip = text;
		this.textualTooltip = null;
		return this;
	}

	public MessageChunk textualTooltip(TextualTooltip tooltip)
	{
		this.itemTooltip = null;
		this.archievementTooltip = null;
		this.entityTooltip = null;
		this.textualTooltip = tooltip;
		return this;
	}

	/**
	 * Adds an event when clicking over this chunk of message
	 * 
	 * @param evt
	 * @return
	 */
	public MessageChunk addClickEvent(ChatMessage.ClickEventActions evt, String value)
	{
		clickEvents.put(evt, value);
		return this;
	}

	public MessageChunk bold()
	{
		this.bold = true;
		return this;
	}

	public MessageChunk underlined()
	{
		this.underlined = true;
		return this;
	}

	public MessageChunk italic()
	{
		this.italic = true;
		return this;
	}

	public MessageChunk strikethrough()
	{
		this.strikethrough = true;
		return this;
	}

	public MessageChunk obfuscated()
	{
		this.obfuscated = true;
		return this;
	}

	/**
	 * When the text is shift-clicked, this string will be inserted in their chat input.
	 * 
	 * @return
	 */
	public MessageChunk insertion(String text)
	{
		this.insertion = text;
		return this;
	}

	/**
	 * Renders a JSON object
	 * 
	 * @param hasEvents
	 *            set to false if it is a chunk of a tooltip, Else true.
	 * @return
	 */
	protected JSONObject getJson(boolean hasEvents)
	{
		JSONObject raw = new JSONObject();
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

		if (hasEvents)
		{
			// HoverEvent
			JSONObject hoverObj = new JSONObject();
			if (this.itemTooltip != null)
			{
				hoverObj.put("action", "show_item");
				hoverObj.put("value", this.itemTooltip);
			}
			if (this.archievementTooltip != null)
			{
				hoverObj.put("action", "show_achievement");
				hoverObj.put("value", this.archievementTooltip);
			}
			if (this.entityTooltip != null)
			{
				hoverObj.put("action", "show_entity");
				hoverObj.put("value", this.entityTooltip);
			}
			if (this.textualTooltip != null)
			{
				hoverObj.put("action", "show_text");
				hoverObj.put("value", this.textualTooltip.getJson());
			}
			raw.put("hoverEvent", hoverObj);

			// ClickEvent
			JSONObject clickObj = new JSONObject();
			for (Map.Entry<ChatMessage.ClickEventActions, String> entry : this.clickEvents.entrySet())
			{
				clickObj.put("action", entry.getKey().getCommand());
				clickObj.put("value", entry.getValue()); //
			}
			raw.put("clickEvent", clickObj);
		}
		return raw;
	}

}

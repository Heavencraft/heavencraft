package api.bookbuilder;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class BookBuilder
{
	public final static String HEAVENCRAFT = ChatColor.WHITE + "Heaven" + ChatColor.AQUA + "craft";

	private String title = null;
	private List<String> pages = new ArrayList<String>();

	private final StringBuilder builder = new StringBuilder();

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void writeLine(String line)
	{
		if (!canWrite(line))
			turnPage();

		builder.append(line).append('\n');
	}

	private boolean canWrite(String line)
	{
		return (builder.length() + line.length()) < 256;
	}

	public void turnPage()
	{
		pages.add(builder.toString());
		builder.setLength(0);
	}

	public ItemStack build()
	{
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) book.getItemMeta();
		if (title != null)
			meta.setTitle(title);
		meta.setAuthor(HEAVENCRAFT);

		if (builder.length() != 0)
			turnPage();
		meta.setPages(pages);

		book.setItemMeta(meta);
		// Clear
		title = null;
		pages = new ArrayList<String>();
		builder.setLength(0);
		return book;
	}
}

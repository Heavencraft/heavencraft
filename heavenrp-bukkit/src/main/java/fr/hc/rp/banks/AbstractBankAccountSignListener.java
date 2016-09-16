package fr.hc.rp.banks;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import fr.hc.core.AbstractSignListener;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.db.bankaccounts.BankAccount;

public abstract class AbstractBankAccountSignListener extends AbstractSignListener
{
	private static final String CONSULT = "Consulter";
	private static final String DEPOSIT = "Déposer";
	private static final String WITHDRAW = "Retirer";
	private static final String STATEMENT = "Relevé";
	private static final String BLUE_CONSULT = ChatColor.BLUE + CONSULT;
	private static final String BLUE_DEPOSIT = ChatColor.BLUE + DEPOSIT;
	private static final String BLUE_WITHDRAW = ChatColor.BLUE + WITHDRAW;
	private static final String BLUE_STATEMENT = ChatColor.BLUE + STATEMENT;

	public AbstractBankAccountSignListener(JavaPlugin plugin, String tag, String permission)
	{
		super(plugin, tag, permission);
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException
	{
		String line = event.getLine(1);

		if (line.equalsIgnoreCase(CONSULT))
		{
			event.setLine(1, BLUE_CONSULT);
			return true;
		}
		else if (line.equalsIgnoreCase(DEPOSIT))
		{
			event.setLine(1, BLUE_DEPOSIT);
			return true;
		}
		else if (line.equalsIgnoreCase(WITHDRAW))
		{
			event.setLine(1, BLUE_WITHDRAW);
			return true;
		}
		else if (line.equalsIgnoreCase(STATEMENT))
		{
			event.setLine(1, BLUE_STATEMENT);
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		String line = sign.getLine(1);

		if (line.equals(BLUE_CONSULT))
		{
			onConsultSignClick(player);
		}
		else if (line.equals(BLUE_DEPOSIT))
		{
			onDepositSignClick(player);
		}
		else if (line.equals(BLUE_WITHDRAW))
		{
			onWithdrawSignClick(player);
		}
		else if (line.equals(BLUE_STATEMENT))
		{
			onStatementSignClick(player);
		}
	}

	protected ItemStack createLastTransactionsBook(BankAccount account, int transactionsPerPage) throws HeavenException
	{
		// TODO Implement transaction book
		throw new NotImplementedException();
	}

	protected abstract void onConsultSignClick(Player player) throws HeavenException;

	protected abstract void onDepositSignClick(Player player) throws HeavenException;

	protected abstract void onWithdrawSignClick(Player player) throws HeavenException;

	protected abstract void onStatementSignClick(Player player) throws HeavenException;

}

package fr.hc.rp;

import fr.hc.core.AbstractDatabaseBukkitPlugin;
import fr.hc.core.HeavenCoreInstance;
import fr.hc.core.ReferencePlugin;
import fr.hc.core.users.UsersListener;
import fr.hc.guard.HeavenGuardInstance;
import fr.hc.rp.banks.LivretSignListener;
import fr.hc.rp.cmd.companies.EntrepriseCommand;
import fr.hc.rp.cmd.parcelle.ParcelleCommand;
import fr.hc.rp.cmd.towns.VilleCommand;
import fr.hc.rp.commands.AccepterCommand;
import fr.hc.rp.commands.BourseCommand;
import fr.hc.rp.commands.PayerCommand;
import fr.hc.rp.commands.RejoindreCommand;
import fr.hc.rp.commands.SpawnCommand;
import fr.hc.rp.commands.SpleefCommand;
import fr.hc.rp.commands.TestCommand;
import fr.hc.rp.db.bankaccounts.BankAccountProvider;
import fr.hc.rp.db.companies.CompanyProvider;
import fr.hc.rp.db.npc.NPCActionProvider;
import fr.hc.rp.db.quests.server.ServerQuestProvider;
import fr.hc.rp.db.quests.server.ServerQuestStepProvider;
import fr.hc.rp.db.stocks.StockProvider;
import fr.hc.rp.db.stores.StoreProvider;
import fr.hc.rp.db.towns.TownProvider;
import fr.hc.rp.db.users.RPUserProvider;
import fr.hc.rp.db.warps.RPWarpProvider;
import fr.hc.rp.economy.EconomyListener;
import fr.hc.rp.economy.GoldDropListener;
import fr.hc.rp.economy.MoneyTask;
import fr.hc.rp.economy.stocks.CoffreSignListener;
import fr.hc.rp.economy.stocks.StockListener;
import fr.hc.rp.economy.stores.AchatSignListener;
import fr.hc.rp.economy.stores.MagasinSignListener;
import fr.hc.rp.listeners.FirstSpawnListener;
import fr.hc.rp.listeners.RespawnListener;
import fr.hc.rp.listeners.SpleefManager;
import fr.hc.rp.npc.NPCAdminCommand;
import fr.hc.rp.npc.NPCMessageListener;
import fr.hc.rp.quests.server.ServerQuestAdminCommand;
import fr.hc.rp.quests.server.ServerQuestCommand;
import fr.hc.rp.quests.server.ServerQuestInventoryListener;
import fr.hc.rp.towns.ParcelleSignListener;
import fr.hc.rp.warps.WarpCommandExecutor;
import fr.hc.rp.warps.WarpSignListener;
import fr.hc.rp.worlds.PortalListener;
import fr.hc.rp.worlds.WorldsListener;
import fr.hc.rp.worlds.WorldsManager;

public class BukkitHeavenRP extends AbstractDatabaseBukkitPlugin implements HeavenRP, ReferencePlugin
{
	private BankAccountProvider bankAccountProvider;
	private CompanyProvider companyProvider;
	private TownProvider townProvider;
	private RPUserProvider userProvider;
	private RPWarpProvider warpProvider;
	private StoreProvider storeProvider;
	private StockProvider stockProvider;
	private PricingManager pricingManager;
	private ServerQuestProvider serverQuestProvider;
	private ServerQuestStepProvider serverQuestStepProvider;
	private NPCActionProvider npcMessageProvider;

	public BukkitHeavenRP()
	{
		super("SELECT * FROM users LIMIT 1");
		HeavenRPInstance.set(this);
	}

	@Override
	public void onEnable()
	{
		super.onEnable();
		saveDefaultConfig();

		bankAccountProvider = new BankAccountProvider();
		companyProvider = new CompanyProvider(connectionProvider);
		townProvider = new TownProvider(connectionProvider);
		userProvider = new RPUserProvider(connectionProvider);
		warpProvider = new RPWarpProvider(connectionProvider);
		storeProvider = new StoreProvider(connectionProvider);
		stockProvider = new StockProvider(connectionProvider);
		pricingManager = new PricingManager();
		serverQuestProvider = new ServerQuestProvider();
		serverQuestStepProvider = new ServerQuestStepProvider();
		npcMessageProvider = new NPCActionProvider();

		HeavenCoreInstance.get().setReferencePlugin(this);
		HeavenGuardInstance.get().setUserProvider(userProvider);

		WorldsManager.init();
		new WorldsListener(this);

		new SpleefManager(this);

		new PortalListener(this);
		new UsersListener(this, userProvider);
		new FirstSpawnListener(this);
		new RespawnListener(this);

		new SpleefCommand(this);
		new TestCommand(this);
		new WarpCommandExecutor(this);
		new SpawnCommand(this);
		new BourseCommand(this);
		new ParcelleCommand(this);
		new PayerCommand(this);
		new VilleCommand(this);
		new RejoindreCommand(this);
		new AccepterCommand(this);

		// Company
		new EntrepriseCommand(this);

		// Economy
		new EconomyListener(this);
		new GoldDropListener(this);
		new MoneyTask(this);
		// Economy/Stocks
		new CoffreSignListener(this);
		new StockListener(this);
		// Economy/Stores
		new AchatSignListener(this);
		new MagasinSignListener(this);

		// NPC
		new NPCAdminCommand(this);
		new NPCMessageListener(this);

		// ServerQuests
		final ServerQuestInventoryListener serverQuestInventoryListener = new ServerQuestInventoryListener(this);
		new ServerQuestCommand(this, serverQuestInventoryListener);
		new ServerQuestAdminCommand(this);

		// Bank
		new LivretSignListener(this);

		// Towns
		new ParcelleSignListener(this);

		// Warps
		new WarpSignListener(this);
	}

	@Override
	public BankAccountProvider getBankAccountProvider()
	{
		return bankAccountProvider;
	}

	@Override
	public CompanyProvider getCompanyProvider()
	{
		return companyProvider;
	}

	@Override
	public TownProvider getTownProvider()
	{
		return townProvider;
	}

	@Override
	public RPUserProvider getUserProvider()
	{
		return userProvider;
	}

	@Override
	public RPWarpProvider getWarpProvider()
	{
		return warpProvider;
	}

	@Override
	public StoreProvider getStoreProvider()
	{
		return storeProvider;
	}

	@Override
	public StockProvider getStockProvider()
	{
		return stockProvider;
	}

	@Override
	public PricingManager getPricingManager()
	{
		return pricingManager;
	}

	@Override
	public ServerQuestProvider getServerQuestProvider()
	{
		return serverQuestProvider;
	}

	@Override
	public ServerQuestStepProvider getServerQuestStepProvider()
	{
		return serverQuestStepProvider;
	}

	@Override
	public NPCActionProvider getNpcMessageProvider()
	{
		return npcMessageProvider;
	}
}
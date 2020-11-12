package party.voicechat.proxycore;

import party.voicechat.core.database.Database;
import party.voicechat.core.translator.Translator;
import party.voicechat.proxycore.util.command.CommandExecutor;
import party.voicechat.proxycore.util.database.MongoDB;
import party.voicechat.proxycore.util.player.Users;
import party.voicechat.proxycore.util.plugin.Plugin;
import party.voicechat.proxycore.util.rank.Ranks;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class ProxyCore extends Plugin {

	private ProxyService service;
	private Database database;
	private Users users;
	private Translator translator;
	private Ranks ranks;
	private CommandExecutor commandExecutor;

	@Override
	public void loadUtilities() {
		database = new MongoDB(this);
		translator = Translator.create(getClassLoader(), "spigotcore");
		users = new Users(this);
		ranks = new Ranks(this);
		commandExecutor = new CommandExecutor(this);

		service = new ProxyService(this);
		addService(service);
	}

	@Override
	public void loadCommands() {

	}

	@Override
	public void loadListener() {

	}

	public ProxyService getService() {
		return service;
	}

	public Database getData() {
		return database;
	}

	public Users getUsers() {
		return users;
	}

	public Translator getTranslator() {
		return translator;
	}

	public Ranks getRanks() {
		return ranks;
	}

	public CommandExecutor getCommandExecutor() {
		return commandExecutor;
	}
}

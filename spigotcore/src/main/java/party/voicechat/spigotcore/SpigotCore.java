package party.voicechat.spigotcore;

import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Player;
import party.voicechat.core.database.Database;
import party.voicechat.core.reflectionhelper.ReflectionHelper;
import party.voicechat.core.task.Task;
import party.voicechat.core.translator.Translator;
import party.voicechat.spigotcore.commands.VCPCommand;
import party.voicechat.spigotcore.commands.spigot.TpsCommand;
import party.voicechat.spigotcore.commands.vanilla.GameModeCommand;
import party.voicechat.spigotcore.commands.vanilla.WeatherCommand;
import party.voicechat.spigotcore.commands.vanilla.WhitelistCommand;
import party.voicechat.spigotcore.listeners.PlayerChatListener;
import party.voicechat.spigotcore.listeners.PlayerCommandPreprocessListener;
import party.voicechat.spigotcore.listeners.PlayerJoinListener;
import party.voicechat.spigotcore.listeners.PlayerPreLoginListener;
import party.voicechat.spigotcore.util.command.CommandExecutor;
import party.voicechat.spigotcore.util.database.MongoDB;
import party.voicechat.spigotcore.util.player.Users;
import party.voicechat.spigotcore.util.plugin.Plugin;
import party.voicechat.spigotcore.util.rank.Ranks;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class SpigotCore extends Plugin {

	private SpigotService service;
	private Database database;
	private Users users;
	private Translator translator;
	private Ranks ranks;
	private CommandExecutor commandExecutor;

	@Override
	public void onEnable() {
		super.onEnable();
		Task.of(this::initializeCommands).runLaterAsync(1, TimeUnit.SECONDS);
	}

	@Override
	public void loadUtilities() {
		database = new MongoDB(this);
		database.connect();
		translator = Translator.create(getClassLoader(), "spigotcore");
		ranks = new Ranks(this);
		ranks.loadRanks();
		users = new Users(this);
		commandExecutor = new CommandExecutor(this);

		service = new SpigotService(this);
		addService(service);
	}

	@Override
	public void loadCommands() {
		commandExecutor.registerCommand(new VCPCommand(this));

		commandExecutor.registerCommand(new TpsCommand(this));

		commandExecutor.registerCommand(new GameModeCommand(this));
		commandExecutor.registerCommand(new WeatherCommand());
		commandExecutor.registerCommand(new WhitelistCommand());
	}

	@Override
	public void loadListener() {
		getServer().getPluginManager().registerEvents(new PlayerChatListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerCommandPreprocessListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerPreLoginListener(this), this);
	}

	public SpigotService getService() {
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

	public void sendMessage(Player player, String key, Object... arguments) {
		player.sendMessage(translator.getString(translator.getDefaultLocale(), key, arguments));
	}

	public void sendErrorMessage(Player player) {
		player.sendMessage(translator.getString(translator.getDefaultLocale(), "error"));
	}

	private void initializeCommands() {
		try {
			CraftServer craftServer = (CraftServer) getServer();
			ReflectionHelper commandMapReflection = ReflectionHelper.of(craftServer, "commandMap");
			SimpleCommandMap commandMap = (SimpleCommandMap) commandMapReflection.get();

			ReflectionHelper knownCommandsReflection = ReflectionHelper.of(commandMap, "knownCommands")
					.setFinal();
			Map<String, Command> stringCommandMap = (Map<String, Command>) knownCommandsReflection.get();

			commandExecutor.setCommands(stringCommandMap);

			List<String> vanillaCommands = Lists.newArrayList();
			Iterator<String> commandIterator = stringCommandMap.keySet().iterator();
			while (commandIterator.hasNext()) {
				String label = commandIterator.next();
				if (label.contains(":")) {
					String[] splitLabel = label.split(":");
					commandIterator.remove();

					if (splitLabel[0].equals("bukkit") || splitLabel[0].equals("minecraft")) {
						vanillaCommands.add(splitLabel[1]);
					}
				}
			}

			vanillaCommands.forEach(stringCommandMap::remove);
			knownCommandsReflection.set(stringCommandMap);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}

package party.voicechat.spigotcore.util.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import party.voicechat.core.command.Command;
import party.voicechat.core.command.SubCommand;
import party.voicechat.core.player.User;
import party.voicechat.core.task.Task;
import party.voicechat.spigotcore.SpigotCore;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class CommandExecutor {

	private final SpigotCore spigotCore;
	private final Map<String, Command> commandMap;

	public CommandExecutor(SpigotCore spigotCore) {
		this.spigotCore = spigotCore;

		commandMap = Maps.newHashMap();
	}

	public Map<String, Command> getCommandMap() {
		return commandMap;
	}

	public void registerCommand(Command command) {
		DummyCommand dummyCommand = new DummyCommand(command);

		initializeCommand(command.getLabel(), command);

		for (String alias : command.getAliases()) {
			initializeCommand(alias, command);
		}
	}

	public void initializeCommand(String label, Command command) {
		commandMap.put(command.getLabel(), command);
		spigotCore.getServer().getCommandMap().register(label, new DummyCommand(command));
	}

	private class DummyCommand extends org.bukkit.command.Command {

		private final Command command;
		private final Map<User, List<String>> tabCache;

		protected DummyCommand(Command command) {
			super(command.getLabel(), "description", "", Arrays.asList(command.getAliases()));
			this.command = command;

			tabCache = Maps.newHashMap();

			setPermission(null);
		}

		@Override
		public boolean execute(CommandSender commandSender, String label, String[] args) {
			if (!(commandSender instanceof Player)) {
				command.onConsoleExecute(commandSender, label, args);
				return true;
			}

			Optional<User> optional = spigotCore.getUsers().getUserByPlayer((Player) commandSender);
			if (!optional.isPresent()) {
				spigotCore.sendErrorMessage((Player) commandSender);
				return true;
			}

			User user = optional.get();
			try {
				if (args.length == 0 || command.getSubCommands().isEmpty()) {
					if (!user.hasPermission(command.getPermissionNeeded())) {
						user.sendMessage(spigotCore.getTranslator(), "noPermission");
						return true;
					}

					command.onExecute(user, label, args);
					return true;
				}

				Optional<SubCommand> optionalSubCommand = command.getSubCommand(args[0].toLowerCase());
				if (!optionalSubCommand.isPresent()) {
					command.onExecute(user, label, args);
					return true;
				}

				SubCommand subCommand = optionalSubCommand.get();
				if (!user.hasPermission(subCommand.getPermissionNeeded())) {
					command.onExecute(user, label, args);
					return true;
				}

				String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
				subCommand.onExecute(user, args[0], subArgs);
			} catch (Exception e) {
				e.printStackTrace();
				user.sendMessage(spigotCore.getTranslator(), "commandError");
			}
			return true;
		}

		@Override
		public List<String> tabComplete(CommandSender commandSender, String label, String[] args) {
			if (!(commandSender instanceof Player)) {
				return super.tabComplete(commandSender, label, args);
			}

			Optional<User> optional = spigotCore.getUsers().getUserByPlayer((Player) commandSender);
			if (!optional.isPresent()) {
				spigotCore.sendErrorMessage((Player) commandSender);
				return Lists.newArrayList();
			}

			User user = optional.get();
			if (tabCache.containsKey(user)) {
				return tabCache.get(user);
			}

			List<String> tabComplete = super.tabComplete(commandSender, label, args);
			if (!user.hasPermission(command.getPermissionNeeded())) {
				return tabComplete;
			}

			if (args.length == 0) {
				tabComplete.add(label);
				return tabComplete;
			}

			tabComplete.clear();
			if (args.length == 1) {
				for (SubCommand subCommand : command.getSubCommands()) {
					if (user.hasPermission(subCommand.getPermissionNeeded())) {
						tabComplete.add(subCommand.getLabel());
					}
				}

				tabComplete.addAll(command.onTabComplete(user, label, args));
				addCache(user, tabComplete);
				return tabComplete;
			}

			Optional<SubCommand> optionalSubCommand = command.getSubCommand(args[0].toLowerCase());
			if (!optionalSubCommand.isPresent() || !user.hasPermission(
					optionalSubCommand.get().getPermissionNeeded())) {
				tabComplete = command.onTabComplete(user, label, args);
				addCache(user, tabComplete);
				return tabComplete;
			}

			SubCommand subCommand = optionalSubCommand.get();
			String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
			tabComplete = subCommand.onTabComplete(user, label, subArgs);
			addCache(user, tabComplete);
			return tabComplete;
		}

		private void addCache(User user, List<String> list) {
			tabCache.put(user, list);
			Task.of(() -> tabCache.remove(user)).runLaterAsync(1, TimeUnit.SECONDS);
		}
	}
}

package party.voicechat.core.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import party.voicechat.core.player.User;
import party.voicechat.core.translator.Translator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public abstract class Command {

	private final String label;
	private final String[] aliases;
	private final String permissionNeeded;

	private final List<SubCommand> subCommands;
	private final Map<User, List<String>> tabCache;

	public Command(String label, String permissionNeeded, String... aliases) {
		this.label = label;
		this.permissionNeeded = permissionNeeded;
		this.aliases = aliases;

		subCommands = Lists.newArrayList();
		tabCache = Maps.newHashMap();
	}

	public abstract void onExecute(User user, String label, String[] args);

	public void onConsoleExecute(Object object, String label, String[] args) {

	}

	public List<String> onTabComplete(User user, String label, String[] args) {
		return Lists.newArrayList();
	}

	public final String getLabel() {
		return label;
	}

	public final String getPermissionNeeded() {
		return permissionNeeded;
	}

	public final String[] getAliases() {
		return aliases;
	}

	public final List<SubCommand> getSubCommands() {
		return subCommands;
	}

	public final void addSubCommand(SubCommand subCommand) {
		subCommands.add(subCommand);
	}

	public final void sendMessage(Translator translator, Object object, String key,
	                              Object... arguments) {
		if (object instanceof User) {
			((User) object).sendMessage(translator, key, arguments);
		} else {
			System.out.println(
					"[" + label.toUpperCase() + "] " + translator.getString(translator.getDefaultLocale(),
							key, arguments).replace("§", "&"));
		}
	}

	public Optional<SubCommand> getSubCommand(String label) {
		for (SubCommand subCommand : subCommands) {
			if (subCommand.getLabel().equals(label)) {
				return Optional.of(subCommand);
			}

			for (String alias : subCommand.getAliases()) {
				if (alias.equals(label)) {
					return Optional.of(subCommand);
				}
			}
		}

		return Optional.empty();
	}
}
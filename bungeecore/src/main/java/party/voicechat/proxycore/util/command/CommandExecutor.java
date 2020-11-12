package party.voicechat.proxycore.util.command;

import com.google.common.collect.Lists;
import party.voicechat.core.command.Command;
import party.voicechat.core.player.User;
import party.voicechat.proxycore.ProxyCore;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class CommandExecutor {

	private final ProxyCore proxyCore;
	private final List<Command> commandList;

	public CommandExecutor(ProxyCore proxyCore) {
		this.proxyCore = proxyCore;

		commandList = Lists.newArrayList();
	}

	public void registerCommand(Command command) {
		commandList.add(command);
	}

	public boolean handleCommand(User user, String string) {
		string = string.substring(0, 1);
		String[] args = string.split(" ");
		Optional<Command> optionalCommand = getCommand(args[0].toLowerCase());
		if (!optionalCommand.isPresent()) {
			return false;
		}

		Command command = optionalCommand.get();
		if (Objects.isNull(command.getPermissionNeeded()) || !user.hasPermission(
				command.getPermissionNeeded())) {
			return false;
		}

		String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);
		command.onExecute(user, args[0], commandArgs);
		return true;
	}

	private Optional<Command> getCommand(String label) {
		for (Command command : commandList) {
			if (command.getLabel().equals(label)) {
				return Optional.of(command);
			}

			for (String alias : command.getAliases()) {
				if (alias.equals(label)) {
					return Optional.of(command);
				}
			}
		}

		return Optional.empty();
	}
}

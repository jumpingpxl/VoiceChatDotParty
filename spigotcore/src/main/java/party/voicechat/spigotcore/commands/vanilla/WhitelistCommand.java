package party.voicechat.spigotcore.commands.vanilla;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import party.voicechat.core.command.Command;
import party.voicechat.core.player.User;

import java.util.List;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class WhitelistCommand extends Command {

	private org.bukkit.command.defaults.WhitelistCommand whitelistCommand;

	public WhitelistCommand() {
		super("whitelist", "command.admin.whitelist");
		whitelistCommand = new org.bukkit.command.defaults.WhitelistCommand();
	}

	@Override
	public void onExecute(User user, String label, String[] args) {
		whitelistCommand.execute((Player) user.getPlayer(), label, args);
	}

	@Override
	public void onConsoleExecute(Object object, String label, String[] args) {
		whitelistCommand.execute((CommandSender) object, label, args);
	}

	@Override
	public List<String> onTabComplete(User user, String label, String[] args) {
		return whitelistCommand.tabComplete((Player) user.getPlayer(), label, args);
	}
}

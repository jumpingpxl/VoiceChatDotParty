package party.voicechat.spigotcore.commands.vanilla;

import party.voicechat.core.command.Command;
import party.voicechat.core.player.User;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class GameRuleCommand extends Command {

	public GameRuleCommand() {
		super("gamerule", "command.admin.gamerule");
	}

	@Override
	public void onExecute(User user, String label, String[] args) {

	}

	@Override
	public void onConsoleExecute(Object object, String label, String[] args) {

	}
}

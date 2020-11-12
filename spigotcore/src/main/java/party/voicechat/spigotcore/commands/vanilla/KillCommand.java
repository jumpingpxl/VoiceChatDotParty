package party.voicechat.spigotcore.commands.vanilla;

import party.voicechat.core.command.Command;
import party.voicechat.core.player.User;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class KillCommand extends Command {

	public KillCommand() {
		super("kill", "command.admin.kill");
	}

	@Override
	public void onExecute(User user, String label, String[] args) {

	}

	@Override
	public void onConsoleExecute(Object object, String label, String[] args) {

	}
}

package party.voicechat.spigotcore.commands.vanilla;

import party.voicechat.core.command.Command;
import party.voicechat.core.player.User;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class StopCommand extends Command {

	public StopCommand() {
		super("stop", "command.admin.stop");
	}

	@Override
	public void onExecute(User user, String label, String[] args) {

	}

	@Override
	public void onConsoleExecute(Object object, String label, String[] args) {

	}
}
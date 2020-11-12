package party.voicechat.spigotcore.commands.vanilla;

import party.voicechat.core.command.Command;
import party.voicechat.core.player.User;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class TimeCommand extends Command {

	public TimeCommand() {
		super("time", "command.admin.time");
	}

	@Override
	public void onExecute(User user, String label, String[] args) {

	}

	@Override
	public void onConsoleExecute(Object object, String label, String[] args) {

	}
}
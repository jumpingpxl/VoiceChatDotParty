package party.voicechat.spigotcore.commands.vanilla;

import party.voicechat.core.command.Command;
import party.voicechat.core.player.User;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class TpCommand extends Command {

	public TpCommand() {
		super("tp", "command.admin.tp");
	}

	@Override
	public void onExecute(User user, String label, String[] args) {

	}

	@Override
	public void onConsoleExecute(Object object, String label, String[] args) {

	}
}
package party.voicechat.spigotcore.commands;

import com.google.common.collect.Lists;
import party.voicechat.core.command.Command;
import party.voicechat.core.player.User;
import party.voicechat.spigotcore.SpigotCore;

import java.util.List;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class VCPCommand extends Command {

	private final SpigotCore spigotCore;

	public VCPCommand(SpigotCore spigotCore) {
		super("voicechatparty", null, "vcp");
		this.spigotCore = spigotCore;
	}

	@Override
	public void onExecute(User user, String label, String[] args) {
		sendMessage(spigotCore.getTranslator(), user, "vnoPermission");
	}

	@Override
	public List<String> onTabComplete(User user, String label, String[] args) {
		return Lists.newArrayList("hi", "hallo", "halllllooooo", "keks", "kekseee");
	}
}

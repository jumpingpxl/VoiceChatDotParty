package party.voicechat.spigotcore.commands.spigot;

import org.bukkit.ChatColor;
import party.voicechat.core.command.Command;
import party.voicechat.core.player.User;
import party.voicechat.spigotcore.SpigotCore;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class TpsCommand extends Command {

	private final SpigotCore spigotCore;

	public TpsCommand(SpigotCore spigotCore) {
		super("tps", "command.tps");
		this.spigotCore = spigotCore;
	}

	@Override
	public void onExecute(User user, String label, String[] args) {
		perform(user, label, args);
	}

	@Override
	public void onConsoleExecute(Object object, String label, String[] args) {
		perform(object, label, args);
	}

	private void perform(Object object, String label, String[] args) {
		double[] tps = spigotCore.getServer().spigot().getTPS();
		String[] tpsAvg = new String[tps.length];
		for (int i = 0; i < tps.length; i++) {
			tpsAvg[i] = format(tps[i]);
		}

		sendMessage(spigotCore.getTranslator(), object, "tpsCommand", tpsAvg[0], tpsAvg[1], tpsAvg[2]);
	}

	private String format(double tps) {
		return
				(tps > 16.0D ? ChatColor.YELLOW : tps > 18.0D ? ChatColor.GREEN : ChatColor.RED).toString()
						+ (tps > 20.0D ? "*" : "") + Math.min(Math.round(tps * 100.0D) / 100.0D, 20.0D);
	}
}

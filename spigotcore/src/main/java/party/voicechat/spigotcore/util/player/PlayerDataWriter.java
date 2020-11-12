package party.voicechat.spigotcore.util.player;

import party.voicechat.core.player.DataWriter;
import party.voicechat.core.player.punishment.Punishment;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class PlayerDataWriter implements DataWriter {

	private final Player player;

	public PlayerDataWriter(Player player) {
		this.player = player;
	}

	@Override
	public void write() {
		player.getSpigotCore().g
	}

	@Override
	public void writePunishment(Punishment punishment) {

	}
}

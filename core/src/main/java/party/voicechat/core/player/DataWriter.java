package party.voicechat.core.player;

import party.voicechat.core.player.punishment.Punishment;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public interface DataWriter {

	void write();

	void writePunishment(Punishment punishment);
}

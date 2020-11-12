package party.voicechat.core.player.punishment;

import java.util.UUID;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public interface Punishment {

	UUID getPunishId();

	UUID getPunisher();

	String getReason();

	void setReason(String value);

	long getDate();

	boolean isInvalid();

	void setInvalid(boolean value);
}

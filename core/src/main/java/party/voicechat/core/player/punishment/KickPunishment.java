package party.voicechat.core.player.punishment;

import java.util.UUID;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class KickPunishment implements Punishment {

	private final PunishProfile punishProfile;
	private final UUID punishId;
	private final UUID punisher;
	private final long date;
	private String reason;
	private long length;
	private boolean invalid;

	private KickPunishment(PunishProfile punishProfile, UUID punishId, UUID punisher, String reason,
	                       long date, boolean invalid) {
		this.punishProfile = punishProfile;
		this.punishId = punishId;
		this.punisher = punisher;
		this.reason = reason;
		this.date = date;
		this.invalid = invalid;
	}

	public static KickPunishment create(PunishProfile punishProfile, UUID punishId, UUID punisher,
	                                    String reason, long date, boolean invalid) {
		return new KickPunishment(punishProfile, punishId, punisher, reason, date, invalid);
	}

	@Override
	public UUID getPunishId() {
		return punishId;
	}

	@Override
	public UUID getPunisher() {
		return punisher;
	}

	@Override
	public String getReason() {
		return reason;
	}

	@Override
	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public long getDate() {
		return date;
	}

	@Override
	public boolean isInvalid() {
		return invalid;
	}

	@Override
	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}
}
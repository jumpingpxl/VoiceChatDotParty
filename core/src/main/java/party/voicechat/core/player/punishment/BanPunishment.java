package party.voicechat.core.player.punishment;

import java.util.UUID;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class BanPunishment implements Punishment {

	private final PunishProfile punishProfile;
	private final UUID punishId;
	private final UUID punisher;
	private final long date;
	private String reason;
	private String expireReason;
	private long length;
	private long expiredAt;
	private boolean invalid;
	private boolean expired;

	private BanPunishment(PunishProfile punishProfile, UUID punishId, UUID punisher, String reason,
	                      long date, boolean invalid) {
		this.punishProfile = punishProfile;
		this.punishId = punishId;
		this.punisher = punisher;
		this.reason = reason;
		this.date = date;
		this.invalid = invalid;
	}

	public static BanPunishment create(PunishProfile punishProfile, UUID punishId, UUID punisher,
	                                   String reason, long date, boolean invalid) {
		return new BanPunishment(punishProfile, punishId, punisher, reason, date, invalid);
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

	public String getExpireReason() {
		return expireReason;
	}

	public void setExpireReason(String expireReason) {
		this.expireReason = expireReason;
	}

	@Override
	public long getDate() {
		return date;
	}

	public long getExpireDate() {
		return date + length;
	}

	public long getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(long expiredAt) {
		this.expiredAt = expiredAt;
	}

	@Override
	public boolean isInvalid() {
		return invalid;
	}

	@Override
	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public boolean isActive() {
		return canExpire() && getExpireDate() > System.currentTimeMillis();
	}

	public boolean canExpire() {
		return true;
	}

	public void checkIfExpired() {
		if (!isActive() && !expired) {
			expired = true;
			expireReason = null;
			expiredAt = System.currentTimeMillis();
			punishProfile.setActiveBan(null);
			punishProfile.getExpiredBans().add(this);
			punishProfile.getUser().getDataWriter().writePunishment(this);
		}
	}
}

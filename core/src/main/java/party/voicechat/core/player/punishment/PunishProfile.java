package party.voicechat.core.player.punishment;

import com.google.common.collect.Lists;
import party.voicechat.core.player.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class PunishProfile {

	private final User user;
	private final List<BanPunishment> expiredBans;
	private final List<MutePunishment> expiredMutes;
	private final List<KickPunishment> kicks;
	private BanPunishment activeBan;
	private MutePunishment activeMute;

	private PunishProfile(User user) {
		this.user = user;

		expiredBans = Lists.newArrayList();
		expiredMutes = Lists.newArrayList();
		kicks = Lists.newArrayList();
	}

	public static PunishProfile create(User user) {
		return new PunishProfile(user);
	}

	public User getUser() {
		return user;
	}

	public Optional<BanPunishment> getActiveBan() {
		return Objects.isNull(activeBan) ? Optional.empty() : Optional.of(activeBan);
	}

	public void setActiveBan(BanPunishment activeBan) {
		this.activeBan = activeBan;
	}

	public Optional<MutePunishment> getActiveMute() {
		return Objects.isNull(activeMute) ? Optional.empty() : Optional.of(activeMute);
	}

	public void setActiveMute(MutePunishment activeMute) {
		this.activeMute = activeMute;
	}

	public List<BanPunishment> getExpiredBans() {
		return expiredBans;
	}

	public List<MutePunishment> getExpiredMutes() {
		return expiredMutes;
	}

	public List<KickPunishment> getKicks() {
		return kicks;
	}
}

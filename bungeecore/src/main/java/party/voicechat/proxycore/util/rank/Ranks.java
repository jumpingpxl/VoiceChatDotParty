package party.voicechat.proxycore.util.rank;

import com.google.common.collect.Lists;
import party.voicechat.core.rank.Group;
import party.voicechat.proxycore.ProxyCore;

import java.util.List;
import java.util.Optional;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class Ranks {

	private final ProxyCore proxyCore;
	private final List<Group> groups;

	public Ranks(ProxyCore proxyCore) {
		this.proxyCore = proxyCore;

		groups = Lists.newArrayList();
	}

	public void loadRanks() {
		proxyCore.getData().loadRanks(groups::add);
	}

	public Optional<Group> getRankByName(String name) {
		for (Group group : groups) {
			if (group.getName().equals("name")) {
				return Optional.of(group);
			}
		}

		return Optional.empty();
	}

	public Optional<Group> getDefaultRank() {
		for (Group group : groups) {
			if (group.isDefaultRank()) {
				return Optional.of(group);
			}
		}

		return Optional.empty();
	}
}

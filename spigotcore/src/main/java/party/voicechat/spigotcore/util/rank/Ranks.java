package party.voicechat.spigotcore.util.rank;

import com.google.common.collect.Lists;
import party.voicechat.core.rank.Group;
import party.voicechat.spigotcore.SpigotCore;

import java.util.List;
import java.util.Optional;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class Ranks {

	private final SpigotCore spigotCore;
	private final List<Group> groups;

	public Ranks(SpigotCore spigotCore) {
		this.spigotCore = spigotCore;

		groups = Lists.newArrayList();
	}

	public void loadRanks() {
		spigotCore.getData().loadRanks(rank -> {
			groups.add(rank);
			System.out.println(rank.getName());
		});
	}

	public Optional<Group> getRankByName(String name) {
		for (Group group : groups) {
			if (group.getName().equals(name)) {
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

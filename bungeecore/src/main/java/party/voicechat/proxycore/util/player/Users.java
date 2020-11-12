package party.voicechat.proxycore.util.player;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import party.voicechat.core.player.User;
import party.voicechat.proxycore.ProxyCore;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class Users {

	private final ProxyCore proxyCore;
	private final List<User> cachedPlayers;

	public Users(ProxyCore proxyCore) {
		this.proxyCore = proxyCore;

		cachedPlayers = Lists.newArrayList();
	}

	public void reloadPlayer(User user, Consumer<User> consumer) {
		cachedPlayers.remove(user);
		proxyCore.getData().loadPlayerByUniqueIdAsync(user.getUniqueId(), optionalPlayer -> {
			User newUser = optionalPlayer.get();
			cachedPlayers.add(newUser);
			consumer.accept(newUser);
		});
	}

	public void getPlayerByName(String name, Consumer<Optional<User>> consumer) {
		Optional<User> cachedUser = getOnlinePlayerByName(name);
		if (cachedUser.isPresent()) {
			consumer.accept(cachedUser);
			return;
		}

		proxyCore.getData().loadPlayerByNameAsync(name, optionalUser -> {
			optionalUser.ifPresent(user -> cachedPlayers.add(user));
			consumer.accept(optionalUser);
		});
	}

	public void getPlayerByUniqueId(UUID uniqueId, Consumer<Optional<User>> consumer) {
		Optional<User> cachedPlayer = getOnlinePlayerByUniqueId(uniqueId);
		if (cachedPlayer.isPresent()) {
			consumer.accept(cachedPlayer);
			return;
		}

		proxyCore.getData().loadPlayerByUniqueIdAsync(uniqueId, optionalPlayer -> {
			optionalPlayer.ifPresent(user -> cachedPlayers.add(user));
			consumer.accept(optionalPlayer);
		});
	}

	public Optional<User> getOnlinePlayerByName(String name) {
		for (User player : cachedPlayers) {
			if (player.getName().equalsIgnoreCase(name)) {
				return Optional.of(player);
			}
		}

		return Optional.empty();
	}

	public Optional<User> getOnlinePlayerByUniqueId(UUID uniqueId) {
		for (User player : cachedPlayers) {
			if (player.getUniqueId().equals(uniqueId)) {
				return Optional.of(player);
			}
		}

		return Optional.empty();
	}

	public Optional<User> getOnlinePlayerByPlayer(ProxiedPlayer player) {
		return getOnlinePlayerByUniqueId(player.getUniqueId());
	}
}
package party.voicechat.spigotcore.util.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import party.voicechat.core.player.User;
import party.voicechat.spigotcore.SpigotCore;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class Users {

	private final SpigotCore spigotCore;
	private List<User> cachedPlayers;
	private List<UUID> loadingPlayers;
	private Map<UUID, List<Consumer<Optional<User>>>> pendingActions;

	public Users(SpigotCore spigotCore) {
		this.spigotCore = spigotCore;

		cachedPlayers = Lists.newArrayList();
		loadingPlayers = Lists.newArrayList();
		pendingActions = Maps.newHashMap();
	}

	public void reloadPlayer(User user, Consumer<User> consumer) {
		cachedPlayers.remove(user);
		spigotCore.getData().loadPlayerByUniqueIdAsync(user.getUniqueId(), optionalPlayer -> {
			User newUser = optionalPlayer.get();
			cachedPlayers.add(newUser);
			consumer.accept(newUser);
		});
	}

	public void getUserByNameAsync(String name, Consumer<Optional<User>> consumer) {
		Optional<User> cachedUser = getCachedUserByName(name);
		if (cachedUser.isPresent()) {
			consumer.accept(cachedUser);
			return;
		}

		Player player = spigotCore.getServer().getPlayer(name);
		UUID uniqueId = player.getUniqueId();
		addLoadingPlayer(consumer, uniqueId);

		spigotCore.getData().loadPlayerByNameAsync(name, loadedUser -> {
			loadedUser.ifPresent(this::addUser);
			consumer.accept(loadedUser);
		});
	}

	public Optional<User> getUserByName(String name) {
		Optional<User> cachedUser = getCachedUserByName(name);
		if (cachedUser.isPresent()) {
			return cachedUser;
		}

		Player player = spigotCore.getServer().getPlayer(name);
		UUID uniqueId = player.getUniqueId();
		loadingPlayers.add(uniqueId);

		Optional<User> loadedUser = spigotCore.getData().loadPlayerByName(name);
		loadedUser.ifPresent(this::addUser);
		return loadedUser;
	}

	public void getUserByUniqueIdAsync(UUID uniqueId, Consumer<Optional<User>> consumer) {
		Optional<User> cachedUSer = getCachedUserByUniqueId(uniqueId);
		if (cachedUSer.isPresent()) {
			consumer.accept(cachedUSer);
			return;
		}

		addLoadingPlayer(consumer, uniqueId);
		spigotCore.getData().loadPlayerByUniqueIdAsync(uniqueId, optionalPlayer -> {
			optionalPlayer.ifPresent(user -> cachedPlayers.add(user));
			consumer.accept(optionalPlayer);
		});
	}

	public Optional<User> getUserByUniqueId(UUID uniqueId) {
		Optional<User> cachedUser = getCachedUserByUniqueId(uniqueId);
		if (cachedUser.isPresent()) {
			return cachedUser;
		}

		loadingPlayers.add(uniqueId);
		Optional<User> loadedUser = spigotCore.getData().loadPlayerByUniqueId(uniqueId);
		loadedUser.ifPresent(this::addUser);
		return loadedUser;
	}

	public void getUserByPlayerAsync(Player player, Consumer<Optional<User>> consumer) {
		getUserByUniqueIdAsync(player.getUniqueId(), optional -> {
			if (optional.isPresent()) {
				consumer.accept(optional);
				return;
			}

			spigotCore.sendErrorMessage(player);
		});
	}

	public Optional<User> getUserByPlayer(Player player) {
		return getUserByUniqueId(player.getUniqueId());
	}

	private Optional<User> getCachedUserByName(String name) {
		for (User player : cachedPlayers) {
			if (player.getName().equalsIgnoreCase(name)) {
				return Optional.of(player);
			}
		}

		return Optional.empty();
	}

	private Optional<User> getCachedUserByUniqueId(UUID uniqueId) {
		for (User player : cachedPlayers) {
			if (player.getUniqueId().equals(uniqueId)) {
				return Optional.of(player);
			}
		}

		return Optional.empty();
	}

	public void removeUser(UUID uniqueId) {
		getCachedUserByUniqueId(uniqueId).ifPresent(user -> cachedPlayers.remove(user));
	}

	public List<User> getCachedPlayers() {
		return cachedPlayers;
	}

	private void addUser(User user) {
		if (loadingPlayers.contains(user.getUniqueId())) {
			cachedPlayers.add(user);
			loadingPlayers.remove(user.getUniqueId());

			if (pendingActions.containsKey(user.getUniqueId())) {
				pendingActions.get(user.getUniqueId()).forEach(action -> action.accept(Optional.of(user)));
				pendingActions.remove(user.getUniqueId());
			}
		}
	}

	private void addLoadingPlayer(Consumer<Optional<User>> consumer, UUID uniqueId) {
		if (loadingPlayers.contains(uniqueId)) {
			List<Consumer<Optional<User>>> pending = pendingActions.containsKey(uniqueId) ?
					pendingActions
					.get(uniqueId) : Lists.newArrayList();
			pending.add(consumer);
			pendingActions.put(uniqueId, pending);
			return;
		}

		loadingPlayers.add(uniqueId);
	}
}

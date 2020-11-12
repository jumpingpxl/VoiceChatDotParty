package party.voicechat.core.database;

import party.voicechat.core.player.User;
import party.voicechat.core.player.punishment.PunishProfile;
import party.voicechat.core.rank.Group;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public interface Database {

	void connect();

	void disconnect();

	Object getDatabase();

	void loadRanks(Consumer<Group> groupConsumer);

	Optional<User> loadPlayerByName(String playerName);

	void loadPlayerByNameAsync(String playerName, Consumer<Optional<User>> userConsumer);

	Optional<User> loadPlayerByUniqueId(UUID uniqueId);

	void loadPlayerByUniqueIdAsync(UUID uniqueId, Consumer<Optional<User>> userConsumer);

	PunishProfile loadPunishProfile(UUID uniqueId);

	void loadPunishProfileAsync(UUID uniqueId, Consumer<PunishProfile> profileConsumer);

	void insertUser(User user);

	void updateUser(User user);
}

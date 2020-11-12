package party.voicechat.spigotcore;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import party.voicechat.core.plugin.Service;
import party.voicechat.core.rank.Group;

import java.util.Optional;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class SpigotService implements Service {

	private final SpigotCore spigotCore;

	protected SpigotService(SpigotCore spigotCore) {
		this.spigotCore = spigotCore;
	}

	public MongoCollection<Document> getCollection(String collection) {
		return ((MongoDatabase) spigotCore.getData().getDatabase()).getCollection(collection);
	}

/*	public void getPlayerByName(String name, Consumer<Optional<User>> consumer) {
		spigotCore.getUsers().getUserByNameAsync(name, consumer);
	}

	public void getPlayerByUniqueId(UUID uniqueId, Consumer<Optional<User>> consumer) {
		spigotCore.getUsers().getUserByUniqueIdAsync(uniqueId, consumer);
	}

	public Optional<User> getOnlinePlayerByName(String name) {
		return spigotCore.getUsers().getOnlinePlayerByName(name);
	}

	public Optional<User> getOnlinePlayerByUniqueId(UUID uniqueId) {
		return spigotCore.getUsers().getOnlinePlayerByUniqueId(uniqueId);
	}

	public Optional<User> getOnlinePlayerByPlayer(Player player) {
		return spigotCore.getUsers().getOnlinePlayerByPlayer(player);
	} */

	public Optional<Group> getRankByName(String name) {
		return spigotCore.getRanks().getRankByName(name);
	}

	public Optional<Group> getDefaultRank() {
		return spigotCore.getRanks().getDefaultRank();
	}
}

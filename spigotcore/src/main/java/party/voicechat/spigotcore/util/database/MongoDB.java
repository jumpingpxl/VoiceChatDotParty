package party.voicechat.spigotcore.util.database;

import com.google.common.collect.Lists;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import party.voicechat.core.database.Database;
import party.voicechat.core.player.Settings;
import party.voicechat.core.player.User;
import party.voicechat.core.player.punishment.ExpiredPunishment;
import party.voicechat.core.player.punishment.PunishProfile;
import party.voicechat.core.player.punishment.PunishmentType;
import party.voicechat.core.player.punishment.UserPunishment;
import party.voicechat.core.rank.Group;
import party.voicechat.core.rank.Rank;
import party.voicechat.core.task.Task;
import party.voicechat.spigotcore.SpigotCore;
import party.voicechat.spigotcore.util.player.Player;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class MongoDB implements Database {

	private static final ConnectionString connectionString = new ConnectionString(
			"mongodb+srv://voicechatadmin:CXK6dDPLS8y4IgpG@voicechatdotparty.abv0p.mongodb.net"
					+ "/voicechatdotparty?retryWrites=true&w=majority");

	private final SpigotCore spigotCore;
	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;

	public MongoDB(SpigotCore spigotCore) {
		this.spigotCore = spigotCore;
	}

	@Override
	public final void connect() {
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(
				connectionString).retryWrites(true).build();
		mongoClient = MongoClients.create(settings);
		mongoDatabase = mongoClient.getDatabase("voicechatdotparty");

/*		MongoCursor<Document> cursor = mongoDatabase.
				Task.of(() -> {
			System.out.println("RUN CURSOR");
			cursor.forEachRemaining(action -> System.out.println("CURSOR " + action.getString("name")));
		}).runRepeat(2, TimeUnit.SECONDS); */
	}

	@Override
	public final void disconnect() {
		mongoClient.close();
	}

	@Override
	public MongoDatabase getDatabase() {
		return mongoDatabase;
	}

	@Override
	public void loadRanks(Consumer<Group> rankConsumer) {
		Task.of(() -> {
			MongoCollection<Document> rankCollection = mongoDatabase.getCollection("rank");
			for (Document document : rankCollection.find()) {
				String rankName = document.getString("name");
				String rankColor = document.getString("color");
				String longName = document.getString("longName");
				String shortName = document.getString("shortName");

				boolean defaultRank = document.getBoolean("defaultRank", false);
				boolean showPrefix = document.getBoolean("showPrefix", true);
				int sortId = document.getInteger("sortId", 0);

				List<String> permissions = document.getList("permissions", String.class,
						Lists.newArrayList());
				List<String> externalPermissions = document.getList("externalPermissions", String.class,
						Lists.newArrayList());

				Rank rank = Rank.create(rankName, rankColor, longName, shortName, defaultRank, showPrefix,
						sortId, permissions, externalPermissions);
				rankConsumer.accept(rank);
			}
		}).runAsync();
	}

	@Override
	public Optional<User> loadPlayerByName(String userName) {
		MongoCollection<Document> userCollection = mongoDatabase.getCollection("user");
		Document document = userCollection.find(Filters.eq("name", userName)).first();
		if (Objects.isNull(document)) {
			return Optional.empty();
		}

		User user = getPlayerByDocument(document);
		return Optional.of(user);
	}

	@Override
	public void loadPlayerByNameAsync(String userName, Consumer<Optional<User>> userConsumer) {
		Task.of(() -> userConsumer.accept(loadPlayerByName(userName))).runAsync();
	}

	@Override
	public Optional<User> loadPlayerByUniqueId(UUID uniqueId) {
		MongoCollection<Document> userCollection = mongoDatabase.getCollection("user");
		Document document = userCollection.find(Filters.eq("uuid", String.valueOf(uniqueId))).first();
		if (Objects.isNull(document)) {
			return Optional.empty();
		}

		User user = getPlayerByDocument(document);
		return Optional.of(user);
	}

	@Override
	public void loadPlayerByUniqueIdAsync(UUID uniqueId, Consumer<Optional<User>> userConsumer) {
		Task.of(() -> userConsumer.accept(loadPlayerByUniqueId(uniqueId))).runAsync();
	}

	@Override
	public PunishProfile loadPunishProfile(UUID uniqueId) {
		MongoCollection<Document> punishmentCollection = mongoDatabase.getCollection("punishment");

		return null;
	}

	@Override
	public void loadPunishProfileAsync(UUID uniqueId, Consumer<PunishProfile> profileConsumer) {
		Task.of(() -> profileConsumer.accept(loadPunishProfile(uniqueId))).runAsync();
	}

	@Override
	public void insertUser(User user) {
		Document document = new Document("_id", new ObjectId());
		document.append("uuid", String.valueOf(user.getUniqueId()));
		document.append("name", user.getName());
		document.append("rank", user.getGroup().getName());
		document.append("locale", user.getLocale().getLanguage());
		document.append("skinValue", translateObjectToString(user.getSkinValue()));

/*		UserPunishment ban = user.getBan();
		if(ban.isActive() || !ban.getExpiredPunishments().isEmpty()){
			document.append("ban", getPunishmentDocument(ban));
		}

		UserPunishment mute = user.getMute();
		if(mute.isActive() || !mute.getExpiredPunishments().isEmpty()){
			document.append("mute", getPunishmentDocument(mute));
		} */

		document.append("settings",
				new Document().append("friendRequests", user.getSettings().enabledFriendRequests())
						.append("privateMessages", user.getSettings().enabledFriendRequests()));

		document.append("friends", uuidListToStringList(user.getRawFriends()));
		document.append("friendRequests", uuidListToStringList(user.getRawFriendRequests()));
		document.append("permissions", user.getPermissions());
		document.append("externalPermissions", user.getExternalPermissions());
	}

	@Override
	public void updateUser(User user) {

	}

	private User getPlayerByDocument(Document document) {
		UUID uniqueId = UUID.fromString(document.getString("uuid"));
		String name = document.getString("name");
		Optional<Group> optionalGroup = spigotCore.getRanks().getRankByName(document.getString("rank"
		));
		if (!optionalGroup.isPresent()) {
			optionalGroup = spigotCore.getRanks().getDefaultRank();
		}

		Locale locale = new Locale(document.getString("locale"));
		String skinValue = translateStringToObject(document.getString("skinValue"), String.class);

		UserPunishment ban = getPunishment("ban", PunishmentType.BAN, document);
		UserPunishment mute = getPunishment("mute", PunishmentType.MUTE, document);

		Document settingsDocument = (Document) document.get("settings");
		Settings settings = Settings.create(settingsDocument.getBoolean("friendRequests"),
				settingsDocument.getBoolean("privateMessages"));

		List<UUID> friends = stringListToUuidList(document.getList("friends", String.class));
		List<UUID> friendRequests = stringListToUuidList(
				document.getList("friendRequests", String.class));
		List<String> permissions = document.getList("permissions", String.class);
		List<String> externalPermissions = document.getList("externalPermissions", String.class);

		return Player.create(spigotCore, uniqueId, name, optionalGroup.get(), locale, skinValue, ban,
				mute, settings, friends, friendRequests, permissions, externalPermissions);
	}

	private Document getPunishmentDocument(UserPunishment punishment) {
		Document document = new Document();
		if (punishment.isActive()) {
			document.append("reason", translateObjectToString(punishment.getReason()));
			document.append("punisher", translateObjectToString(punishment.getPunisher()));
			document.append("length", punishment.getLength());
			document.append("date", punishment.getDate());
		}

		List<Document> expired = Lists.newArrayList();
		for (ExpiredPunishment expiredPunishment : punishment.getExpiredPunishments()) {
			Document expiredDocument = new Document();
			expiredDocument.append("reason", translateObjectToString(expiredPunishment.getReason()));
			expiredDocument.append("punisher", translateObjectToString(expiredPunishment.getPunisher()));
			document.append("length", expiredPunishment.getLength());
			document.append("date", expiredPunishment.getDate());
			expiredDocument.append("expiredAt", expiredPunishment.getExpiredAt());
		}

		if (!expired.isEmpty()) {
			document.append("expired", expired);
		}

		return document;
	}

	private UserPunishment getPunishment(String key, PunishmentType type, Document document) {
		if (document.containsKey(key)) {
			Document punishDocument = (Document) document.get(key);
			String reason = null;
			UUID punisher = null;
			long length = 0L;
			long date = 0L;
			if (punishDocument.containsKey("reason")) {
				reason = translateStringToObject(punishDocument.getString("reason"), String.class);
				punisher = translateStringToUuid(punishDocument.getString("punisher"));
				length = punishDocument.getLong("length");
				date = punishDocument.getLong("date");
			}

			List<ExpiredPunishment> expiredPunishments = Lists.newArrayList();
			if (punishDocument.containsKey("expired")) {
				List<Document> expiredDocuments = punishDocument.getList("expired", Document.class);
				for (Document expired : expiredDocuments) {
					String expiredReason = translateStringToObject(expired.getString("reason"),
							String.class);
					UUID expiredPunisher = translateStringToUuid(expired.getString("punisher"));
					long expiredLength = expired.getLong("length");
					long expiredDate = expired.getLong("date");
					long expiredAt = expired.getLong("expiredAt");

					ExpiredPunishment expiredPunishment = ExpiredPunishment.create(expiredPunisher,
							expiredReason, expiredLength, expiredDate, expiredAt);
					expiredPunishments.add(expiredPunishment);
				}
			}

			return UserPunishment.create(type, punisher, reason, length, date, expiredPunishments);
		}

		return UserPunishment.createEmpty(type);
	}

	private String translateObjectToString(Object object) {
		return Objects.isNull(object) ? "" : String.valueOf(object);
	}

	private UUID translateStringToUuid(String string) {
		return string.length() == 0 ? null : UUID.fromString(string);
	}

	private <T> T translateStringToObject(String string, Class<T> object) {
		return string.length() == 0 ? null : object.cast(string);
	}

	private <T> T translateStringToObject(String string, Class<T> object, T defaultValue) {
		return string.length() == 0 ? defaultValue : object.cast(string);
	}

	private List<String> uuidListToStringList(List<UUID> uuidList) {
		List<String> list = Lists.newArrayList();
		for (UUID uuid : uuidList) {
			list.add(String.valueOf(uuid));
		}

		return list;
	}

	private List<UUID> stringListToUuidList(List<String> stringList) {
		List<UUID> list = Lists.newArrayList();
		for (String string : stringList) {
			list.add(UUID.fromString(string));
		}

		return list;
	}
}

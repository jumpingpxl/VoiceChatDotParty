package party.voicechat.spigotcore.util.player;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import party.voicechat.core.player.Settings;
import party.voicechat.core.player.User;
import party.voicechat.core.player.punishment.PunishmentType;
import party.voicechat.core.player.punishment.UserPunishment;
import party.voicechat.core.rank.Group;
import party.voicechat.core.translator.Translator;
import party.voicechat.spigotcore.SpigotCore;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class Player implements User {

	private final SpigotCore spigotCore;
	private final Server server;
	private final UUID uniqueId;
	private Group group;
	private Locale locale;
	private Settings settings;
	private UserPunishment ban;
	private UserPunishment mute;
	private String name;
	private String skinValue;
	private List<UUID> friends;
	private List<UUID> friendRequests;
	private List<String> permissions;
	private List<String> externalPermissions;

	private Player(SpigotCore spigotCore, UUID uniqueId, String name, Group group, Locale locale,
	               String skinValue, UserPunishment ban, UserPunishment mute, Settings settings,
	               List<UUID> friends, List<UUID> friendRequests, List<String> permissions,
	               List<String> externalPermissions) {
		this.spigotCore = spigotCore;
		this.server = spigotCore.getServer();
		this.uniqueId = uniqueId;
		this.name = name;
		this.group = group;
		this.locale = locale;
		this.skinValue = skinValue;
		this.ban = ban;
		this.mute = mute;
		this.settings = settings;
		this.friends = friends;
		this.friendRequests = friendRequests;
		this.permissions = permissions;
		this.externalPermissions = externalPermissions;

		if (Objects.isNull(skinValue)) {

		}
	}

	public static Player create(SpigotCore spigotCore, UUID uniqueId, String name, Group group,
	                            Locale locale, String skinValue, UserPunishment ban,
	                            UserPunishment mute, Settings settings, List<UUID> friends,
	                            List<UUID> friendRequests, List<String> permissions,
	                            List<String> externalPermissions) {
		return new Player(spigotCore, uniqueId, name, group, locale, skinValue, ban, mute, settings,
				friends, friendRequests, permissions, externalPermissions);
	}

	public static Player createNew(SpigotCore spigotCore, org.bukkit.entity.Player player) {
		return new Player(spigotCore, player.getUniqueId(), player.getName(),
				spigotCore.getRanks().getDefaultRank().get(),
				spigotCore.getTranslator().getDefaultLocale(),
				null, UserPunishment.createEmpty(PunishmentType.BAN),
				UserPunishment.createEmpty(PunishmentType.MUTE), Settings.createNew(),
				Lists.newArrayList(),
				Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList());
	}

	protected SpigotCore getSpigotCore() {
		return spigotCore;
	}

	@Override
	public Locale getLocale() {
		return locale;
	}

	@Override
	public Group getGroup() {
		return group;
	}

	@Override
	public Settings getSettings() {
		return settings;
	}

	@Override
	public UserPunishment getBan() {
		return null;
	}

	@Override
	public UserPunishment getMute() {
		return null;
	}

	@Override
	public org.bukkit.entity.Player getPlayer() {
		return server.getPlayer(uniqueId);
	}

	@Override
	public UUID getUniqueId() {
		return uniqueId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDisplayName() {
		return group.getColor() + name;
	}

	@Override
	public String getSkinValue() {
		return skinValue;
	}

	@Override
	public String getString(Translator translator, String key, Object... arguments) {
		return translator.getString(locale, key, arguments);
	}

	@Override
	public boolean hasPermission(String permission) {
		return permission == null || permissions.contains(permission);
	}

	@Override
	public boolean isOnline() {
		return Objects.nonNull(getPlayer());
	}

	@Override
	public List<UUID> getRawFriends() {
		return friends;
	}

	@Override
	public List<User> getFriends() {
		List<User> friends = Lists.newArrayList();
		for (UUID rawFriend : this.friends) {
			spigotCore.getUsers().getUserByUniqueIdAsync(rawFriend,
					optional -> optional.ifPresent(friends::add));
		}

		return friends;
	}

	@Override
	public List<UUID> getRawFriendRequests() {
		return friendRequests;
	}

	@Override
	public List<User> getFriendRequests() {
		List<User> friendRequests = Lists.newArrayList();
		for (UUID rawRequest : this.friendRequests) {
			spigotCore.getUsers().getUserByUniqueIdAsync(rawRequest,
					optional -> optional.ifPresent(friendRequests::add));
		}

		return friendRequests;
	}

	@Override
	public List<String> getPermissions() {
		return permissions;
	}

	@Override
	public List<String> getExternalPermissions() {
		return externalPermissions;
	}

	public void sendMessage(String message) {
		getPlayer().sendMessage(message);
	}

	public void sendMessage(Translator translator, String key, Object... arguments) {
		sendMessage(getString(translator, key, arguments));
	}

	@Override
	public void kick(String reason) {

	}

	@Override
	public void sendToServer(String server) {

	}

	@Override
	public void write() {

	}

	private void setSkinValue() {
		EntityPlayer entityPlayer = ((CraftPlayer) getPlayer()).getHandle();
		GameProfile gameProfile = entityPlayer.getProfile();
		Property property = gameProfile.getProperties().get("textures").iterator().next();
		skinValue = property.getValue();
	}
}

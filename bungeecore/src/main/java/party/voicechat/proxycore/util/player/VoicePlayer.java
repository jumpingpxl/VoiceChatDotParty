package party.voicechat.proxycore.util.player;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import party.voicechat.core.player.Settings;
import party.voicechat.core.player.User;
import party.voicechat.core.rank.Group;
import party.voicechat.core.translator.Translator;
import party.voicechat.proxycore.ProxyCore;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class VoicePlayer implements User {

	private final ProxyCore proxyCore;
	private final ProxyServer proxyServer;
	private final UUID uniqueId;
	private Group group;
	private Locale locale;
	private Settings settings;
	private String name;
	private String skinValue;
	private List<UUID> friends;
	private List<String> permissions;
	private List<String> externalPermissions;

	private VoicePlayer(ProxyCore proxyCore, ProxyServer proxyServer, UUID uniqueId, String name,
	                    Group group, Locale locale, String skinValue, Settings settings,
	                    List<UUID> friends, List<String> permissions,
	                    List<String> externalPermissions) {
		this.proxyCore = proxyCore;
		this.proxyServer = proxyServer;
		this.uniqueId = uniqueId;
		this.name = name;
		this.group = group;
		this.locale = locale;
		this.skinValue = skinValue;
		this.settings = settings;
		this.friends = friends;
		this.permissions = permissions;
		this.externalPermissions = externalPermissions;
	}

	public Locale getLocale() {
		return locale;
	}

	public Group getGroup() {
		return group;
	}

	@Override
	public Settings getSettings() {
		return settings;
	}

	public ProxiedPlayer getPlayer() {
		return proxyServer.getPlayer(uniqueId);
	}

	public UUID getUniqueId() {
		return uniqueId;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return group.getColor() + name;
	}

	public String getSkinValue() {
		return null;
	}

	public String getString(Translator translator, String key, Object... arguments) {
		return translator.getString(locale, key, arguments);
	}

	public boolean hasPermission(String permission) {
		return permissions.contains(permission);
	}

	@Override
	public List<UUID> getRawFriends() {
		return friends;
	}

	@Override
	public List<User> getFriends() {
		List<User> friends = Lists.newArrayList();
		for (UUID rawFriend : this.friends) {
			proxyCore.getUsers().getPlayerByUniqueId(rawFriend,
					optional -> optional.ifPresent(friends::add));
		}

		return friends;
	}

	public void sendMessage(String message) {
		getPlayer().sendMessage(message);
	}

	public void sendMessage(Translator translator, String key, Object... arguments) {
		sendMessage(getString(translator, key, arguments));
	}
}
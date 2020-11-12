package party.voicechat.core.player;

import party.voicechat.core.player.punishment.PunishProfile;
import party.voicechat.core.rank.Group;
import party.voicechat.core.translator.Translator;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public interface User {

	Locale getLocale();

	Group getGroup();

	Settings getSettings();

	PunishProfile getPunishProfile();

	DataWriter getDataWriter();

	Object getPlayer();

	UUID getUniqueId();

	String getName();

	String getDisplayName();

	String getSkinValue();

	String getString(Translator translator, String key, Object... arguments);

	boolean hasPermission(String permission);

	boolean isOnline();

	List<UUID> getRawFriends();

	List<User> getFriends();

	List<UUID> getRawFriendRequests();

	List<User> getFriendRequests();

	List<String> getPermissions();

	List<String> getExternalPermissions();

	void sendMessage(String message);

	void sendMessage(Translator translator, String key, Object... arguments);

	void kick(String reason);

	void sendToServer(String server);

	void write();
}

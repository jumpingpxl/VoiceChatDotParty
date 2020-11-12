package party.voicechat.core.player;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class Settings {

	private boolean friendRequests;
	private boolean privateMessages;

	private Settings(boolean friendRequests, boolean privateMessages) {
		this.friendRequests = friendRequests;
		this.privateMessages = privateMessages;
	}

	public static Settings create(boolean friendRequests, boolean privateMessages) {
		return new Settings(friendRequests, privateMessages);
	}

	public static Settings createNew() {
		return new Settings(true, true);
	}

	public boolean enabledFriendRequests() {
		return friendRequests;
	}

	public boolean enabledPrivateMessages() {
		return privateMessages;
	}
}

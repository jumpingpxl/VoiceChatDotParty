package party.voicechat.core.command;

import com.google.common.collect.Lists;
import party.voicechat.core.player.User;

import java.util.List;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public abstract class SubCommand {

	private final Command parent;
	private final String label;
	private final String[] aliases;
	private String permissionNeeded;

	public SubCommand(Command parent, String label, String... aliases) {
		this.parent = parent;
		this.label = label;
		this.aliases = aliases;
	}

	public abstract void onExecute(User user, String label, String[] args);

	public List<String> onTabComplete(User user, String label, String[] args) {
		return Lists.newArrayList();
	}

	public final String getLabel() {
		return label;
	}

	public final String getPermissionNeeded() {
		return permissionNeeded;
	}

	public final void setPermissionNeeded(String permissionNeeded) {
		this.permissionNeeded = permissionNeeded;
	}

	public final String[] getAliases() {
		return aliases;
	}

	public final Command getParent() {
		return parent;
	}
}

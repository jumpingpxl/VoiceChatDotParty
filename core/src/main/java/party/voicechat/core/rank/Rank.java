package party.voicechat.core.rank;

import java.util.List;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class Rank implements Group {

	private final String name;
	private final String color;
	private final String longName;
	private final String shortName;
	private final boolean defaultRank;
	private final boolean showPrefix;
	private final int sortId;
	private final List<String> permissions;
	private final List<String> externalPermissions;

	private Rank(String name, String color, String longName, String shortName, boolean defaultRank,
	             boolean showPrefix, int sortId, List<String> permissions,
	             List<String> externalPermissions) {
		this.name = name;
		this.color = color;
		this.longName = longName;
		this.shortName = shortName;
		this.defaultRank = defaultRank;
		this.showPrefix = showPrefix;
		this.sortId = sortId;
		this.permissions = permissions;
		this.externalPermissions = externalPermissions;
	}

	public static Rank create(String name, String color, String longName, String shortName,
	                          boolean defaultRank, boolean showPrefix, int sortId,
	                          List<String> permissions, List<String> externalPermissions) {
		return new Rank(name, color, longName, shortName, defaultRank, showPrefix, sortId, permissions,
				externalPermissions);
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return "§" + color;
	}

	public String getLongName() {
		return longName;
	}

	public String getShortName() {
		return shortName;
	}

	public boolean isDefaultRank() {
		return defaultRank;
	}

	public boolean isPrefixShown() {
		return showPrefix;
	}

	public boolean hasPermission(String permission) {
		return permissions.contains(permission);
	}

	public int getSortId() {
		return sortId;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public List<String> getExternalPermissions() {
		return null;
	}
}

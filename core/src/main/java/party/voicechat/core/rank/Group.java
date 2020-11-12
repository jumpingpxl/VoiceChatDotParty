package party.voicechat.core.rank;

import java.util.List;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public interface Group {

	String getName();

	String getColor();

	String getLongName();

	String getShortName();

	boolean isDefaultRank();

	boolean isPrefixShown();

	boolean hasPermission(String permission);

	int getSortId();

	List<String> getPermissions();

	List<String> getExternalPermissions();
}

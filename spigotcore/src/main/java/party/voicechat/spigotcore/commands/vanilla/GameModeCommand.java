package party.voicechat.spigotcore.commands.vanilla;

import com.google.common.collect.Lists;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import party.voicechat.core.command.Command;
import party.voicechat.core.player.User;
import party.voicechat.spigotcore.SpigotCore;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class GameModeCommand extends Command {

	private final SpigotCore spigotCore;

	public GameModeCommand(SpigotCore spigotCore) {
		super("gamemode", "command.admin.gamemode", "gm");
		this.spigotCore = spigotCore;
	}

	@Override
	public void onExecute(User user, String label, String[] args) {
		perform(user, label, args);
	}

	@Override
	public void onConsoleExecute(Object object, String label, String[] args) {
		perform(object, label, args);
	}

	private void perform(Object object, String label, String[] args) {
		if (args.length == 0) {
			sendMessage(spigotCore.getTranslator(), object, "gameModeCommandArgs");
			return;
		}

		Player player;
		if (args.length >= 2) {
			if (object instanceof User) {
				User user = (User) object;
				if (!user.hasPermission("command.admin.gamemode.other")) {
					sendMessage(spigotCore.getTranslator(), user, "noPermission");
					return;
				}
			}

			player = spigotCore.getServer().getPlayer(args[1]);
			if (Objects.isNull(player)) {
				sendMessage(spigotCore.getTranslator(), object, "playerNotOnline");
			}
		} else if (object instanceof User) {
			player = (Player) ((User) object).getPlayer();
		} else {
			throw new UnsupportedOperationException("You are the console, you cant switch your "
					+ "gamemode");
		}

		GameMode gameMode = null;
		switch (args[0].toLowerCase()) {
			case "survival":
			case "0":
				gameMode = GameMode.SURVIVAL;
				break;
			case "creative":
			case "1":
				gameMode = GameMode.CREATIVE;
				break;
			case "adventure":
			case "2":
				gameMode = GameMode.ADVENTURE;
				break;
			case "spectator":
			case "3":
				gameMode = GameMode.SPECTATOR;
				break;
			default:
				sendMessage(spigotCore.getTranslator(), object, "gameModeCommandArgs");
				return;
		}

		User user = spigotCore.getUsers().getUserByPlayer(player).get();
		player.setGameMode(gameMode);
		sendMessage(spigotCore.getTranslator(), user, "gameModeCommandChanged", gameMode);
		if (args.length == 2) {
			sendMessage(spigotCore.getTranslator(), object, "gameModeCommandSuccess",
					user.getDisplayName(), gameMode);
		}
	}

	@Override
	public List<String> onTabComplete(User user, String label, String[] args) {
		if (args.length == 1) {
			return Arrays.asList("survival", "creative", "adventure", "spectator");
		}

		if (args.length == 2) {
			List<String> playerNames = Lists.newArrayList();
			for (Player player : spigotCore.getServer().getOnlinePlayers()) {
				playerNames.add(player.getName());
			}

			return playerNames;
		}

		return Lists.newArrayList();
	}
}

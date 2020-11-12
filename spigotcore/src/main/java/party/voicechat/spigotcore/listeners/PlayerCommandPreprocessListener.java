package party.voicechat.spigotcore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import party.voicechat.spigotcore.SpigotCore;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class PlayerCommandPreprocessListener implements Listener {

	private final SpigotCore spigotCore;

	public PlayerCommandPreprocessListener(SpigotCore spigotCore) {
		this.spigotCore = spigotCore;
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
/*		spigotCore.getUsers().getUserByPlayerAsync(player, optional -> {
			User user = optional.get();
			if (spigotCore.getCommandExecutor().handleCommand(user, event.getMessage())) {
				event.setCancelled(true);
			}
		}); */
	}
}

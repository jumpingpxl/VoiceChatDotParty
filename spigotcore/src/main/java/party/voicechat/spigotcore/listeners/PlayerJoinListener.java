package party.voicechat.spigotcore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import party.voicechat.core.player.User;
import party.voicechat.spigotcore.SpigotCore;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class PlayerJoinListener implements Listener {

	private SpigotCore spigotCore;

	public PlayerJoinListener(SpigotCore spigotCore) {
		this.spigotCore = spigotCore;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		Player player = event.getPlayer();
		spigotCore.getUsers().getUserByUniqueIdAsync(player.getUniqueId(), optional -> {
			if (!optional.isPresent()) {
				return;
			}

			User user = optional.get();

			user.sendMessage("§7Willkommen zurück, " + user.getDisplayName());
		});
	}
}

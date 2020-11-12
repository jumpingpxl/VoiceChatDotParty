package party.voicechat.spigotcore.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import party.voicechat.spigotcore.SpigotCore;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class PlayerQuitListener implements Listener {

	private SpigotCore spigotCore;

	public PlayerQuitListener(SpigotCore spigotCore) {
		this.spigotCore = spigotCore;
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		spigotCore.getUsers().removeUser(event.getPlayer().getUniqueId());
	}
}

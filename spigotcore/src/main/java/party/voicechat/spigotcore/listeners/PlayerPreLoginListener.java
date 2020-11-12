package party.voicechat.spigotcore.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import party.voicechat.core.player.User;
import party.voicechat.spigotcore.SpigotCore;

import java.util.Optional;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class PlayerPreLoginListener implements Listener {

	private SpigotCore spigotCore;

	public PlayerPreLoginListener(SpigotCore spigotCore) {
		this.spigotCore = spigotCore;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		Optional<User> optionalUser = spigotCore.getUsers().getUserByUniqueId(event.getUniqueId());
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
		}
	}
}

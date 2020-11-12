package party.voicechat.spigotcore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import party.voicechat.core.player.User;
import party.voicechat.spigotcore.SpigotCore;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class PlayerChatListener implements Listener {

	private final SpigotCore spigotCore;

	public PlayerChatListener(SpigotCore spigotCore) {
		this.spigotCore = spigotCore;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		Player player = event.getPlayer();
		spigotCore.getUsers().getUserByPlayerAsync(player, optional -> {
			if (!optional.isPresent()) {
				return;
			}

			User user = optional.get();
			spigotCore.getTranslator().broadcastMessage(spigotCore.getUsers().getCachedPlayers(),
					"chatFormat", user.getGroup().isPrefixShown() ? 0 : 1, user.getGroup().getColor(),
					user.getGroup().getLongName(), user.getName(), event.getMessage());

			System.out.println(
					"[" + user.getGroup().getLongName() + "] " + user.getName() + " sent a message: "
							+ event.getMessage());
		});
	}
}

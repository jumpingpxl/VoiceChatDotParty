package party.voicechat.spigotcore.commands.vanilla;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import party.voicechat.core.command.Command;
import party.voicechat.core.player.User;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class WeatherCommand extends Command {

	private final org.bukkit.command.defaults.WeatherCommand weatherCommand;

	public WeatherCommand() {
		super("weather", "command.admin.weather");

		this.weatherCommand = new org.bukkit.command.defaults.WeatherCommand();
	}

	@Override
	public void onExecute(User user, String label, String[] args) {
		weatherCommand.execute((Player) user.getPlayer(), label, args);
	}

	@Override
	public void onConsoleExecute(Object object, String label, String[] args) {
		weatherCommand.execute((CommandSender) object, label, args);
	}
}
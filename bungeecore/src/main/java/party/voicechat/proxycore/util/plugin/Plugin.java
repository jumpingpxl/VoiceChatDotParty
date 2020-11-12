package party.voicechat.proxycore.util.plugin;

import com.google.common.collect.Maps;
import party.voicechat.core.plugin.Service;

import java.util.Map;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public abstract class Plugin extends net.md_5.bungee.api.plugin.Plugin {

	private static final Map<Class<? extends Service>, Service> services = Maps.newHashMap();

	@Override
	public void onLoad() {
		loadUtilities();
		loadCommands();
		loadListener();
	}

	public abstract void loadUtilities();

	public abstract void loadCommands();

	public abstract void loadListener();

	public final synchronized Service getService(Class<? extends Service> serviceClass) {
		return services.get(serviceClass);
	}

	public final synchronized void addService(Service service) {
		services.put(service.getClass(), service);
	}
}

package party.voicechat.proxycore;

import party.voicechat.core.plugin.Service;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class ProxyService implements Service {

	private ProxyCore proxyCore;

	protected ProxyService(ProxyCore proxyCore) {
		this.proxyCore = proxyCore;
	}
}

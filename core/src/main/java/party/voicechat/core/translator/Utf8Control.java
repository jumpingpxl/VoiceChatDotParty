package party.voicechat.core.translator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class Utf8Control extends ResourceBundle.Control {

	@Override
	public ResourceBundle newBundle(String baseName, Locale locale, String format,
	                                ClassLoader loader,
	                                boolean reload)
			throws IllegalAccessException, InstantiationException, IOException {
		String bundleName = toBundleName(baseName, locale);
		String resourceName = toResourceName(bundleName, "properties");
		ResourceBundle bundle = null;
		InputStream stream = null;

		if (reload) {
			URL url = loader.getResource(resourceName);
			if (Objects.nonNull(url)) {
				URLConnection connection = url.openConnection();
				if (Objects.nonNull(connection)) {
					connection.setUseCaches(false);
					stream = connection.getInputStream();
				}
			}
		} else {
			stream = loader.getResourceAsStream(resourceName);
		}

		if (Objects.nonNull(stream)) {
			try {
				bundle = new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
			} finally {
				stream.close();
			}
		}

		return bundle;
	}
}
package party.voicechat.core.translator;

import com.google.common.collect.Maps;
import party.voicechat.core.player.User;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class Translator {

	private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{\\w+}");

	private final Map<Locale, ResourceBundleCache> loadedLocales;
	private final ClassLoader classLoader;
	private final Locale defaultLocale;
	private final String baseName;

	private Translator(ClassLoader classLoader, String baseName) {
		this.classLoader = classLoader;
		this.baseName = baseName;

		loadedLocales = Maps.newHashMap();
		defaultLocale = Locale.GERMAN;

		addResourceBundle(defaultLocale);
	}

	public static Translator create(ClassLoader classLoader, String baseName) {
		return new Translator(classLoader, baseName);
	}

	public Locale getDefaultLocale() {
		return defaultLocale;
	}

	public final Translator addLocale(Locale locale) {
		addResourceBundle(locale);
		return this;
	}

	public final String getPattern(Locale locale, String key) {
		if (!loadedLocales.containsKey(locale)) {
			locale = defaultLocale;
		}

		ResourceBundle resourceBundle = loadedLocales.get(locale).getResourceBundle();
		return retrieveString(resourceBundle, key);
	}

	public final String getPattern(User user, String key) {
		return getPattern(user.getLocale(), key);
	}

	public final String format(String rawString, Object... arguments) {
		return MessageFormat.format(rawString, arguments);
	}

	public final String getString(Locale locale, String key, Object... arguments) {
		return format(getPattern(locale, key), arguments);
	}

	public final String getString(User user, String key, Object... arguments) {
		return getString(user.getLocale(), key, arguments);
	}

	public final void sendMessage(User user, String key, Object... arguments) {
		user.sendMessage(getString(user, key, arguments));
	}

	public final void broadcastMessage(List<User> users, String key, Object... arguments) {
		Map<Locale, String> messageMap = Maps.newHashMap();
		for (Locale locale : loadedLocales.keySet()) {
			messageMap.put(locale, getString(locale, key, arguments));
		}

		for (User user : users) {
			if (user.isOnline()) {
				user.sendMessage(messageMap.get(user.getLocale()));
			}
		}
	}

	private String retrieveString(ResourceBundle resourceBundle, String key) {
		String baseString = resourceBundle.getString(key);
		Matcher matcher = VARIABLE_PATTERN.matcher(baseString);
		while (matcher.find()) {
			String variable = matcher.group();
			String variableStripped = variable.substring(2, variable.length() - 1);

			if (resourceBundle.containsKey(variableStripped)) {
				String retrievedVariable = retrieveString(resourceBundle, variableStripped);
				baseString = baseString.replaceAll(Pattern.quote(variable), retrievedVariable);
			} else {
				String escapedVariable = variable.replace("{", "'{'").replace("}", "'}'");
				baseString = baseString.replace(variable, escapedVariable);
			}
		}

		return baseString;
	}

	private void addResourceBundle(Locale locale) {
		ResourceBundleCache resourceBundleCache = ResourceBundleCache.of(baseName, locale,
				classLoader);
		loadedLocales.put(locale, resourceBundleCache);
	}
}

package party.voicechat.core.reflectionhelper;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class ReflectionHelper {

	private static final List<ReflectionHelper> cachedReflections = Lists.newArrayList();
	private static Field modifiersField;

	static {
		try {
			modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	private final Object instance;
	private final Field field;
	private final Class<?> fieldClass;
	private final String fieldName;

	private ReflectionHelper(Object instance, String fieldName) throws NoSuchFieldException {
		this.instance = instance;
		this.fieldName = fieldName;

		fieldClass = instance.getClass();
		field = fieldClass.getDeclaredField(fieldName);
		field.setAccessible(true);
	}

	public static ReflectionHelper create(Object instance, String fieldName)
			throws NoSuchFieldException {
		return new ReflectionHelper(instance, fieldName);
	}

	public static ReflectionHelper of(Object instance, String fieldName) throws NoSuchFieldException {
		Optional<ReflectionHelper> optionalCached = getCachedReflection(instance, fieldName);
		if (optionalCached.isPresent()) {
			return optionalCached.get();
		}

		ReflectionHelper reflectionHelper = new ReflectionHelper(instance, fieldName);
		cachedReflections.add(reflectionHelper);
		return reflectionHelper;
	}

	private static Optional<ReflectionHelper> getCachedReflection(Object instance,
	                                                              String fieldName) {
		for (ReflectionHelper cachedReflection : cachedReflections) {
			if (cachedReflection.fieldClass == instance.getClass() && cachedReflection.fieldName.equals(
					fieldName)) {
				return Optional.of(cachedReflection);
			}
		}

		return Optional.empty();
	}

	public Object get() throws IllegalAccessException {
		return field.get(instance);
	}

	public ReflectionHelper setFinal() throws IllegalAccessException {
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		return this;
	}

	public void set(Object value) throws IllegalAccessException {
		field.set(instance, value);
	}
}

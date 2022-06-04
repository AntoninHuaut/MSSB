package fr.maner.mssb.factory.item;

import com.google.common.collect.ForwardingMultimap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class SkullFactory {

    public static ItemStack buildFromBase64(String base64) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        loadProfile(meta, base64);
        item.setItemMeta(meta);

        return item;
    }

    // TODO check still work
    private static void loadProfile(ItemMeta meta, String base64) {
        Class<?> profileClass = Reflection.getClass("com.mojang.authlib.GameProfile");
        Constructor<?> profileConstructor = Reflection.getDeclaredConstructor(profileClass, UUID.class, String.class);
        Object profile = Reflection.newInstance(profileConstructor, UUID.randomUUID(), null);
        byte[] encodedData = base64.getBytes();

        Method getPropertiesMethod = Reflection.getDeclaredMethod(profileClass, "getProperties");
        Object propertyMap = Reflection.invoke(getPropertiesMethod, profile);
        Class<?> propertyClass = Reflection.getClass("com.mojang.authlib.properties.Property");

        Reflection.invoke(Reflection.getDeclaredMethod(ForwardingMultimap.class, "put", Object.class, Object.class),
                propertyMap, "textures",
                Reflection.newInstance(Reflection.getDeclaredConstructor(propertyClass, String.class, String.class),
                        "textures", new String(encodedData)));

        Reflection.setField("profile", meta, profile);
    }

    private static final class Reflection {

        private static Class<?> getClass(String forName) {
            try {
                return Class.forName(forName);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }

        private static <T> Constructor<T> getDeclaredConstructor(Class<T> clazz, Class<?>... params) {
            try {
                return clazz.getDeclaredConstructor(params);
            } catch (NoSuchMethodException e) {
                return null;
            }
        }

        private static <T> T newInstance(Constructor<T> constructor, Object... params) {
            try {
                return constructor.newInstance(params);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                return null;
            }
        }

        private static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... params) {
            try {
                return clazz.getDeclaredMethod(name, params);
            } catch (NoSuchMethodException e) {
                return null;
            }
        }

        private static Object invoke(Method method, Object object, Object... params) {
            method.setAccessible(true);
            try {
                return method.invoke(object, params);
            } catch (InvocationTargetException | IllegalAccessException e) {
                return null;
            }
        }

        private static void setField(String name, Object instance, Object value) {
            Field field = getDeclaredField(instance.getClass(), name);
            field.setAccessible(true);
            try {
                field.set(instance, value);
            } catch (IllegalAccessException ignored) {
            }
        }

        private static Field getDeclaredField(Class<?> clazz, String name) {
            try {
                return clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                return null;
            }
        }

    }

}
package fr.elitgaimix.models.spigot.utils.injector;

import fr.elitgaimix.models.spigot.Autowired.Autowired;
import fr.elitgaimix.models.spigot.commands.Cmd;
import fr.elitgaimix.models.spigot.commands.SubCmd.SubCmd;
import fr.elitgaimix.models.spigot.commands.arg.CmdArgument;
import fr.elitgaimix.models.spigot.commands.arg.CmdArgumentWP;
import fr.elitgaimix.models.spigot.logger.CtMsg;
import fr.elitgaimix.models.spigot.plugin.PluginController;
import fr.elitgaimix.models.spigot.plugin.ServerInfo;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.checkerframework.checker.units.qual.t;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.jar.JarEntry;
import java.net.URL;
import java.security.CodeSource;
import java.util.*;
import java.util.jar.JarFile;

@SuppressWarnings("all")
public class Injector {

    private static final Map<String, Object> instances = new HashMap<>();

    /**
     * Obtient une instance d'une classe injectée.
     *
     * @param klass la classe de l'instance souhaitée
     * @param <T>   le type de l'instance
     * @return l'instance de la classe
     */
    public static <T> T getInstance(Class<? extends T> klass) {
        if (!instances.containsKey(klass.getSimpleName()))
            inject(instance(klass));
        return (T) instances.get(klass.getSimpleName());
    }

    /**
     * Enregistre une instance injectée.
     *
     * @param instance l'instance à enregistrer
     */
    public static void registerInjectedInstances(Object instance) {
        Class<?> klass = instance.getClass();
        if (!instances.containsKey(klass.getSimpleName()))
            registerInstance(klass.getSimpleName(), instance);
    }

    /**
     * Enregistre une instance injectée.
     *
     * @param component le composant à enregistrer
     * @param <T>       le type du composant
     * @return le composant enregistré
     */
    public static <T> T registerInstance(T component) {
        inject(component);
        return component;
    }

    /**
     * Enregistre une instance injectée avec un nom spécifique.
     *
     * @param name      le nom de l'instance
     * @param component le composant à enregistrer
     */
    public static void registerInstance(String name, Object component) {
        instances.put(name, component);
    }

    /**
     * Injecte les dépendances dans un objet cible.
     *
     * @param target l'objet cible
     */
    public static void inject(Object target) {
        for (Field field : target.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                Object dependency = instance(field.getType());
                if (!instances.containsKey(dependency.getClass().getSimpleName())) {
                    for (Field f : dependency.getClass().getDeclaredFields()) {
                        if (f.isAnnotationPresent(Autowired.class)) {
                            inject(dependency);
                            break;
                        }
                    }
                }
                try {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    field.set(target, dependency);
                    field.setAccessible(accessible);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        registerInjectedInstances(target);
    }

    /**
     * Enregistre un plugin.
     *
     * @param ppackage le package du plugin
     * @throws InvocationTargetException si une erreur d'invocation se produit
     * @throws NoSuchMethodException     si la méthode spécifiée n'existe pas
     * @throws IllegalAccessException    si l'accès à la méthode est refusé
     * @throws InstantiationException    si une erreur d'instanciation se produit
     */
    public static void registerPlugin(String ppackage) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        inject(readClass(ppackage));
        // System.out.println(ChatColor.GREEN + "Plugin " + ppackage + " registered !");
        CtMsg.log("§aPlugin " + ppackage + " registered !");
    }

    /**
     * Lit les classes d'un package.
     *
     * @param packageName le nom du package
     * @return la liste des classes lues
     */
    public static List<Class<?>> readClass(String packageName) {
        String packageRelPath = packageName.replace('.', '/');
        CodeSource src = Injector.class.getProtectionDomain().getCodeSource();
        List<String> classes = new ArrayList<>();

        if (src != null) {
            URL jar = src.getLocation();
            if(jar == null)
                throw new NullPointerException("Jar is null");
            try (JarFile jarFile = new JarFile(jar.getFile())) {
                jarFile.stream()
                        .filter(entry -> !entry.isDirectory() &&
                                !entry.getName().contains("-") &&
                                entry.getName().endsWith(".class"))
                        .map(JarEntry::getName)
                        .filter(name -> name.startsWith(packageRelPath))
                        .filter(Predicates.nonNull())
                        .forEach(classes::add);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        final List<Class<?>> klass = new ArrayList<>();
        for (String classpath : classes) {
            klass.add(getClass(classpath.replace("/", ".")));
        }
        return klass;
    }

    /**
     * Injecte les dépendances dans une classe.
     *
     * @param klass la classe à injecter
     */
    public static void inject(Class<?> klass) {
        if (klass.getSuperclass() != ServerInfo.class && klass.getSuperclass() != SubCmd.class && klass.getSuperclass() != CmdArgument.class && klass.getSuperclass() != CmdArgumentWP.class && klass.getSuperclass() != Cmd.class && !Arrays.stream(klass.getInterfaces()).toList().contains(Listener.class)) {
            for (Field field : klass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    inject(instance(klass));
                }
            }
        }
        if(klass.getSuperclass() == ServerInfo.class){
            try {
                ServerInfo serverInfo = (ServerInfo) instance(klass);
                for (Field field : klass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        inject(serverInfo);
                        break;
                    }
                }
                PluginController.INSTANCE.registerClassInfo(serverInfo);
                // System.out.println("Register server info : \"" + klass.getName() + "\" !");  
                CtMsg.log("§aRegister server info : §6§l\"" + klass.getName() + "\"§r§a !");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (klass.getSuperclass() == Cmd.class) {
            Class<? extends Cmd> kclass = (Class<? extends Cmd>) klass;
            try {
                Cmd obj = kclass.getConstructor().newInstance();
                for (Field field : klass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        inject(obj);
                        break;
                    }
                }
                PluginController.INSTANCE.getServer().getCommandMap().register(PluginController.INSTANCE.getName(), obj);
                // System.out.println("Register cmd : \"" + klass.getName() + "\" !");
                CtMsg.log("§3Register cmd : §6§l\"" + klass.getName() + "\"§r§3 !");
            } catch (Exception ignored) {

            }
        }
        if (Arrays.stream(klass.getInterfaces()).toList().contains(Listener.class)) {
            try {
                Listener listener = (Listener) klass.getConstructor().newInstance();
                for (Field field : klass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        inject(listener);
                        break;
                    }
                }
                PluginController.INSTANCE.getServer().getPluginManager().registerEvents(listener, PluginController.INSTANCE);
                // System.out.println("Register listener : \"" + klass.getName() + "\" !");
                CtMsg.log("§bRegister listener : §6§l\"" + klass.getName() + "\"§r§b !");
            } catch (Exception ignored) {

            }
        }
    }

    /**
     * Injecte les dépendances dans une liste de classes.
     *
     * @param klass la liste de classes à injecter
     */
    public static void inject(List<Class<?>> klass) {
        for (Class<?> kclass : klass) {
            inject(kclass);
        }
    }

    /**
     * Obtient une instance d'une classe.
     *
     * @param kclass la classe de l'instance souhaitée
     * @return l'instance de la classe
     */
    public static Object instance(Class<?> kclass) {
        if (instances.containsKey(kclass.getSimpleName())) {
            return instances.get(kclass.getSimpleName());
        }
        return newInstance(kclass);
    }

    /**
     * Crée une nouvelle instance d'une classe.
     *
     * @param kclass la classe à instancier
     * @return la nouvelle instance de la classe
     */
    public static Object newInstance(Class<?> kclass) {
        try {
            if (kclass.getConstructors().length == 0)
                return kclass.newInstance();
            Constructor<?>[] constructors = kclass.getConstructors();
            for (Constructor<?> constructor : constructors) {
                if (constructor.isAnnotationPresent(Autowired.class)) {
                    Object[] args = new Object[constructor.getParameterCount()];
                    return constructor.newInstance(args);
                }
            }
            Constructor<?> constructor = constructors[0];
            Object[] args = new Object[constructor.getParameterCount()];
            Object instance = constructor.newInstance(args);
            instances.put(instance.getClass().getSimpleName(), instance);
            return instance;
        } catch (Exception e) {
            CtMsg.error("§cClass : " + kclass.getSimpleName() + " Can't be Autowired");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtient une classe à partir d'un chemin.
     *
     * @param path le chemin de la classe
     * @return la classe correspondante
     */
    public static Class<?> getClass(String path) {
        try {
            var classLoader = Injector.class.getClassLoader();
            var classLoaderClass = classLoader.getClass();
            var method = classLoaderClass.getDeclaredMethod("loadClass", String.class, boolean.class);
            method.setAccessible(true);
            Class<?> klass = (Class<?>) method.invoke(classLoader, path.replace(".class", ""), true);
            method.setAccessible(false);
            return klass;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
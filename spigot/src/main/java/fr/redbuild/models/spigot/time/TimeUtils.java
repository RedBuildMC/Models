package fr.redbuild.models.spigot.time;

import fr.redbuild.models.spigot.plugin.PluginController;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class TimeUtils {
    public static BukkitTask countdown(int time, TimeUnit timeUnit, Consumer<Double> stepCallback,
                                       Runnable onEndCallback) {
        final AtomicReference<BukkitTask> bukkitTaskRef = new AtomicReference<>();
        // transfert en tick / secondes (20 ticks = 1 seconde)
        int tick = (int) timeUnit.toSeconds(time) * 20;
        AtomicInteger tickCounter = new AtomicInteger(tick);
        final BukkitTask bukkitTask = PluginController.INSTANCE.getServer()
                .getScheduler()
                .runTaskTimer(PluginController.INSTANCE, () -> {
                    if (tickCounter.get() == 0) {
                        onEndCallback.run();
                        bukkitTaskRef.get().cancel();
                        return;
                    }
                    stepCallback.accept(tickCounter.get() / 20D);
                    tickCounter.decrementAndGet();
                }, 0, 1);
        bukkitTaskRef.set(bukkitTask);
        return bukkitTask;
    }

    public static BukkitTask countdown(double tick,int time, TimeUnit timeUnit, Consumer<Double> stepCallback, Runnable onEndCallback) {
        final AtomicReference<BukkitTask> bukkitTaskRef = new AtomicReference<>();
        int ttick = (int) timeUnit.toSeconds(time) * ((int) tick);
        AtomicInteger tickCounter = new AtomicInteger(ttick);
        final BukkitTask bukkitTask = PluginController.INSTANCE.getServer()
                .getScheduler()
                .runTaskTimer(PluginController.INSTANCE, () -> {
                    if (tickCounter.get() == 0) {
                        onEndCallback.run();
                        bukkitTaskRef.get().cancel();
                        return;
                    }
                    stepCallback.accept((double) tickCounter.get() / tick);
                    tickCounter.decrementAndGet();
                }, 0, 1);
        bukkitTaskRef.set(bukkitTask);
        return bukkitTask;
    }

    public static BukkitTask timer(double tick,int time, TimeUnit timeUnit, Consumer<Double> stepCallback, Runnable onEndCallback) {
        final AtomicReference<BukkitTask> bukkitTaskRef = new AtomicReference<>();
        int ttick = (int) timeUnit.toSeconds(time) * ((int) tick);
        AtomicInteger tickCounter = new AtomicInteger(ttick);
        final BukkitTask bukkitTask = PluginController.INSTANCE.getServer()
                .getScheduler()
                .runTaskTimer(PluginController.INSTANCE, () -> {
                    if (tickCounter.get() == 0) {
                        onEndCallback.run();
                        bukkitTaskRef.get().cancel();
                        return;
                    }
                    stepCallback.accept((double) tickCounter.get() / tick);
                    tickCounter.decrementAndGet();
                }, 0, Double.doubleToLongBits(tick));
        bukkitTaskRef.set(bukkitTask);
        return bukkitTask;
    }

}

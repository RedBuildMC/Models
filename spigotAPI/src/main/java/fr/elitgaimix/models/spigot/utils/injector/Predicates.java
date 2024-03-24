package fr.elitgaimix.models.spigot.utils.injector;

import java.util.Objects;
import java.util.function.Predicate;

public class Predicates {
    private Predicates() {
    }

    /**
     * Always true
     *
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> alwaysTrue() {
        return (object) -> true;
    }

    public static <T> Predicate<T> nonNull() {
        return Objects::nonNull;
    }

    /**
     * Always false
     *
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> alwaysFalse() {
        return (object) -> false;
    }
}

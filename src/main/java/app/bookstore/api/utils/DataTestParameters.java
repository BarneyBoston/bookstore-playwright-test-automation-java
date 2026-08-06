package app.bookstore.api.utils;

import java.util.function.Function;

public record DataTestParameters<T, U, W>(String name,
                                                                    Function<T, W> responseMap,
                                                                    Function<U, W> dbMap) {
    @Override
    public String toString() {
        return name;
    }
}

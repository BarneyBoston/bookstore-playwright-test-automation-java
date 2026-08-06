package app.bookstore.api.utils;

import java.util.List;

@SuppressWarnings("unused")
public abstract class MatrixCreator {
    private MatrixCreator() {
        /* This utility class should not be instantiated */
    }

    public static Object[][] listToMatrix(List<?> list) {
        return list.stream()
                .map(entry -> new Object[]{entry})
                .toArray(Object[][]::new);
    }

    public static Object[][] arrayToMatrix(Object... array) {
        var output = new Object[array.length][1];

        for (int i = 0; i < array.length; i++) {
            output[i][0] = array[i];
        }

        return output;
    }

}
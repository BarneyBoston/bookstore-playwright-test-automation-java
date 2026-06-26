package app.bookstore.db.utils;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Record {

    private final Map<String, String> map = new HashMap<>();

    public void set(String column, String value) {
        map.put(column.toUpperCase(), value);
    }

    @Override
    public String toString() {
        return map.toString();
    }

    public boolean hasColumn(String column) {
        return map.containsKey(column.toUpperCase());
    }

    public String get(String column) {
        return map.get(column.toUpperCase());
    }
}

package repository.cache;


import model.annotations.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cache<ID, T> {
    private Map<ID, T> storage;

    public void save(List<T> entities) {
        this.storage = listToMap(entities);
    }

    public List<T> load() {
        System.out.println("Loaded from cache.");

        return mapToList();
    }

    public Map<ID, T> loadMap() {
        return storage;
    }

    public void invalidateCache() {
        storage = null;
    }

    public boolean hasResult() {
        return storage != null;
    }

    private List<T> mapToList() {
        return new ArrayList<>(storage.values());
    }

    private Map<ID, T> listToMap(List<T> list) {
        Map<ID, T> map = new HashMap<>();

        for (T t : list) {
            for (Field field : t.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Id.class)) {
                    try {
                        map.put((ID) field.get(t), t);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return map;
    }
}
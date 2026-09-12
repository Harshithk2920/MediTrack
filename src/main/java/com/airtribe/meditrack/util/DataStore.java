package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.MedicalEntity;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Generic thread-safe DataStore for storing and querying MedicalEntity instances.
 * Demonstrates Generics, Collections (Map & List), Thread Synchronization, Streams, and Serialization.
 *
 * @param <T> Entity extending MedicalEntity
 */
public class DataStore<T extends MedicalEntity> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<String, T> storageMap = new ConcurrentHashMap<>();

    public synchronized void add(T entity) {
        Validator.validateNotNull(entity, "Entity");
        if (entity.getId() == null || entity.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Entity ID cannot be null or empty.");
        }
        storageMap.put(entity.getId(), entity);
    }

    public synchronized Optional<T> getById(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(storageMap.get(id));
    }

    public synchronized List<T> getAll() {
        return new ArrayList<>(storageMap.values());
    }

    public synchronized boolean remove(String id) {
        if (id == null) return false;
        return storageMap.remove(id) != null;
    }

    public synchronized void clear() {
        storageMap.clear();
    }

    public synchronized int size() {
        return storageMap.size();
    }

    /**
     * Java 8 Stream API support for custom predicate filtering.
     */
    public synchronized List<T> filter(Predicate<T> predicate) {
        return storageMap.values().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Return custom stream for stream pipeline operations.
     */
    public synchronized Stream<T> stream() {
        return storageMap.values().stream();
    }

    /**
     * Sort records using a custom Comparator.
     */
    public synchronized List<T> getSorted(Comparator<T> comparator) {
        return storageMap.values().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /**
     * Save DataStore contents to file via Native Java Binary Serialization.
     */
    public synchronized void saveToFile(String filePath) throws IOException {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(storageMap);
        }
    }

    /**
     * Load DataStore contents from file via Serialization.
     */
    @SuppressWarnings("unchecked")
    public synchronized void loadFromFile(String filePath) throws IOException, ClassNotFoundException {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Map<String, T> loadedMap = (Map<String, T>) ois.readObject();
            storageMap.clear();
            storageMap.putAll(loadedMap);
        }
    }
}

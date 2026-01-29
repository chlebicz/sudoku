package pl.komponentowe.model;

import java.util.List;

/**
 * Generic interface for Data Access Objects.
 * @param <T> The type of object to be handled by the DAO
 */
public interface Dao<T> extends AutoCloseable {
    /**
     * Reads an object with the given name.
     * @param name Name of the object to read
     * @return The read object
     */
    T read(String name);

    /**
     * Writes an object with the given name.
     * @param name Name under which the object will be saved
     * @param obj The object to save
     */
    void write(String name, T obj);

    /**
     * Returns a list of names of all saved objects.
     * @return List of names
     */
    List<String> names();
}

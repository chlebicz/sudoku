package pl.komponentowe.model;

/**
 * Generic repository interface.
 * @param <T> The type of object to be handled by the Repository
 */
public interface Repository<T> {
    /**
     * Creates a new object in the repository.
     * @param t object
     * @return object
     */
    T create(T t);

    /**
     * Reads an object from the repository.
     * @param id identifier
     * @return object
     */
    T read(String id);

    /**
     * Updates an object in the repository.
     * @param t object
     * @return object
     */
    T update(T t);

    /**
     * Deletes an object from the repository.
     * @param id identifier
     */
    void delete(String id);
}

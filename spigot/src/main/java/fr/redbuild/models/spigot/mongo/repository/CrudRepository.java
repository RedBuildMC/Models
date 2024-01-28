package fr.redbuild.models.spigot.mongo.repository;
    import java.util.List;
    import java.util.Optional;
public interface CrudRepository<T, ID> {

        /**
         * Returns the number of entities
         *
         * @return the number of entities
         */
        long count();

        /**
         * remove an entity
         *
         * @param entity
         */
        void delete(T entity);

        /**
         * Remove all entities
         */
        void deleteAll();

        /**
         * Remove entities contained in the collection
         *
         * @param entities
         */
        void deleteAll(List<? extends T> entities);

        /**
         * Remove an entity by it's id
         *
         * @param id
         */
        void deleteById(ID id);


        /**
         * Check if an entity already exists by it's id
         *
         * @param id
         * @return
         */
        boolean existsById(ID id);

        /**
         * Find an entity by it's ID
         *
         * @param id
         * @return
         */
        Optional<T> findById(ID id);


        /**
         * Get all entities
         *
         * @return
         */
        List<T> findAll();


        /**
         * Save an entity to a database
         *
         * @param <S>
         * @param entity
         * @return
         */
        <S extends T> boolean save(S entity);


        /**
         * Save all entites to a database
         *
         * @param <S>
         * @param entities
         * @return
         */
        <S extends T> boolean saveAll(List<S> entities);

    }

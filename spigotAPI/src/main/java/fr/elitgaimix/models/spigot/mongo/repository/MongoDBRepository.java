package fr.elitgaimix.models.spigot.mongo.repository;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import fr.elitgaimix.models.spigot.plugin.PluginController;
import fr.elitgaimix.models.spigot.utils.mongo.Id;

import org.bson.Document;
import org.bson.conversions.Bson;
/**
 * This class represents a MongoDB repository implementation for CRUD operations.
 * It provides methods to interact with a MongoDB collection and perform operations such as
 * saving, deleting, finding, and counting entities.
 *
 * @param <T>  the type of the entity
 * @param <ID> the type of the entity's ID
 */
public class MongoDBRepository<T, ID> implements CrudRepository<T, ID> {

    private final MongoCollection<T> collection;


    public MongoDBRepository(Class<T> dataClass, String collectionName, String dbName) {
        MongoClient client = MongoClients.create(PluginController.INSTANCE.getConfig().getString("database.host"));
        MongoDatabase db = client.getDatabase(dbName);
        this.collection = db.getCollection(collectionName, dataClass);
    }


    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(T entity) {
        collection.withCodecRegistry(PluginController.codecController.getCodecRegistries()).deleteOne(Filters.eq("_id",getId(entity)));
    }

    @Override
    public void deleteAll() {
        collection.withCodecRegistry(PluginController.codecController.getCodecRegistries()).deleteMany(new Document());
    }

    @Override
    public void deleteAll(List<? extends T> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteById(ID id) {
        collection.withCodecRegistry(PluginController.codecController.getCodecRegistries()).deleteOne(Filters.eq("_id", id));
    }

    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    @Override
    public Optional<T> findById(ID id) {
        for (T object : findAll()) {
            if (getId(object) == null){
                continue;
            }
            if (getId(object).equals(id)) {
                return Optional.of(object);
            }
        }
        return findAll().stream().filter(obj -> getId(obj).equals(id)).findAny();
    }

    @Override
    public List<T> findAll() {
        List<T> result = new ArrayList<>();
        for (T object : this.collection.withCodecRegistry(PluginController.codecController.getCodecRegistries()).find()) {
            result.add(object);
        }
        return result;
    }

    @Override
    public <S extends T> boolean save(S entity) {
            var id = getId(entity);
            if(id == null) {
                var result = collection.withCodecRegistry(PluginController.codecController.getCodecRegistries()).insertOne(entity);
                return result.wasAcknowledged();
            }else{
                Bson filter = Filters.eq("_id", id);
                var result = collection.withCodecRegistry(PluginController.codecController.getCodecRegistries()).replaceOne(filter, entity, new ReplaceOptions().upsert(true));
                return result.wasAcknowledged();
            }
    }

    @Override
    public <S extends T> boolean saveAll(List<S> entities) {
        return entities.stream()
                .map(this::save)
                .filter(Boolean.FALSE::equals)
                .findFirst().orElse(true);
    }

    public <S extends T> Object getId(S entity) {
        Object id = null;
        try {
            for (Field f : entity.getClass().getDeclaredFields()) {
                if (f.isAnnotationPresent(Id.class)) {
                    f.setAccessible(true);
                    id = f.get(entity);
                    f.setAccessible(false);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return id;
    }

}
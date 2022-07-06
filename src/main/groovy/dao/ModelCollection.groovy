package dao

import com.mongodb.async.client.FindIterable
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import util.ModelMapper

import java.util.concurrent.CompletableFuture

class ModelCollection<T> extends DocumentCollection {
    final ModelMapper<T> MAPPER

    ModelCollection(String uri, Class<? extends ModelMapper<T>> mapperClass) {
        this(uri, mapperClass.newInstance())
    }

    ModelCollection(String uri, ModelMapper<T> mapper) {
        super(uri)
        this.MAPPER = mapper
    }

    ModelFindIterable<T> findModels(){
        return new ModelFindIterable<T>(COLLECTION.find() as FindIterable<Document>, MAPPER)
    }

    CompletableFuture<T> getModel(ObjectId id) {
        return findModel(id).first()
    }

    ModelFindIterable<T> findModel(ObjectId id) {
        return new ModelFindIterable<T>(find(id), MAPPER)
    }

    CompletableFuture<T> insertOneModel(T model) {
        Document document = MAPPER.toDocument(model)
        return insertOne(document).thenApply({ MAPPER.toModel(it) })
    }

    CompletableFuture<List<T>> insertManyModel(List<T> models) {
        List<Document> documents = MAPPER.toDocuments(models)
        return insertMany(documents).thenApply({ MAPPER.toModels(it) })
    }

    CompletableFuture<T> findOneAndUpdateModel(Bson filter, Bson update) {
        return findOneAndUpdate(filter, update).thenApply({ MAPPER.toModel(it) })
    }

    CompletableFuture<T> findOneAndDeleteModel(Bson filter) {
        return findOneAndDelete(filter).thenApply({ MAPPER.toModel(it) })
    }
}

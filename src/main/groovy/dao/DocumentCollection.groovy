package dao

import com.mongodb.ConnectionString
import com.mongodb.async.client.MongoClient
import com.mongodb.async.client.MongoClients
import com.mongodb.async.client.MongoCollection
import com.mongodb.async.client.MongoDatabase
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import org.apache.commons.lang3.Validate
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.types.ObjectId

import javax.print.Doc
import java.util.concurrent.CompletableFuture

class DocumentCollection {
    final MongoCollection<Document> COLLECTION

    DocumentCollection(String uri) {
        Validate.notBlank(uri, "brokeUrl must not be blank")

        ConnectionString connectionString = new ConnectionString(uri)
        Validate.notBlank(connectionString.database, "database name must be not blank in URI")
        Validate.notBlank(connectionString.collection, "collection name must be not blank in URI")

        MongoClient mongoClient = MongoClients.create(uri)
        MongoDatabase mongoDatabase = mongoClient.getDatabase(connectionString.database)
        this.COLLECTION = mongoDatabase.getCollection(connectionString.collection)
    }

    CompletableFuture<Document> getFirst(ObjectId id) {
        return find(id).first()
    }

    DocumentFindIterable find(ObjectId id) {
        Validate.notNull(id, "Id must not be null")
        return find(new Document("_id", id))
    }

    DocumentFindIterable find(Bson filter) {
        return new DocumentFindIterable(COLLECTION.find(filter))
    }

    CompletableFuture<Document> insertOne(Document document) {
        CompletableFuture<Document> future = new MongoCompletableFuture<>()
        COLLECTION.insertOne(document, { Void avoid, Throwable throwable -> future.onResult(document, throwable) })
        return future
    }

    CompletableFuture<Document> findOneAndUpdate(Bson filter, Bson update) {
        CompletableFuture<Document> future = new MongoCompletableFuture<>()
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions(returnDocument: ReturnDocument.AFTER)
        COLLECTION.findOneAndUpdate(filter, update, options, future)
        return future
    }

    CompletableFuture<Document> findOndAndDelete(Bson filter) {
        CompletableFuture<Document> future = new MongoCompletableFuture<>()
        COLLECTION.findOneAndDelete(filter, future)
        return future
    }
}

package dao

import com.mongodb.async.client.FindIterable
import org.bson.Document
import org.bson.conversions.Bson

import java.util.concurrent.CompletableFuture

class DocumentFindIterable {

    final FindIterable<Document> ITERABLE

    DocumentFindIterable(FindIterable iterable) {
        this.ITERABLE = iterable
    }

    CompletableFuture<Document> first() {
        CompletableFuture<Document> future = new MongoCompletableFuture<>()
        ITERABLE.first(future)
        return future
    }

    CompletableFuture<List<Document>> intoDocuments() {
        MongoCompletableFuture<List<Document>> future = new MongoCompletableFuture<>()
        ITERABLE.into(new ArrayList<Document>(), future)
        return future
    }

    DocumentFindIterable projection(Bson projection) {
        ITERABLE.projection(projection)
        return this
    }
}

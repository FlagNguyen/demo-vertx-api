package dao

import com.mongodb.async.client.FindIterable
import org.bson.Document

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
}

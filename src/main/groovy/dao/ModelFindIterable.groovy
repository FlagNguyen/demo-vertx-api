package dao

import com.mongodb.async.client.FindIterable
import org.bson.Document
import util.ModelMapper

import java.util.concurrent.CompletableFuture

class ModelFindIterable<T> {
    private FindIterable<Document> ITERABLE
    private final ModelMapper<T> MAPPER

    ModelFindIterable(DocumentFindIterable findIterable, ModelMapper<T> mapper) {
        this(findIterable.ITERABLE, mapper)
    }

    ModelFindIterable(FindIterable<Document> iterable, ModelMapper<T> mapper) {
        this.ITERABLE = iterable
        this.MAPPER = mapper
    }

    CompletableFuture<T> first() {
        return this.firstDocument().thenApply({ document -> MAPPER.toModel(document) })
    }

    CompletableFuture<Document> firstDocument() {
        CompletableFuture<Document> future = new MongoCompletableFuture<>()
        ITERABLE.first(future)
        return future
    }
}

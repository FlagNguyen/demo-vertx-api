package dao

import com.mongodb.async.SingleResultCallback

import java.util.concurrent.CompletableFuture

class MongoCompletableFuture<T> extends CompletableFuture<T> implements SingleResultCallback<T> {
    @Override
    void onResult(T result, Throwable throwable) {
        if (throwable != null) {
            this.completeExceptionally(throwable)
            return
        }

        try {
            this.complete(result)
        } catch (e) {
            this.completeExceptionally(e)
        }
    }
}

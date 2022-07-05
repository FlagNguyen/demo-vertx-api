package util

import org.bson.Document

import java.util.stream.Collectors

abstract class ModelMapper<T> {
    abstract T toModel(Document document)

    abstract Document toDocument(T model)

    final List<Document> toDocuments(List<T> models) {
        if (models == null) {
            return new ArrayList<Document>(0)
        }

        return models.stream().map(this.&toDocument).collect(Collectors.toList())
    }

    final List<T> toModels(List<Document> documents) {
        if (documents == null) {
            return new ArrayList<T>(0)
        }

        return documents.stream().map(this.&toModel).collect(Collectors.toList())
    }

}

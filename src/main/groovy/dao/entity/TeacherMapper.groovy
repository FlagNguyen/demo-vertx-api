package dao.entity

import util.ModelMapper
import org.bson.Document

class TeacherMapper extends ModelMapper<Teacher> {

    @Override
    Teacher toModel(Document document){
        if (document == null){
            return null
        }

        Teacher model = new Teacher(
                teacherID: document._id,
                teacherName: document.teacherName,
                email: document.email
        )
        return model
    }

    @Override
    Document toDocument(Teacher model){
        if(model == null){
            return null
        }

        Document document = new Document()
        if(model.teacherID){
            document.put("_id", model.teacherID)
        }

        document.putAll([
                teacherName: model.teacherName,
                email: model.email
        ])
        return document
    }
}

package dao.entity


import dao.ModelCollection

class TeacherCollection extends ModelCollection<Teacher> {
    TeacherCollection(String uri) {
        super(uri, TeacherMapper.class)
    }
}

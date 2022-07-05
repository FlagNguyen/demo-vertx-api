package entity

class Teacher {
    private String teacherID
    private String teacherName
    private String email

    Teacher() {
    }

    Teacher(String teacherID, String teacherName, String email) {
        this.teacherID = teacherID
        this.teacherName = teacherName
        this.email = email
    }

    String getTeacherID() {
        return teacherID
    }

    void setTeacherID(String teacherID) {
        this.teacherID = teacherID
    }

    String getTeacherName() {
        return teacherName
    }

    void setTeacherName(String teacherName) {
        this.teacherName = teacherName
    }

    String getEmail() {
        return email
    }

    void setEmail(String email) {
        this.email = email
    }


    @Override
    public String toString() {
        return "Teacher{" +
                "teacherID='" + teacherID + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

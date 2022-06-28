package entity;

class Subject {
    private String subjectID
    private String subjectName
    private int credits

    Subject(String subjectID, String subjectName, int credits) {
        this.subjectID = subjectID
        this.subjectName = subjectName
        this.credits = credits
    }

    String getId() {
        return subjectID
    }

    String getSubjectName() {
        return subjectName
    }

    int getCredits() {
        return credits
    }

    void setId(String id) {
        this.subjectID = id
    }

    void setSubjectName(String subjectName) {
        this.subjectName = subjectName
    }

    void setCredits(int credits) {
        this.credits = credits
    }
}

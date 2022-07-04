package util

import dao.entity.Teacher
import org.apache.commons.validator.routines.EmailValidator

import java.util.stream.Collectors

class Validator {

    /**
     * Method validate input new teacher object
     * @validation: ID not duplicate & follow email format
     * @param inputTeacherModel
     * @return List error key messages
     */
    static List validateTeacherRequestAndReturnMessage(Teacher inputTeacherModel) {
        List outputErrorMessages = new ArrayList()

        //Get teacher ID list for checking duplicate
        List<String> teacherIDList = SampleTeacherData.TEACHER_BY_ID.values()
                .stream()
                .map({ teacher -> teacher.teacherID })
                .collect(Collectors.toList())

        if (teacherIDList.contains(inputTeacherModel.getTeacherID())) {
            outputErrorMessages.add("DuplicateID")
        }

        if (!EmailValidator.getInstance().isValid(inputTeacherModel.getEmail())) {
            outputErrorMessages.add("WrongFormatEmail")
        }

        return outputErrorMessages
    }

}

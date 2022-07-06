package util

import dao.entity.Teacher
import org.apache.commons.lang3.Validate
import org.apache.commons.validator.routines.EmailValidator

class Validator {

    /**
     * Method validate input new teacher object
     * @validation: ID not duplicate & follow email format
     * @param inputTeacherModel
     * @return List error key messages
     */
    static List validateTeacherRequestAndReturnMessage(Teacher inputTeacherModel) {
        List outputErrorMessages = new ArrayList()

        if (!Validate.notBlank(inputTeacherModel.teacherName)
                || !Validate.notBlank(inputTeacherModel.email)) {
            outputErrorMessages.add("LackField")
        }
        if (!EmailValidator.getInstance().isValid(inputTeacherModel.email)) {
            outputErrorMessages.add("WrongFormatEmail")
        }

        return outputErrorMessages
    }

}

package util


import dao.entity.Teacher
import dao.entity.TeacherCollection
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.Validate
import org.apache.commons.validator.routines.EmailValidator
import org.bson.types.ObjectId

class Validator {

    /**
     * Method validate input new teacher object
     * @validation: ID not duplicate & follow email format
     * @param inputTeacherModel
     * @return List error key messages
     */
    static void validateTeacherRequestAndReturnMessage(Teacher inputTeacherModel) {

        //Lack of fields
        Validate.isTrue(StringUtils.isNotBlank(inputTeacherModel.teacherName) &&
                StringUtils.isNotBlank(inputTeacherModel.email), "Lack of required fields")

        //Validate email format
        Validate.isTrue(EmailValidator.getInstance().isValid(inputTeacherModel.email), "Wrong email format")


    }

    /**
     *
     * @param teacherIdParam
     * @return true if this teacher existed and false if it not
     */
    static boolean checkExistedTeacher(ObjectId teacherIdParam, TeacherCollection collection) {
        //Get teacher object in database which has input id parameter
        Teacher foundTeacher = collection.getModel(teacherIdParam).join()

        //Check this teacher is existed or not
        if (foundTeacher == null) {
            return false
        }
        return true
    }

}

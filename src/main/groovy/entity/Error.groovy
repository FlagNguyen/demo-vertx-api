package entity

class Error {
    private String errorCode
    private String errorMessage

    Error() {
    }

    Error(String errorCode, String errorMessage) {
        this.errorCode = errorCode
        this.errorMessage = errorMessage
    }

    String getErrorCode() {
        return errorCode
    }

    void setErrorCode(String errorCode) {
        this.errorCode = errorCode
    }

    String getErrorMessage() {
        return errorMessage
    }

    void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage
    }
}

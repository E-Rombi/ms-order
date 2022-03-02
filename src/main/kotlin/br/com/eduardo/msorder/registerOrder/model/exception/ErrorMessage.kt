package br.com.eduardo.msorder.registerOrder.model.exception

data class ErrorMessage(
    val message: String?
)

data class FieldErrorMessage(
    val field: String?,
    val message: String?
)


object Messages {
    fun conditionCheckFailed(): String = "Error, please verify the content"
    fun resourceNotFound(resource: String): String = "$resource not found"
}
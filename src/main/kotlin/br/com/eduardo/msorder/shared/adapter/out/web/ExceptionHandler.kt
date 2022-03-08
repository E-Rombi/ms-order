package br.com.eduardo.msorder.shared.adapter.out.web

import br.com.eduardo.msorder.shared.model.exception.ErrorMessage
import br.com.eduardo.msorder.shared.model.exception.FieldErrorMessage
import br.com.eduardo.msorder.shared.model.exception.GenericException
import br.com.eduardo.msorder.shared.model.exception.ResourceNotFoundException
import org.hibernate.validator.internal.engine.path.PathImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.validation.ConstraintViolationException

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler
    fun handleConstraintViolation(e: ConstraintViolationException): ResponseEntity<List<FieldErrorMessage>> {
        val response = e.constraintViolations.map {
            FieldErrorMessage((it.propertyPath as PathImpl).leafNode.toString(), it.message)
        }

        return ResponseEntity.badRequest().body(response)
    }

    @ExceptionHandler
    fun handleResourceNotFound(e: ResourceNotFoundException): ResponseEntity<ErrorMessage> {
        return ResponseEntity.notFound().build()
    }

    @ExceptionHandler
    fun handleGeneric(e: GenericException): ResponseEntity<ErrorMessage> {
        return ResponseEntity.internalServerError().body(ErrorMessage(e.message))
    }
}
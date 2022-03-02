package br.com.eduardo.msorder.registerOrder.adapter.out.web

import br.com.eduardo.msorder.registerOrder.model.exception.FieldErrorMessage
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException
import org.hibernate.validator.internal.engine.path.PathImpl
import org.slf4j.LoggerFactory
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
}
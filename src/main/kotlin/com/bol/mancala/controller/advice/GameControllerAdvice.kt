package com.bol.mancala.controller.advice

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.time.Instant

@ControllerAdvice
class GameControllerAdvice {

    @ExceptionHandler(RuntimeException::class)
    fun noStones(ex: RuntimeException, req: WebRequest): ResponseEntity<Any> {
        val map = mapOf("timestamp" to Instant.now().toEpochMilli(),
                        "message" to ex.localizedMessage)

        return ResponseEntity(map, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun messageNotReadable(ex: HttpMessageNotReadableException, req: WebRequest): ResponseEntity<Any> {
        val message = when (val cause = ex.cause) {
            is InvalidFormatException -> ex.mostSpecificCause.localizedMessage

            is MissingKotlinParameterException ->
                "json request not valid: ${cause.parameter.name} is required"

            is UnrecognizedPropertyException -> "json request contains an unknown field '${cause.propertyName}'"

            is MismatchedInputException -> {
                val type = cause.targetType.simpleName.toLowerCase()
                val fieldName = cause.path[0].fieldName

                "json request not valid: $fieldName must be of type $type"
            }

            else                               ->
                "json request not valid: ${ex.localizedMessage.split(":")[0].toLowerCase()}"
        }

        val map = mapOf("timestamp" to Instant.now().toEpochMilli(),
                        "message" to message)

        return ResponseEntity(map, HttpStatus.BAD_REQUEST)
    }

}

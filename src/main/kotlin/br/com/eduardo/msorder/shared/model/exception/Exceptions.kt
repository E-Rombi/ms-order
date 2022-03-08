package br.com.eduardo.msorder.shared.model.exception

class ResourceNotFoundException(message: String) : RuntimeException(message)

class GenericException(message: String) : RuntimeException(message)

class ConfirmationOrderException(message: String) : RuntimeException(message)
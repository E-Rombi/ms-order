package br.com.eduardo.msorder.registerOrder.model.exception

class ResourceNotFoundException(message: String) : RuntimeException(message)

class GenericException(message: String) : RuntimeException(message)
package module.engine.exception

class NoKeyException(message: String) : Exception(message) {
    constructor() : this("No such key")
}
package module.engine.exception

class NoPalletException(message: String) : Exception(message) {
    init {
        println("NoPalletException: $message")
    }
}
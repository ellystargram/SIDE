package module.commandbar.exceptions

class InvalidCommandException(val problemCommand: String, val description: String) : Exception() {
    override val message: String?
        get() = "Invalid command"
}
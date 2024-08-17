package module.commandbar.exceptions

class DebugException(val problemCommand: String, val description: String) : Exception() {
    override val message: String?
        get() = "Debug command"
}
package module.commandbar.exceptions

class FileException(val problemCommand: String, val description:String) : Exception() {
    override val message: String?
        get() = "FileException: $description\n$problemCommand"
}
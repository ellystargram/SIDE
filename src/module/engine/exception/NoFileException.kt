package module.engine.exception

class NoFileException(private val msg:String?): Exception() {
    override val message: String?
        get() = msg ?: "No such file"
}
package module.engine.exception

class UnknownJsonTypeException(private val msg:String?): Exception() {
    override val message: String?
        get() = msg ?: "Unknown JSON type"
}
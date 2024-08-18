package module.engine.exception

class DamagedSystemConfigException(message: String) : Exception(message) {
    constructor() : this("Damaged system configuration")
}
package module.display

import module.engine.json.JsonObject
import javax.swing.JFrame

class Window(val settings: JsonObject): JFrame() {
    init {
        title = "SIDE: Venus"
        setSize(800, 600)
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
    }
}
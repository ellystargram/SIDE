package module.display

import module.engine.json.JsonObject
import javax.swing.JFrame

class Window(val settings: JsonObject): JFrame() {
    init {
        title = settings.getPrimitiveAsString("ideWindow.title")
        val width = settings.getPrimitiveAsInt("ideWindow.width")
        val height = settings.getPrimitiveAsInt("ideWindow.height")
        setSize(width, height)
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
    }
}
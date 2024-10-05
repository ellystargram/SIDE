package module.engine.terminal

import module.display.terminalPane.TerminalTabPane
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import javax.swing.SwingUtilities



class SystemTerminal() : Terminal {
    var process:Process? = null
    var writer:OutputStream? = null
    var reader:BufferedReader? = null

    init {
        open()
    }

    override var terminalTab: TerminalTabPane.Tab? = null

    override fun executeCommand(command: String) {
        try {
            SwingUtilities.invokeLater {
                terminalTab?.log?.append(">$command\n")
            }
            writer?.write((command + "\n").toByteArray())
            writer?.flush()
        } catch (e: Exception) {
            SwingUtilities.invokeLater {
                terminalTab?.log?.append("Error: ${e.message}\n")
            }
        }
    }

    override fun close() {
        process?.destroy()
    }

    override fun open() {
        val shellCommand = getBasicShellName()
        process = ProcessBuilder(*shellCommand).redirectErrorStream(true).start()
        if (process == null) {
            return
        }
        writer = process!!.outputStream
        reader = BufferedReader(InputStreamReader(process!!.inputStream))
        Thread {
            reader?.lines()?.forEach { line ->
                SwingUtilities.invokeLater {
                    terminalTab?.log?.append("$line\n")
                }
            }
        }.start()
    }

}

fun getBasicShellName(): Array<String> {
    return when {
        System.getProperty("os.name").lowercase().contains("windows") -> arrayOf("cmd")
        System.getProperty("os.name").lowercase().contains("linux") -> arrayOf("bash")
        System.getProperty("os.name").lowercase().contains("mac") -> arrayOf("bash")
        else -> arrayOf("sh", "-c")
    }
}

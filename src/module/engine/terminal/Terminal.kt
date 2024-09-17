package module.engine.terminal

import java.io.BufferedReader
import java.io.InputStreamReader
import javax.swing.JTextArea
import javax.swing.SwingUtilities

class Terminal(private val outputField: JTextArea) {
    var shellCommand = getBasicShellName()
    var process = ProcessBuilder(*shellCommand).redirectErrorStream(true).start()
    val writer = process.outputStream
    val reader = BufferedReader(InputStreamReader(process.inputStream))
    init {
        Thread {
            reader.lines().forEach { line ->
                SwingUtilities.invokeLater {
                    outputField.append("$line\n")
                }
            }
        }.start()
    }

    fun executeCommand(command: String) {
        try{
            SwingUtilities.invokeLater {
                outputField.append(">$command\n")
            }
            writer.write((command+"\n").toByteArray())
            writer.flush()
        } catch (e: Exception) {
//            System.getProperty("os.name").lowercase().contains("windows") -> arrayOf("cmd")
//            System.getProperty("os.name").lowercase().contains("linux") -> arrayOf("bash")
//            System.getProperty("os.name").lowercase().contains("mac") -> arrayOf("bash")
//            else -> arrayOf("sh", "-c")
            SwingUtilities.invokeLater {
                outputField.append("Error: ${e.message}\n")
            }
        }
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
//class Terminal {
//fun executeTerminalCommand(command: String, outputField: JTextArea) {
//    Thread {
////        try {
////            val process = ProcessBuilder(*command.split(" ").toTypedArray()).redirectErrorStream(true).start()
////            BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
////                reader.lines().forEach { line ->
////                    SwingUtilities.invokeLater {
////                        outputField.append("$line\n")
////                    }
////                }
////            }
////            process.waitFor()
////        } catch (e: Exception) {
////            SwingUtilities.invokeLater {
////                outputField.append("Error: ${e.message}\n")
////            }
////        }
//    }.start()
//}
////}
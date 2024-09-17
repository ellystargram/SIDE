package module.engine.font

import java.awt.Font
import java.awt.FontFormatException
import java.awt.GraphicsEnvironment
import java.io.File
import java.io.IOException

class FontManager {
    init {
        uploadFont("/resources/fonts/")
    }
    fun uploadFont(fontPath: String) {
        try {
            val resource = object {}.javaClass.getResource(fontPath)
            if (resource != null) {
                val uri = resource.toURI()
                val fontDir = File(uri)

                if (fontDir.isDirectory) {
                    val files = fontDir.listFiles { _, name -> name.endsWith(".ttf") || name.endsWith(".TTF") }
                    if (files != null) {
                        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
                        for (file in files) {
                            try {
                                object {}.javaClass.getResourceAsStream("$fontPath/${file.name}").use { stream ->
                                    val font = Font.createFont(Font.TRUETYPE_FONT, stream)
                                    ge.registerFont(font)
                                    println("Registered font: ${font.name}")
                                }
                            } catch (e: FontFormatException) {
                                println("폰트 형식 오류: ${e.message}")
                            } catch (e: IOException) {
                                println("폰트 파일을 읽는 중 오류 발생: ${e.message}")
                            }
                        }
                    } else {
                        println("디렉토리에 폰트 파일이 없음")
                    }
                } else {
                    println("디렉토리가 존재하지 않거나 유효하지 않음")
                }
            } else {
                println("리소스 경로를 찾을 수 없음: $fontPath")
            }
        } catch (e: Exception) {
            println("폰트 로드 중 오류 발생: ${e.message}")
        }
    }
}


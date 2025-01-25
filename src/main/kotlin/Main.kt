import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import layout.os.MacLayout
import layout.os.WindowsLayout

//@Composable
//@Preview
//fun App() {
//    var text by remember { mutableStateOf("Hello, World!") }
//
//    MaterialTheme {
//        Button(onClick = {
//            text = "Hello, Desktop!"
//        }) {
//            Text(text)
//        }
//    }
//}
//
//fun main() = application {
//    Window(onCloseRequest = ::exitApplication) {
//        App()
//    }
//}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "SIDE Earth") {
        if (System.getProperty("os.name").contains("Windows")) {
            WindowsLayout()
        } else if (System.getProperty("os.name").contains("Mac")) {
            MacLayout()
        }
        else {
            val os = System.getProperty("os.name")
            TODO("$os is Not supported yet")
        }
    }
}

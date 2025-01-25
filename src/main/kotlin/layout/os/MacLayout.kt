package layout.os

import androidx.compose.runtime.Composable
import layout.ide.IDELayout

@Composable
fun MacLayout() {
    IDELayout {
        north { }
        south { }
        west { }
        east { }
        center { }
    }
}
package layout.ide

import androidx.compose.runtime.Composable

class IDELayoutScopeImpl: IDELayoutScope {
    var northComposable: (@Composable () -> Unit)? = null
    var southComposable: (@Composable () -> Unit)? = null
    var westComposable: (@Composable () -> Unit)? = null
    var eastComposable: (@Composable () -> Unit)? = null
    var centerComposable: (@Composable () -> Unit)? = null

    override fun north(composable: @Composable () -> Unit) {
        northComposable = composable
    }

    override fun south(composable: @Composable () -> Unit) {
        southComposable = composable
    }

    override fun west(composable: @Composable () -> Unit) {
        westComposable = composable
    }

    override fun east(composable: @Composable () -> Unit) {
        eastComposable = composable
    }

    override fun center(composable: @Composable () -> Unit) {
        centerComposable = composable
    }
}
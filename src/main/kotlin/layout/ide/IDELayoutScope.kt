package layout.ide

import androidx.compose.runtime.Composable

interface IDELayoutScope {
    fun north(composable: @Composable () -> Unit)
    fun south(composable: @Composable () -> Unit)
    fun west(composable: @Composable () -> Unit)
    fun east(composable: @Composable () -> Unit)
    fun center(composable: @Composable () -> Unit)
}
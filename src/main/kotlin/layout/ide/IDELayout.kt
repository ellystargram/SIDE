package layout.ide

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun IDELayout(
    composable: IDELayoutScope.() -> Unit
) {
    val scope = remember { IDELayoutScopeImpl() }
    scope.composable()

    Box(modifier = Modifier.fillMaxSize()) {
        scope.northComposable?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.DarkGray)
                    .border(1.dp, Color.Black)
                    .align(Alignment.TopCenter)
            ) {
                /* North | Top */
                Box {
                    Text("North")
                    it()
                }
            }
        }

        scope.southComposable?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.DarkGray)
                    .border(1.dp, Color.Black)
                    .align(Alignment.BottomCenter)
            ) {
                /* South | Bottom */
                Box {
                    Text("South")
                    it()
                }
            }
        }

        scope.westComposable?.let {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(200.dp)
                    .padding(
                        top = if (scope.northComposable != null) 50.dp else 0.dp, //top north
                        bottom = if (scope.southComposable != null) 50.dp else 0.dp //bottom south
                    )
                    .background(Color.DarkGray)
                    .border(1.dp, Color.Black)
                    .align(Alignment.CenterStart)
            ) {
                /* West | Left */
                Box{
                    Text("West")
                    it()
                }
            }
        }

        scope.eastComposable?.let {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(200.dp)
                    .padding(
                        top = if (scope.northComposable != null) 50.dp else 0.dp, //top north
                        bottom = if (scope.southComposable != null) 50.dp else 0.dp //bottom south
                    )
                    .background(Color.DarkGray)
                    .border(1.dp, Color.Black)
                    .align(Alignment.CenterEnd)
            ) {
                /* East | Right */
                Box {
                    Text("East")
                    it()
                }
            }
        }

//        scope.centerComposable?.let {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(top = 50.dp, bottom = 50.dp, start = 50.dp, end = 50.dp)
//                    .background(Color.White)
//                    .align(Alignment.Center)
//            ) {
//                /* Center */
//                it()
//            }
//        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = if (scope.westComposable != null) 200.dp else 0.dp, //left west
                    end = if (scope.eastComposable != null) 200.dp else 0.dp, //right east
                    top = if (scope.northComposable != null) 50.dp else 0.dp, //top north
                    bottom = if (scope.southComposable != null) 50.dp else 0.dp //bottom south
                )
                .background(Color.White)
        ) {
            /* Center */
            Box{
                Text("Center")
                scope.centerComposable?.invoke()
            }
        }

    }

}
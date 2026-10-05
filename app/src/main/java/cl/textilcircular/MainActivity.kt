package cl.textilcircular

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cl.textilcircular.ui.theme.TextilCircularTheme
import cl.textilcircular.view.MainScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            TextilCircularTheme {

                MainScreen()

            }
        }
    }
}
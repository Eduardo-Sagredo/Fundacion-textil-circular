package com.example.fundaciontextilcircular

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fundaciontextilcircular.data.local.TextilDatabase
import com.example.fundaciontextilcircular.data.local.dao.EmprendedoraDao
import com.example.fundaciontextilcircular.data.repository.EmprendedoraRepository
import com.example.fundaciontextilcircular.ui.theme.FundacionTextilCircularTheme
import com.example.fundaciontextilcircular.ui.view.TestScreen
import com.example.fundaciontextilcircular.ui.viewmodel.TestViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = TextilDatabase.getDatabase(this)

        val repository = EmprendedoraRepository(database.emprendedoraDao())

        val viewModel = TestViewModel(repository)

        enableEdgeToEdge()
        setContent {
            FundacionTextilCircularTheme {
                TestScreen(viewModel = viewModel)
            }

        }
    }
}

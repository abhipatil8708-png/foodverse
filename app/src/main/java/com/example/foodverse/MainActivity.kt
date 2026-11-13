package com.example.foodverse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.foodverse.presentation.navigation.FoodVerseNavGraph
import com.example.foodverse.ui.theme.FoodVerseBackground
import com.example.foodverse.ui.theme.FoodverseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodverseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = FoodVerseBackground
                ) {
                    FoodVerseNavGraph()
                }
            }
        }
    }
}

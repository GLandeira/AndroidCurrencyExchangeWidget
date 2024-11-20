package com.example.currencyexchange

import android.os.Bundle
import android.util.Log
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
import androidx.work.ExistingPeriodicWorkPolicy
import com.example.currencyexchange.ui.theme.CurrencyExchangeTheme
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import androidx.work.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyExchangeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Chiara <3",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Require network connectivity
            .build()

        // Feature flags
        val inputData = Data.Builder()
            .putBoolean("useAPI", true) // Pass your toggle flag
            .build()

        // Schedule a one-time work request to run immediately
        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<CurrencyFetchWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "CurrencyFetchWork",
            ExistingWorkPolicy.REPLACE,
            oneTimeWorkRequest
        )

        // Schedule periodic work
        val workRequest = PeriodicWorkRequestBuilder<CurrencyFetchWorker>(0, TimeUnit.HOURS)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "CurrencyFetchWork",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            workRequest
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CurrencyExchangeTheme {
        Greeting("Chiara <3")
    }
}
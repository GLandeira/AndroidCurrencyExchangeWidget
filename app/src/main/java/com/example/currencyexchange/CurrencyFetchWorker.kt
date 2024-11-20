package com.example.currencyexchange

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class CurrencyFetchWorker(context: Context, workerParams: WorkerParameters, useAPI: Boolean) : Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d("CurrencyWidgetProvider", "It is working")

        val useAPI = inputData.getBoolean("useAPI", false)
        Log.d("CurrencyFetchWorker", "useAPI: $useAPI")

        var rates: Map<String, Double> = emptyMap()
        if(useAPI) {
            val apiKey = BuildConfig.API_KEY
            val baseCurrency = "EUR"
            val targetCurrencies = arrayOf("UYU", "USD", "ARS")

            Log.d("CurrencyWidgetProvider", "using api: $apiKey")
            rates = fetchConversionRate(apiKey, baseCurrency, targetCurrencies)
            Log.d("CurrencyWidgetProvider", "Retrieved conversion rates: $rates")
        }
        else {
            // Trying to simulate random failure
//            val randomNumber = (1..10).random()
//            Log.d("CurrencyWidgetProvider", "randomNumber: $randomNumber")
//            if (randomNumber == 10) {
//                Log.d("CurrencyWidgetProvider", "Passeeeeeeeeeeeeeeeeeeeeed")
//                rates = mapOf("UYU" to 1.0, "USD" to 2.0)
//            }

//            rates = mapOf("UYU" to 1.0, "USD" to 2.0)
        }

        if (rates.isNotEmpty()) {
            saveRatesToPreferences(rates)
            return Result.success()
        }

        return Result.retry()
    }

    private fun saveRatesToPreferences(rates: Map<String, Double>) {
        val prefs = applicationContext.getSharedPreferences("CurrencyRates", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        for ((currency, rate) in rates) {
            editor.putFloat(currency, rate.toFloat())
        }

        editor.apply()
    }
}

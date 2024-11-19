package com.example.currencyexchange

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class CurrencyFetchWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d("CurrencyWidgetProvider", "It is working")

        val apiKey = BuildConfig.API_KEY
        val baseCurrency = "EUR"
        val targetCurrencies = arrayOf("UYU", "USD")

        Log.d("CurrencyWidgetProvider", "using api: $apiKey")
        val rates = fetchConversionRate(apiKey, baseCurrency, targetCurrencies)
        Log.d("CurrencyWidgetProvider", "Retrieved conversion rates: $rates")
        //val rates = mapOf("UYU" to 1.0, "USD" to 2.0)

        if (rates.isNotEmpty()) {
            saveRatesToPreferences(rates)
            return Result.success()
        }

        return Result.failure()
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

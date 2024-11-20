package com.example.currencyexchange

import android.util.Log
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

fun fetchConversionRate(apiKey: String, baseCurrency: String, targetCurrencies: Array<String>): Map<String, Double> {
    // Convert the array to a comma-separated string
    val symbols = targetCurrencies.joinToString(",")

    Log.d("CurrencyWidgetProvider", "Fetching... $apiKey base: $baseCurrency symbols: $symbols")
    val apiUrl = "https://api.exchangeratesapi.io/v1/latest?access_key=$apiKey&base=$baseCurrency&symbols=$symbols"
    val conversionRates = mutableMapOf<String, Double>()

    try {
        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        // Check if the response is OK (HTTP 200)
        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            val response = connection.inputStream.bufferedReader().readText()
            val jsonObject = JSONObject(response)

            Log.d("CurrencyWidgetProvider", "Fetched json object: $jsonObject")

            val rates = jsonObject.getJSONObject("rates")

            // Extract rates for all target currencies
            for (currency in targetCurrencies) {
                if (rates.has(currency)) {
                    conversionRates[currency] = rates.getDouble(currency)
                }
            }
        } else {
            Log.e("API_ERROR", "HTTP error: ${connection.responseCode}")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("API_ERROR", "Error fetching conversion rate: ${e.message}")
    }

    return conversionRates
}
package com.example.currencyexchange

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews

class CurrencyWidgetProvider : AppWidgetProvider() {
    private var inputText = "0"

    // Placeholder exchanges
    private var euroToUyu = 40.0
    private var euroToUsd = 1.05
    private var euroToArs = 1050.0

    // Toggle functionality
    private var currentCurrency = "UYU"
    private val currencies = arrayOf("UYU", "USD", "ARS")

    override fun onEnabled(context: Context) {
        super.onEnabled(context)

        // Initialize currency in SharedPreferences
        val prefs = context.getSharedPreferences("CurrencyPrefs", Context.MODE_PRIVATE)
        if (!prefs.contains("current_currency")) {
            prefs.edit().putString("current_currency", "UYU").apply()
        }

        // Log for debugging
        Log.d("CurrencyWidgetProvider", "Widget enabled. Default currency set to UYU.")
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val prefs = context.getSharedPreferences("CurrencyPrefs", Context.MODE_PRIVATE)
        currentCurrency = prefs.getString("current_currency", "UYU") ?: "UYU" // Load the current currency

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)

            // Update the toggle button text to reflect the current currency
            views.setTextViewText(R.id.button_toggle_currency, currentCurrency)

            // Set up button intents
            setButtonIntents(context, views, appWidgetId)

            // Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        // Retrieve the current currency from SharedPreferences
        val prefs = context.getSharedPreferences("CurrencyPrefs", Context.MODE_PRIVATE)
        currentCurrency = prefs.getString("current_currency", "UYU") ?: "UYU" // Use class-level variable

        val action = intent.action
        Log.d("CurrencyWidgetProvider", "Received action: $action")

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

        if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            inputText = getInputText(context, appWidgetId) // Retrieve the current inputText

            when (intent.action) {
                "ACTION_NUMBER" -> {
                    val number = intent.getStringExtra("EXTRA_NUMBER") ?: return
                    if (inputText == "0") inputText = "" // Remove leading zero
                    inputText += number
                }
                "ACTION_DECIMAL" -> {
                    inputText.replace(".", "")
                    inputText += "."
                }
                "ACTION_ERASE" -> {
                    inputText = if (inputText.length > 1) inputText.dropLast(1) else "0" // Remove last character
                }
                "ACTION_RESET" -> {
                    inputText = "0" // Reset input
                }
                "TOGGLE_CURRENCY" -> {
                    val currentIndex = currencies.indexOf(currentCurrency)
                    val nextIndex = (currentIndex + 1) % currencies.size
                    currentCurrency = currencies[nextIndex]

                    // Save the updated currency
                    prefs.edit().putString("current_currency", currentCurrency).apply()

                    // Update the toggle button text
                    views.setTextViewText(R.id.button_toggle_currency, currentCurrency)

                    Log.d("CurrencyWidgetProvider", "Selected currency: $currentCurrency")
                }
            }

            saveInputText(context, appWidgetId, inputText) // Save the updated inputText

            // Retrieve the stored conversion rates
            val prefs = context.applicationContext.getSharedPreferences("CurrencyRates", Context.MODE_PRIVATE)

            val euroToUyuConversion = prefs.getFloat("UYU", 0.0f).toDouble()
            if(euroToUyuConversion != 0.0) euroToUyu = euroToUyuConversion

            val euroToUsdConversion = prefs.getFloat("USD", 0.0f).toDouble()
            if(euroToUsdConversion != 0.0) euroToUsd = euroToUsdConversion

            val euroToArsConversion = prefs.getFloat("ARS", 0.0f).toDouble()
            if(euroToArsConversion != 0.0) euroToArs = euroToArsConversion

            Log.d("CurrencyWidgetProvider", "Retrieved conversion rates from applicationContext UYU: $euroToUyuConversion")
            Log.d("CurrencyWidgetProvider", "Retrieved conversion rates from applicationContext USD: $euroToUsdConversion")
            Log.d("CurrencyWidgetProvider", "Retrieved conversion rates from applicationContext ARS: $euroToArsConversion")

            // Update both TextViews
            updateTextViews(views)

            // Push the updated views to the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun setButtonIntents(context: Context, views: RemoteViews, appWidgetId: Int) {
        // Setting flags for the correct android versions
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        // Number Buttons
        for (i in 0..9) {
            val intent = Intent(context, CurrencyWidgetProvider::class.java).apply {
                action = "ACTION_NUMBER"
                putExtra("EXTRA_NUMBER", i.toString())
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            val pendingIntent = PendingIntent.getBroadcast(context, i, intent, flags)

            views.setOnClickPendingIntent(context.resources.getIdentifier("button_$i", "id", context.packageName), pendingIntent)
        }

        // Decimal Button
        val decimalIntent = Intent(context, CurrencyWidgetProvider::class.java).apply {
            action = "ACTION_DECIMAL"
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        val decimalPendingIntent = PendingIntent.getBroadcast(
            context,
            10,
            decimalIntent,
            flags
        )
        views.setOnClickPendingIntent(R.id.button_decimal, decimalPendingIntent)

        // Erase Button
        val eraseIntent = Intent(context, CurrencyWidgetProvider::class.java).apply {
            action = "ACTION_ERASE"
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        val erasePendingIntent = PendingIntent.getBroadcast(context, 11, eraseIntent, flags)
        views.setOnClickPendingIntent(R.id.button_erase, erasePendingIntent)

        // Reset Button
        val resetIntent = Intent(context, CurrencyWidgetProvider::class.java).apply {
            action = "ACTION_RESET"
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        val resetPendingIntent = PendingIntent.getBroadcast(context, 12, resetIntent, flags)
        views.setOnClickPendingIntent(R.id.button_reset, resetPendingIntent)

        // Currency Button
        val toggleIntent = Intent(context, CurrencyWidgetProvider::class.java).apply {
            action = "TOGGLE_CURRENCY"
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        val togglePendingIntent = PendingIntent.getBroadcast(context, 13, toggleIntent, flags)
        views.setOnClickPendingIntent(R.id.button_toggle_currency, togglePendingIntent)
    }

    private fun updateTextViews(views: RemoteViews) {
        // Update widget_input with the user's input
        views.setTextViewText(R.id.widget_input, inputText)

        // Calculate the conversion and update widget_converted_value
        val amount = inputText.toDoubleOrNull() ?: 0.0
        val rate = when (currentCurrency) {
            "USD" -> euroToUsd
            "ARS" -> euroToArs
            else -> euroToUyu
        }
        val euros = amount / rate

        val formattedEuros = String.format("€%.2f", euros)
        Log.d("CurrencyWidgetProvider", "Updating widget_converted_value with text: $formattedEuros")
        views.setTextViewText(R.id.widget_converted_value, formattedEuros)
    }

    private fun saveInputText(context: Context, appWidgetId: Int, value: String) {
        val prefs = context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("inputText_$appWidgetId", value).apply()
    }

    private fun getInputText(context: Context, appWidgetId: Int): String {
        val prefs = context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE)
        return prefs.getString("inputText_$appWidgetId", "0") ?: "0"
    }
}
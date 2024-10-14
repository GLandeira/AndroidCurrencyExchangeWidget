package com.example.currencyexchange

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

class CurrencyWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // Iterate through each widget instance
        for (appWidgetId in appWidgetIds) {
            // Create RemoteViews object using the widget layout
            val views = RemoteViews(context.packageName, R.layout.widget_layout)

            // Update the TextView in the widget layout
            views.setTextViewText(R.id.widget_text, "Hello from Widget!")

            // Request widget update
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
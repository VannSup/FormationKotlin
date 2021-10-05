package fr.vannsuplabs.formationkotlin.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateConverter {
    private const val API_DATE_PATTERN = "yyyy-MM-dd"
    private const val APP_DATE_PATTERN = "dd/MM/yyyy"

    fun convertDateToString(dateToConvert: String?): String {
        dateToConvert?: return "Unknown"

        val dateSplit = dateToConvert.split("T")[0]
        var date = Date()
        try {
            date = SimpleDateFormat(API_DATE_PATTERN, Locale.US).parse(dateSplit)!!
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val formatter = SimpleDateFormat(APP_DATE_PATTERN, Locale.FRANCE)
        return formatter.format(date)
    }
}
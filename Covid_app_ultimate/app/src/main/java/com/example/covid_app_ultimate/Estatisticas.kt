package com.example.covid_app_ultimate

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Estatisticas(
    var country: String?,
    var uf: String?,
    var state: String?,
    var suspects: Int = 0,
    var refuses: Int = 0,
    var cases: Int = 0,
    var confirmed: Int = 0,
    var deaths: Int = 0,
    var recovered: Int = 0,
    var date: String?,
    var hour: String?
) {
    override fun toString(): String {
        return date.toString()
    }

    fun getData(date: String): String {
        val diaString = date
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var date = LocalDate.parse(diaString)
        var formattedDate = date.format(formatter)
        return formattedDate
    }
}
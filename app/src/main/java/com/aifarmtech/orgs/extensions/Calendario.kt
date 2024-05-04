package com.aifarmtech.orgs.extensions

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
private val formatador = DateTimeFormatter
    .ofPattern("dd/MM/yyyy", Locale("UTC"))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.paraFormatoBrasileiro() : String = this.format(formatador)

@RequiresApi(Build.VERSION_CODES.O)
fun String.paraLocalDate(): LocalDate = LocalDate.parse(this, formatador)

@RequiresApi(Build.VERSION_CODES.O)
fun Long.paraLocalDate(): LocalDate = Instant.ofEpochMilli(this)
    .atZone(ZoneId.of("America/Sao_Paulo"))
    .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
    .toLocalDate()
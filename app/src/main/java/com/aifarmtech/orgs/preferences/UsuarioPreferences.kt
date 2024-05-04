package com.aifarmtech.orgs.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore


// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sessao_usuario")

val usuarioLogadoPreferences = stringPreferencesKey("usuarioLogado")

val produtoLogadoPreferences = longPreferencesKey("produtoLogado")
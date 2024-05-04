package com.aifarmtech.orgs.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.math.BigDecimal

@Entity
@Parcelize
data class Produto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nome: String,
    val cpf: String,
    val numerotelefone: String,
    val email: String,
    val descricao: String,
    val valor: BigDecimal,
    val imagem: String? = null,
    val usuarioId: String? = null
) : Parcelable


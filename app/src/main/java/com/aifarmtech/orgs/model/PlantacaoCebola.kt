package com.aifarmtech.orgs.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "plantacao_cebola")
data class PlantacaoCebola(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val comprimentoLinha: Int,
    val plantasPorMetro: Int,
    val custoPorMilPlantas: Double,
    val data: Long, // Adicionando a propriedade de data
    val hora: Long, // Adicionando a propriedade de hora
    @ColumnInfo(name = "produto_id")
    val produtoId: Long
)


//foreignKeys = [ForeignKey(
//entity = Produto::class,
//parentColumns = ["id"],
//childColumns = ["produtoId"],
//onDelete = ForeignKey.CASCADE
//)]

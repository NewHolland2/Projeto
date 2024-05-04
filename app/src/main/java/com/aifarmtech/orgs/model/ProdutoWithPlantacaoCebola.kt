package com.aifarmtech.orgs.model

import androidx.room.Embedded
import androidx.room.Relation

data class ProdutoWithPlantacaoCebola (
    @Embedded val produto: Produto,
    @Relation(
        parentColumn = "id",
        entityColumn = "produto_id"
    )
    val plantacaoCebola: List<PlantacaoCebola>
)
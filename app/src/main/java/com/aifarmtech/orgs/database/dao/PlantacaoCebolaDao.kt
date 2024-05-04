package com.aifarmtech.orgs.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.aifarmtech.orgs.model.PlantacaoCebola
import com.aifarmtech.orgs.model.Produto
import com.aifarmtech.orgs.model.ProdutoWithPlantacaoCebola
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantacaoCebolaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirPlantacao(plantacao: PlantacaoCebola)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirProduto(produto: Produto)

    @Query("SELECT * FROM plantacao_cebola")
    fun getPlantacoes(): LiveData<List<PlantacaoCebola>>

//    @Transaction
//    @Query("SELECT * FROM plantacao_cebola WHERE id = :produtoId")
//    fun getProdutoWithPlantacaoCebola(produtoId: Long): Flow<List<ProdutoWithPlantacaoCebola>>

    @Transaction
    @Query("SELECT * FROM produto")
    fun buscaProdutosComPlantacoes(): Flow<List<ProdutoWithPlantacaoCebola>>

    @Query("SELECT COUNT(*) FROM plantacao_cebola")
    fun getQuantidadePlantacoes(): Int

    @Delete
    suspend fun deletePlantacao(plantacao: PlantacaoCebola)

    @Query("SELECT * FROM plantacao_cebola WHERE id = :id")
    fun buscaPorId(id: Long) : Flow<PlantacaoCebola?>
}
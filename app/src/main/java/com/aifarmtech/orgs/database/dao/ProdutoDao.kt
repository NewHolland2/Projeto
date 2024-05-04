package com.aifarmtech.orgs.database.dao

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
interface ProdutoDao {

    @Query("SELECT * FROM Produto")
    fun buscaTodos(): Flow<List<Produto>>

    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId")
    fun buscaTodosDoUsuario(usuarioId: String) : Flow<List<Produto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salva(vararg produto: Produto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirPlantacao(plantacao: PlantacaoCebola)

    @Delete
    suspend fun remove(produto: Produto)

    @Query("SELECT * FROM produto WHERE id = :id")
    fun buscaPorId(id: Long): Flow<Produto>

    @Query("SELECT * FROM Produto ORDER BY nome ASC")
    fun buscaPorTodosOrdenadorNomeAsc(): List<Produto>?

    @Query("SELECT * FROM Produto ORDER BY nome DESC")
    abstract fun buscaPorTodosOrdenadorNomeDesc(): List<Produto>?

    @Query("SELECT * FROM Produto ORDER BY descricao ASC")
    abstract fun buscaPorTodosOrdenadorDescricaoAsc(): List<Produto>?

    @Query("SELECT * FROM Produto ORDER BY descricao DESC")
    abstract fun buscaPorTodosOrdenadorDescricaoDesc(): List<Produto>?

    @Query("SELECT * FROM Produto ORDER BY valor ASC")
    abstract fun buscaPorTodosOrdenadorValorAsc(): List<Produto>?

    @Query("SELECT * FROM Produto ORDER BY valor DESC")
    abstract fun buscaPorTodosOrdenadorValorDesc(): List<Produto>?
}
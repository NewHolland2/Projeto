package com.aifarmtech.orgs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aifarmtech.orgs.database.converter.Converters
import com.aifarmtech.orgs.database.dao.PlantacaoCebolaDao
import com.aifarmtech.orgs.database.dao.ProdutoDao
import com.aifarmtech.orgs.database.dao.UsuarioDao
import com.aifarmtech.orgs.model.PlantacaoCebola
import com.aifarmtech.orgs.model.Produto
import com.aifarmtech.orgs.model.Usuario
@Database(entities = [PlantacaoCebola::class, Usuario::class, Produto::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class) // Se você tem TypeConverters, adicione-os aqui
abstract class AppDataBase : RoomDatabase(){

    abstract fun plantacaoCebolaDao(): PlantacaoCebolaDao
    abstract fun UsuarioDao(): UsuarioDao
    abstract fun ProdutoDao(): ProdutoDao
    // Adicione outros DAOs aqui, se necessário

    companion object{
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun instancia(context: Context) : AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "plantacao_cebola_database"
                ).addMigrations(
                    MIGRATION_1_2,
                    MIGRATION_2_3,
                    MIGRATION_3_4,
                    MIGRATION_4_5
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
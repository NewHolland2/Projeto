package com.aifarmtech.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import com.aifarmtech.orgs.R
import com.aifarmtech.orgs.database.AppDataBase
import com.aifarmtech.orgs.databinding.ListaProdutosActivityBinding
import com.aifarmtech.orgs.extensions.vaiPara
import com.aifarmtech.orgs.model.Produto
import com.aifarmtech.orgs.ui.recyclerview.ListaProdutoAdapter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

private const val TAG = "ListaProdutosActivity"

class ListaProdutosActivity : UsuarioBaseActivity() {

    private val binding by lazy {
        ListaProdutosActivityBinding.inflate(layoutInflater)
    }

    private val adapter = ListaProdutoAdapter(context = this)

    private val produtoDao by lazy {
        val db = AppDataBase.instancia(this)
        db.ProdutoDao()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configurandoRecyclerView()
        configuraFab()

        // apresentar um componente que indica de carregamento
        lifecycleScope.launch {
            launch {
                usuario
                    .filterNotNull()
                    .collect {usuario ->
                        buscaProdutoUsuario(usuario.id)
                    }
            }
        }
    }

    private suspend fun buscaProdutoUsuario(usuarioId: String) {
        produtoDao.buscaTodosDoUsuario(usuarioId).collect { produtos ->
            adapter.atualiza(produtos)
        }
    }

//
//    override fun onResume() {
//        super.onResume()
////        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
////            Toast.makeText(
////                this@ListaProdutosActivity,
////                "Ocorreu um problema",
////                Toast.LENGTH_SHORT
////            ).show()
////        }
//
////        lifecycleScope.launch {
////            Log.i(TAG, "OnResume: contexto da Coroutine $coroutineContext")
////            repeat(1000) {
////                Log.i(TAG, "OnResume: Coroutine está em execução $it" )
////                delay(1000)
////            }
////        }
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_lista_produtos_ordenar, menu)
        menuInflater.inflate(R.menu.menu_lista_produto, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val produtosOrdenado: List<Produto>? = when (item.itemId) {
            R.id.menu_lista_produto_nome_asc ->
                produtoDao.buscaPorTodosOrdenadorNomeAsc()

            R.id.menu_lista_produto_nome_desc ->
                produtoDao.buscaPorTodosOrdenadorNomeDesc()

            R.id.menu_lista_produto_descricao_asc ->
                produtoDao.buscaPorTodosOrdenadorDescricaoAsc()

            R.id.menu_lista_produto_descricao_desc ->
                produtoDao.buscaPorTodosOrdenadorDescricaoDesc()

            R.id.menu_lista_produto_valor_asc ->
                produtoDao.buscaPorTodosOrdenadorValorAsc()

            R.id.menu_lista_produto_valor_desc ->
                produtoDao.buscaPorTodosOrdenadorValorDesc()

            else -> null
        }
        produtosOrdenado?.let {
            adapter.atualiza(it)
        }
        when (item.itemId) {
            R.id.menu_lista_produto_voltar_para_tela_login -> {
                lifecycleScope.launch {
                    deslogaUsuario()
                }
            }
            R.id.menu_lista_produto_perfil_do_usuario -> {
                lifecycleScope.launch {
                    vaiPara(ActivityPerfilUsuario::class.java)
                }
            }

            R.id.menu_lista_produtos_todos_produtos -> {
                lifecycleScope.launch {
                    vaiPara(TodosProdutosActivity::class.java)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configuraFab() {
        val fab = binding.floatingActionButton
        fab.setOnClickListener {
            vaiParaFormularioProduto()
        }
    }

    private fun vaiParaFormularioProduto() {
        val intent = Intent(this, FormularioProdutoActivity::class.java)
        startActivity(intent)
    }

    private fun configurandoRecyclerView() {
        val recyclerView = binding.activityRecyclerview
        //val recyclerView = findViewById<RecyclerView>(R.id.activity_lista_produto_recyclerView)
        recyclerView.adapter = adapter

        // implementação do listener para abrir a Activity de detalhes do produto
        // com o produto clicado
        adapter.quandoClicaNoItem = {
            val intent = Intent(
                this,
                DetalhesItemActivity::class.java
            ).apply {
                // envio do produto por meio do extra
                putExtra(CHAVE_PRODUTO_ID, it.id)
            }
            startActivity(intent)
        }
        adapter.quandoClicaEmEditar = {
            Log.i(TAG, "configuraRecyclerView: Editar $it")
        }
        adapter.quandoClicaEmRemover = {
            Log.i(TAG, "configuraRecyclerView: Remover $it")
        }
    }
}
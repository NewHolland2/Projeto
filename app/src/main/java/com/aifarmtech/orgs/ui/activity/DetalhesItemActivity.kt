
package com.aifarmtech.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aifarmtech.orgs.R
import com.aifarmtech.orgs.database.AppDataBase
import com.aifarmtech.orgs.databinding.ActivityDetalhesProdutoBinding
import com.aifarmtech.orgs.extensions.formataParaMoedaBrasileira
import com.aifarmtech.orgs.extensions.tentaCarregarImagem
import com.aifarmtech.orgs.extensions.vaiPara
import com.aifarmtech.orgs.model.Produto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class DetalhesItemActivity : AppCompatActivity() {
    private var produtoId: Long = 0L
    private var produto: Produto? = null
    private val binding by lazy {
        ActivityDetalhesProdutoBinding.inflate(layoutInflater)
    }
    private val produtoDao by lazy {
        AppDataBase.instancia(this).ProdutoDao()
    }


    private val scope = CoroutineScope(IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        tentaCarregarProduto()
//        val configurandoBotao = binding.activityFormularioCadastroBotaoCadastrar
//        configurandoBotao.setOnClickListener {
//            val intent = Intent(this, ActivityDetalhesFormularioDate::class.java)
//            startActivity(intent)
//            finish()
//        }

        configuraBotao()
    }

    override fun onResume() {
        super.onResume()
        buscaProduto()
    }

    private fun buscaProduto() {
        lifecycleScope.launch {
            produtoDao.buscaPorId(produtoId).collect { produtoEncontrado ->
                produto = produtoEncontrado
                produto?.let {
                    preencheCampos(it)
                } ?: finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_detalhes_protudo_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_detalhes_protudo_delete -> {
                lifecycleScope.launch {
                    produto?.let { produtoDao.remove(it) }
                    finish()
                }
            }

            R.id.menu_detalhes_protudo_editar -> {
                Intent(this, FormularioProdutoActivity::class.java).apply {
                    putExtra(CHAVE_PRODUTO_ID, produtoId)
                    startActivity(this)
                }
            }

            R.id.menu_lista_produto_perfil -> {
                Intent(this, ActivityDetalhesPerfil::class.java).apply {
                    startActivity(this)
                }
            }


        }
        return super.onOptionsItemSelected(item)
    }

    private fun tentaCarregarProduto() {
        // tentativa de buscar o produto se ele existir,
        // caso contrário, finalizar a Activity
        produtoId = intent.getLongExtra(CHAVE_PRODUTO_ID, 0L)
    }


    private fun preencheCampos(produtoCarregado: Produto) {
        with(binding) {
            activityDetalhesItemImagem.tentaCarregarImagem(produtoCarregado.imagem)
            activityDetalhesItemNome.text = produtoCarregado.nome
            activityDetalhesItemCpf.text = produtoCarregado.cpf
            activityDetalhesItemNumerotelefone.text = produtoCarregado.numerotelefone
            activityDetalhesItemEmail.text = produtoCarregado.email
            activityDetalhesItemDescricao.text = produtoCarregado.descricao
            activityDetalhesItemValor.text = produtoCarregado.valor.formataParaMoedaBrasileira()
        }
    }

    private fun configuraBotao() {
        val button = binding.activityDetalhesListaRelatorio

        button.setOnClickListener {
            vaiPara(PlantacaoCebolaActivity::class.java)
        }
    }
}

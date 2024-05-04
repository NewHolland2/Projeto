package com.aifarmtech.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aifarmtech.orgs.database.AppDataBase
import com.aifarmtech.orgs.databinding.ActivityDetalhesPerfilBinding
import com.aifarmtech.orgs.model.Produto
import kotlinx.coroutines.launch

class ActivityDetalhesPerfil: AppCompatActivity() {
    private var produtoId: Long = 0L
    private var produto: Produto? = null
    private val binding by lazy {
        ActivityDetalhesPerfilBinding.inflate(layoutInflater)
    }
    private val produtoDao by lazy {
        AppDataBase.instancia(this).ProdutoDao()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        tentaCarregarProduto()
        configuraBotaoSairActivity()
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            buscaProduto()
        }
    }


    private fun buscaProduto() {
        lifecycleScope.launch {
            produtoDao.buscaPorId(produtoId).collect { produtoEncontrado ->
                produto = produtoEncontrado
                produto?.let {
                    preencheCampo(it)
                }?:finish()
            }
        }
    }

    private fun tentaCarregarProduto() {
        produtoId = intent.getLongExtra(CHAVE_PRODUTO_ID, 0L)
        Log.i("DetalhesPerfil", "onCreate: falha ao tentar carregar produto")
    }

    private fun configuraBotaoSairActivity() {
        binding.activityPerfilUsuarioBotaoSairDoApp.setOnClickListener {
            lifecycleScope.launch {
                finish()
            }
        }
    }

    private fun preencheCampo(produtoCarregado: Produto){
        with(binding) {
            activityDetalhesItemNome.text = produtoCarregado.nome
            activityDetalhesItemCpf.text = produtoCarregado.cpf
            activityDetalhesItemNumerotelefone.text = produtoCarregado.numerotelefone
            activityDetalhesItemEmail.text = produtoCarregado.email
            activityDetalhesItemDescricao.text = produtoCarregado.descricao
        }
    }
}
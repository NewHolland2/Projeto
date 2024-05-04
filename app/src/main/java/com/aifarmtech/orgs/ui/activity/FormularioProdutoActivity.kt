package com.aifarmtech.orgs.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.aifarmtech.orgs.database.AppDataBase
import com.aifarmtech.orgs.database.dao.ProdutoDao
import com.aifarmtech.orgs.databinding.ActivityFormularioProdutoBinding
import com.aifarmtech.orgs.dialog.FormularioImagemDialog
import com.aifarmtech.orgs.extensions.tentaCarregarImagem
import com.aifarmtech.orgs.model.Produto
import kotlinx.coroutines.launch
import java.math.BigDecimal

class FormularioProdutoActivity : UsuarioBaseActivity() {
    private val binding by lazy {
        ActivityFormularioProdutoBinding.inflate(layoutInflater)
    }

    private var url: String? = null
    private var produtoId = 0L
    private val produtoDao: ProdutoDao by lazy {
        val db = AppDataBase.instancia(this)
        db.ProdutoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Cadastrar Pessoa"
        configuraBotaoSalvar()
        binding.activityFormularioProdutoImagem.setOnClickListener {
            FormularioImagemDialog(this)
                .mostra(url) { imagem ->
                    url = imagem
                    binding.activityFormularioProdutoImagem.tentaCarregarImagem(url)
                }
        }
        TentaCarregarProduto()
        TentaBuscarProduto()
    }

    private fun TentaCarregarProduto() {
        produtoId = intent.getLongExtra(CHAVE_PRODUTO_ID, 0L)
    }

    private fun TentaBuscarProduto() {
        lifecycleScope.launch {
            produtoDao.buscaPorId(produtoId).collect { produto ->
                produto?.let {
                    title = "Alterar Dados"
                    PreencheCampo(it)
                }

            }
        }
    }

    private fun PreencheCampo(produtoCarregado: Produto) {
        title = "Alterar Dados Pessoais"
        url = produtoCarregado.imagem
        binding.activityFormularioProdutoImagem.tentaCarregarImagem(produtoCarregado.imagem)
        binding.activityFormularioProdutoNome.setText(produtoCarregado.nome)
        binding.activityFormularioProdutoDescricao.setText(produtoCarregado.descricao)
        binding.activityFormularioProdutoValor.setText(produtoCarregado.valor.toPlainString())
    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = binding.activityFormularioProdutoBotaoSalvar

        botaoSalvar.setOnClickListener {

            lifecycleScope.launch {
                usuario.value?.let { usuario ->
                    val produtoNovo = CriaProduto(usuario.id)
                    produtoDao.salva(produtoNovo)
                    finish()
                }
            }
        }
    }

    private fun CriaProduto(usuarioId: String): Produto {
        val campoNome = binding.activityFormularioProdutoNome
        val nome = campoNome.text.toString()
        val campocpf = binding.activityFormularioProdutoCpf
        val cpf = campocpf.text.toString()
        val campoNumeroTelefone = binding.activityFormularioProdutoNumerotelefone
        val numerotelefone = campoNumeroTelefone.text.toString()
        val campoEmail = binding.activityFormularioProdutoEmail
        val email = campoEmail.text.toString()
        val campoDescricao = binding.activityFormularioProdutoDescricao
        val descricao = campoDescricao.text.toString()
        val campoValor = binding.activityFormularioProdutoValor
        val valorEmTexto = campoValor.text.toString()
        val valor = if (valorEmTexto.isBlank()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(valorEmTexto)
        }

        return Produto(
            id = produtoId,
            nome = nome,
            cpf = cpf,
            numerotelefone = numerotelefone,
            email = email,
            descricao = descricao,
            valor = valor,
            imagem = url,
            usuarioId = usuarioId
        )
    }
}

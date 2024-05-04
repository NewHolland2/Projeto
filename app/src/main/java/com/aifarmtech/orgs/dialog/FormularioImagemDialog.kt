package com.aifarmtech.orgs.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.aifarmtech.orgs.databinding.ProdutoDialogFormularioImagemBinding
import com.aifarmtech.orgs.extensions.tentaCarregarImagem

class FormularioImagemDialog(private val context: Context) {

    fun mostra(
        urlPadrao: String? = null,
        quandoImagemCarregada: (imagem: String) -> Unit
    ) {
        val binding = ProdutoDialogFormularioImagemBinding.inflate(LayoutInflater.from(context)).apply {
            urlPadrao?.let {
                formularioDialogImageviewPadrao.tentaCarregarImagem(it)
                formularioDialogProdutoUrl.setText(it)
            }
            formularioDialogButtonCarregar.setOnClickListener {
                val url = formularioDialogProdutoUrl.text.toString()
                formularioDialogImageviewPadrao.tentaCarregarImagem(url) }

            AlertDialog.Builder(context)
                .setView(root)
                .setPositiveButton("Confirmar") { _, _ ->
                    val url = formularioDialogProdutoUrl.text.toString()
                    quandoImagemCarregada(url)
                }
                .setNegativeButton("Cancelar") { _, _ ->

                }
                .show()
        }

    }

}
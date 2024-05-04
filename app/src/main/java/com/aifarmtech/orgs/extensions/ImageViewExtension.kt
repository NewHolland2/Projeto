package com.aifarmtech.orgs.extensions

import android.widget.ImageView

import coil.load
import com.aifarmtech.orgs.R

fun ImageView.tentaCarregarImagem(
    url: String? = null,
    // transformação de parâmetro com valor padrão para possibilitar a alteração do fallback
    // essa mesma técnica pode ser utilizada para o error e placeholder também
    fallback: Int = R.drawable.imagem_padrao
) {
    load(url) {
        fallback(fallback)
        error(R.drawable.baseline_error_24)
        placeholder(R.drawable.placeholder)
    }
}
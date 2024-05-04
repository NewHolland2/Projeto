package com.aifarmtech.orgs.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aifarmtech.orgs.fragments.ProdutoRepository
import com.aifarmtech.orgs.model.ProdutoComPlantacoes
import kotlinx.coroutines.launch

class ProdutoComPlatacao(private val repository: ProdutoRepository) : ViewModel() {
    private val _produtosComPlantacoes = MutableLiveData<List<ProdutoComPlantacoes>>()
    val produtosComPlantacoes: LiveData<List<ProdutoComPlantacoes>> = _produtosComPlantacoes

    fun buscarProdutosComPlantacoes() {
        viewModelScope.launch {
            val produtos = repository.buscarProdutosComPlantacoes()
            _produtosComPlantacoes.postValue(produtos)
        }
    }
}
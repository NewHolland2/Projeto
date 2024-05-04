package com.aifarmtech.orgs.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aifarmtech.orgs.R
import com.aifarmtech.orgs.database.AppDataBase
import com.aifarmtech.orgs.databinding.MainActivityBinding
import com.aifarmtech.orgs.model.PlantacaoCebola
import com.aifarmtech.orgs.model.Produto
import com.aifarmtech.orgs.preferences.dataStore
import com.aifarmtech.orgs.preferences.produtoLogadoPreferences
import com.aifarmtech.orgs.ui.recyclerview.PlantacaoAdapter
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import kotlin.math.log

class PlantacaoCebolaActivity: AppCompatActivity() {

    private var produtoId = 0L
    private var plantacoesId = 0L
    private lateinit var plantacaoAdapter: PlantacaoAdapter
    private var selectedDate: Long = 0
    private var selectedTime: Long = 0

    private val _produto: MutableStateFlow<Produto?> = MutableStateFlow(null)
    private val produto: StateFlow<Produto?> = _produto

    private val binding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }

    private val plantacaoCebolaDao by lazy {
        AppDataBase.instancia(this).plantacaoCebolaDao()
    }

    private val produtoDao by lazy {
        AppDataBase.instancia(this).ProdutoDao()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setContentView(binding.root)
        val recycler = findViewById<RecyclerView>(R.id.recyclerViewPlantacoes)
        plantacaoAdapter = PlantacaoAdapter()
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = plantacaoAdapter


        val button = findViewById<Button>(R.id.buttonCalcular)
        val buscandoComprimentoLinha = findViewById<TextInputEditText>(R.id.editTextComprimentoLinha)
        val buscandoPlantasPorMetros = findViewById<TextInputEditText>(R.id.editTextPlantasPorMetro)
        val buscandoCustoPorMetro = findViewById<TextInputEditText>(R.id.editTextCustoPorMilPlantas)
        val buscandoResultado = findViewById<TextView>(R.id.textViewResultado)
        button.setOnClickListener {
            val comprimentoLinha = buscandoComprimentoLinha.text.toString().toIntOrNull()
            val plantasPorMetro = buscandoPlantasPorMetros.text.toString().toIntOrNull()
            val custoPorMilPlantas = buscandoCustoPorMetro.text.toString().toDoubleOrNull()

            if (comprimentoLinha != null && plantasPorMetro != null && custoPorMilPlantas != null) {
                selecionarDataEhora()
                Log.i("Plantacao", "onCreate: ")
            } else {
                buscandoResultado.text = "Por favor, insira números válidos."
            }
        }

        lifecycleScope.launch {
            verificaProduto().let {
                Log.i("Plantacao", "aqui está correto!")
            }
        }
        carregarPlantacoes()
    }


    private suspend fun verificaProduto() {
        dataStore.data.collect {preferences ->
            preferences[produtoLogadoPreferences]?.let { usuarioId ->
                buscaProduto(usuarioId)
            }
        }
    }

    private suspend fun buscaProduto(id: Long): Produto? {
        return produtoDao.buscaPorId(id).firstOrNull().also {
            _produto.value = it
        }
    }

    private fun selecionarDataEhora() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecione a Data")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener { selectedDateInMillis ->
            selectedDate = selectedDateInMillis
            selecionarHora()
        }


        datePicker.show(supportFragmentManager, "DATE_PICKER")
    }

    private fun selecionarHora() {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                .setMinute(Calendar.getInstance().get(Calendar.MINUTE))
                .setTitleText("Selecione a Hora")
                .build()

        timePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            calendar.set(Calendar.MINUTE, timePicker.minute)
            selectedTime = calendar.timeInMillis
            salvarPlantacao()
        }

        timePicker.show(supportFragmentManager, "TIME_PICKER")
    }

    @SuppressLint("WrongViewCast")
    private fun salvarPlantacao() {
        val buscaComprimentoLinha = findViewById<TextInputEditText>(R.id.editTextComprimentoLinha)
        val buscaPlantasPorMetro = findViewById<TextInputEditText>(R.id.editTextPlantasPorMetro)
        val buscaCustoPorMilPlantas = findViewById<TextInputEditText>(R.id.editTextCustoPorMilPlantas)

        if (buscaComprimentoLinha != null && buscaPlantasPorMetro != null && buscaCustoPorMilPlantas != null) {
            val comprimentoLinha = buscaComprimentoLinha.text.toString().toInt()
            val plantasPorMetro = buscaPlantasPorMetro.text.toString().toInt()
            val custoPorMilPlantas = buscaCustoPorMilPlantas.text.toString().toDouble()

            val plantacao = PlantacaoCebola(
                    comprimentoLinha = comprimentoLinha,
                    plantasPorMetro = plantasPorMetro,
                    custoPorMilPlantas = custoPorMilPlantas,
                    data = selectedDate,
                    hora = selectedTime,
                    produtoId = 0L
                )


            lifecycleScope.launch(Dispatchers.IO) {
                val produtoNovo = plantacao
                AppDataBase.instancia(this@PlantacaoCebolaActivity).plantacaoCebolaDao()
                    .inserirPlantacao(produtoNovo)
                updateQuantideLinha()
            }
        }
    }


    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun carregarPlantacoes() {

        val plantacaoDao = AppDataBase.instancia(this).plantacaoCebolaDao()
        plantacaoDao.getPlantacoes().observe(this) { plantacoes ->

            plantacaoAdapter.submitList(plantacoes)

            // Calcular e exibir o custo total de todas as plantações
            val custoTotalGeral = plantacoes.sumOf { plantacao ->
                // Recalcular a quantidade de plantas e o custo total para cada plantação
                val quantidadePlantasMultiplicando = (plantacao.comprimentoLinha * plantacao.plantasPorMetro).toFloat()
                val quantidadePlantas = quantidadePlantasMultiplicando / 1000
                quantidadePlantas * plantacao.custoPorMilPlantas
            }
            val buscaCustoTotalGeral = findViewById<TextView>(R.id.textViewCustoTotalGeral)
            buscaCustoTotalGeral.text =
                "Custo Total de Todas as Plantações: R$ ${String.format("%.2f", custoTotalGeral)}"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateQuantideLinha() {
        lifecycleScope.launch(Dispatchers.IO) {
            val quantidadeLinhas = AppDataBase.instancia(this@PlantacaoCebolaActivity).plantacaoCebolaDao().getQuantidadePlantacoes()
            withContext(Dispatchers.Main) {
                val buscaQuantidadeLinha = findViewById<TextView>(R.id.textViewQuantidadeLinhas)
                buscaQuantidadeLinha.text = "Quantidade de Linhas de Plantação: $quantidadeLinhas"
            }
        }
    }
}

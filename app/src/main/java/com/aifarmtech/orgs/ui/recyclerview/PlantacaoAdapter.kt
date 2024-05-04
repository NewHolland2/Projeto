package com.aifarmtech.orgs.ui.recyclerview

import android.annotation.SuppressLint
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aifarmtech.orgs.R
import com.aifarmtech.orgs.database.AppDataBase
import com.aifarmtech.orgs.model.PlantacaoCebola
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar


class PlantacaoAdapter : ListAdapter<PlantacaoCebola, PlantacaoAdapter.PlantacaoViewHolder>(
    PlantacaoDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantacaoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_plantacao_cebola, parent, false)
        return PlantacaoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantacaoViewHolder, position: Int) {
        val plantacao = getItem(position)
        holder.bind(plantacao)
    }


    inner class PlantacaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnLongClickListener {
                showPopupMenu()
                true
            }
        }
        @SuppressLint("SetTextI18n")
        fun bind(plantacao: PlantacaoCebola) {
            with(itemView) {
                val buscarComprimentoLinha = findViewById<TextView>(R.id.textViewComprimentoLinha)
                val buscaarPlantasPorMetro = findViewById<TextView>(R.id.textViewPlantasPorMetro)
                val buscarQuantidadePlantas = findViewById<TextView>(R.id.textViewQuantidadePlantas)
                val buscarCustoPorMetro = findViewById<TextView>(R.id.textViewCustoPorMilPlantas)
                val buscarCustoTotal = findViewById<TextView>(R.id.textViewCustoTotal)
                buscarComprimentoLinha.text = "Comprimento: ${plantacao.comprimentoLinha} metros"
                buscaarPlantasPorMetro.text = "Plantas por metro: ${plantacao.plantasPorMetro}"
                buscarCustoPorMetro.text = "Custo por mil plantas: R$ ${plantacao.custoPorMilPlantas}"
                val quantidadePlantasMultiplicando = (plantacao.comprimentoLinha * plantacao.plantasPorMetro).toFloat()
                buscarQuantidadePlantas.text = "Quantidade de Plantas: ${quantidadePlantasMultiplicando}"
                val quantidadePlantas = quantidadePlantasMultiplicando / 1000
                val custoTotal = (quantidadePlantas * plantacao.custoPorMilPlantas).toFloat()
                buscarCustoTotal.text= "Custo Total: R$ ${String.format("%.2f", custoTotal)}"
                val buscaData = findViewById<TextView>(R.id.textViewData)
                val buscaHora = findViewById<TextView>(R.id.textViewHora)
                buscaData.text = "Data: ${formatarData(plantacao.data)}"
                buscaHora.text = "Hora: ${formatarHora(plantacao.hora)}"
            }
        }

        private fun showPopupMenu() {
            val popupMenu = PopupMenu(itemView.context, itemView)
            popupMenu.menuInflater.inflate(R.menu.menu_adapter, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_remove -> {
                        removeItem(adapterPosition)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        @OptIn(DelicateCoroutinesApi::class)
        private fun removeItem(position: Int) {
            val plantacao = getItem(position)
            // Remova o item do banco de dados
            GlobalScope.launch(Dispatchers.IO) {
                AppDataBase.instancia(itemView.context).plantacaoCebolaDao().deletePlantacao(plantacao)
            }
        }

        private fun formatarData(data: Long): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = data
            return DateFormat.format("dd/MM/yyyy", calendar).toString()
        }

        private fun formatarHora(hora: Long): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = hora
            return DateFormat.format("HH:mm", calendar).toString()
        }
    }

    class PlantacaoDiffCallback : DiffUtil.ItemCallback<PlantacaoCebola>() {
        override fun areItemsTheSame(oldItem: PlantacaoCebola, newItem: PlantacaoCebola): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PlantacaoCebola, newItem: PlantacaoCebola): Boolean {
            return oldItem == newItem
        }
    }
}
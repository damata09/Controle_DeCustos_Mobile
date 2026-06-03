package com.example.costcontrol.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.costcontrol.databinding.ItemCostBinding
import com.example.costcontrol.model.Cost
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class CostAdapter(
    private var costs: List<Cost>,
    private val onEditClick: (Cost) -> Unit,
    private val onDeleteClick: (Cost) -> Unit
) : RecyclerView.Adapter<CostAdapter.CostViewHolder>() {

    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    inner class CostViewHolder(private val binding: ItemCostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cost: Cost) {
            binding.tvCostDesc.text = cost.descricao
            binding.tvCostValue.text = currencyFormat.format(cost.valor)
            binding.tvCostCategory.text = cost.categoria?.nome ?: "Sem categoria"

            val rawDate = cost.data
            binding.tvCostDate.text = try {
                if (rawDate != null) {
                    val cleanDate = rawDate.substringBefore(".")
                    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    parser.timeZone = TimeZone.getTimeZone("UTC")
                    val formatter = SimpleDateFormat("dd/MM/yyyy · HH:mm", Locale.getDefault())
                    val date = parser.parse(cleanDate)
                    if (date != null) formatter.format(date) else rawDate
                } else {
                    "Sem data"
                }
            } catch (e: Exception) {
                rawDate?.substringBefore("T") ?: "Sem data"
            }

            binding.btnEditCost.setOnClickListener { onEditClick(cost) }
            binding.btnDeleteCost.setOnClickListener { onDeleteClick(cost) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CostViewHolder {
        val binding = ItemCostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CostViewHolder, position: Int) {
        holder.bind(costs[position])
    }

    override fun getItemCount(): Int = costs.size

    fun updateData(newCosts: List<Cost>) {
        costs = newCosts
        notifyDataSetChanged()
    }
}

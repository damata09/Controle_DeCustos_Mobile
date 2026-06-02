package com.example.costcontrol.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.costcontrol.databinding.ItemCategoryBinding
import com.example.costcontrol.model.Category

class CategoryAdapter(
    private var categories: List<Category>,
    private val onEditClick: (Category) -> Unit,
    private val onDeleteClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.tvCategoryName.text = category.nome
            binding.tvCategoryDesc.text = category.descricao ?: "Sem descrição"

            binding.btnEditCategory.setOnClickListener { onEditClick(category) }
            binding.btnDeleteCategory.setOnClickListener { onDeleteClick(category) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    fun updateData(newCategories: List<Category>) {
        this.categories = newCategories
        notifyDataSetChanged()
    }
}

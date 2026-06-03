package com.example.costcontrol.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.costcontrol.R
import com.example.costcontrol.databinding.ItemCategoryBinding
import com.example.costcontrol.model.Category

class CategoryAdapter(
    private var categories: List<Category>,
    private val onEditClick: (Category) -> Unit,
    private val onDeleteClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val avatarColors = intArrayOf(
        R.color.avatar1,
        R.color.avatar2,
        R.color.avatar3,
        R.color.avatar4,
        R.color.avatar5
    )

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category, position: Int) {
            binding.tvCategoryName.text = category.nome
            binding.tvCategoryDesc.text = category.descricao ?: "Sem descrição"
            binding.tvCategoryInitial.text =
                category.nome.trim().firstOrNull()?.uppercase() ?: "?"

            val color = ContextCompat.getColor(
                binding.root.context,
                avatarColors[position % avatarColors.size]
            )
            (binding.tvCategoryInitial.background as? GradientDrawable)?.setColor(color)
                ?: run {
                    val drawable = GradientDrawable().apply {
                        shape = GradientDrawable.OVAL
                        setColor(color)
                    }
                    binding.tvCategoryInitial.background = drawable
                }

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
        holder.bind(categories[position], position)
    }

    override fun getItemCount(): Int = categories.size

    fun updateData(newCategories: List<Category>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}

package com.example.costcontrol.ui.category

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.costcontrol.api.RetrofitClient
import com.example.costcontrol.databinding.ActivityCategoryEditBinding
import com.example.costcontrol.model.Category
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryEditBinding
    private var category: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Obter Categoria dos Extras
        category = intent.getSerializableExtra("EXTRA_CATEGORY") as? Category
        if (category == null) {
            Toast.makeText(this, "Erro ao carregar dados da categoria.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Preencher Formulário
        binding.tietCategoryName.setText(category?.nome)
        binding.tietCategoryDesc.setText(category?.descricao)

        // Ação do Botão Atualizar
        binding.btnUpdateCategory.setOnClickListener {
            updateCategory()
        }
    }

    private fun updateCategory() {
        val name = binding.tietCategoryName.text.toString().trim()
        val desc = binding.tietCategoryDesc.text.toString().trim()

        if (name.isEmpty()) {
            binding.tilCategoryName.error = "O nome da categoria é obrigatório"
            return
        } else {
            binding.tilCategoryName.error = null
        }

        val categoryId = category?.id ?: return
        val updatedCategory = Category(
            id = categoryId,
            nome = name,
            descricao = if (desc.isEmpty()) null else desc
        )

        binding.btnUpdateCategory.isEnabled = false
        binding.btnUpdateCategory.text = "Salvando alterações..."

        RetrofitClient.instance.updateCategory(categoryId, updatedCategory)
            .enqueue(object : Callback<Category> {
                override fun onResponse(call: Call<Category>, response: Response<Category>) {
                    binding.btnUpdateCategory.isEnabled = true
                    binding.btnUpdateCategory.text = "Salvar Alterações"

                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@CategoryEditActivity,
                            "Categoria atualizada com sucesso!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@CategoryEditActivity,
                            "Erro ao atualizar categoria: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Category>, t: Throwable) {
                    binding.btnUpdateCategory.isEnabled = true
                    binding.btnUpdateCategory.text = "Salvar Alterações"
                    Toast.makeText(
                        this@CategoryEditActivity,
                        "Falha na rede: ${t.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

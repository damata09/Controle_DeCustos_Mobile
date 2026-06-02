package com.example.costcontrol.ui.category

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.costcontrol.api.RetrofitClient
import com.example.costcontrol.databinding.ActivityCategoryCreateBinding
import com.example.costcontrol.model.Category
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Ação do Botão Salvar
        binding.btnSaveCategory.setOnClickListener {
            saveCategory()
        }
    }

    private fun saveCategory() {
        val name = binding.tietCategoryName.text.toString().trim()
        val desc = binding.tietCategoryDesc.text.toString().trim()

        if (name.isEmpty()) {
            binding.tilCategoryName.error = "O nome da categoria é obrigatório"
            return
        } else {
            binding.tilCategoryName.error = null
        }

        val newCategory = Category(nome = name, descricao = if (desc.isEmpty()) null else desc)

        binding.btnSaveCategory.isEnabled = false
        binding.btnSaveCategory.text = "Salvando..."

        RetrofitClient.instance.createCategory(newCategory).enqueue(object : Callback<Category> {
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                binding.btnSaveCategory.isEnabled = true
                binding.btnSaveCategory.text = "Salvar Categoria"

                if (response.isSuccessful) {
                    Toast.makeText(
                        this@CategoryCreateActivity,
                        "Categoria cadastrada com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish() // Volta para a tela anterior (Lista)
                } else {
                    Toast.makeText(
                        this@CategoryCreateActivity,
                        "Erro ao salvar categoria: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                binding.btnSaveCategory.isEnabled = true
                binding.btnSaveCategory.text = "Salvar Categoria"
                Toast.makeText(
                    this@CategoryCreateActivity,
                    "Falha de rede: ${t.localizedMessage}",
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

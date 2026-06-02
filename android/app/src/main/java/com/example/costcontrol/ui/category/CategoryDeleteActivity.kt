package com.example.costcontrol.ui.category

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.costcontrol.api.RetrofitClient
import com.example.costcontrol.databinding.ActivityCategoryDeleteBinding
import com.example.costcontrol.model.Category
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryDeleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryDeleteBinding
    private var category: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryDeleteBinding.inflate(layoutInflater)
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

        // Preencher Informações de Confirmação
        binding.tvCategoryNameConfirm.text = category?.nome
        binding.tvCategoryDescConfirm.text = category?.descricao ?: "Sem descrição"

        // Configurar botões
        binding.btnCancelDelete.setOnClickListener {
            finish() // Apenas fecha a tela
        }

        binding.btnConfirmDelete.setOnClickListener {
            confirmDelete()
        }
    }

    private fun confirmDelete() {
        val categoryId = category?.id ?: return

        binding.btnConfirmDelete.isEnabled = false
        binding.btnConfirmDelete.text = "Excluindo..."
        binding.btnCancelDelete.isEnabled = false

        RetrofitClient.instance.deleteCategory(categoryId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                binding.btnConfirmDelete.isEnabled = true
                binding.btnConfirmDelete.text = "Sim, Excluir Categoria"
                binding.btnCancelDelete.isEnabled = true

                if (response.isSuccessful) {
                    Toast.makeText(
                        this@CategoryDeleteActivity,
                        "Categoria excluída com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish() // Volta para a listagem
                } else {
                    Toast.makeText(
                        this@CategoryDeleteActivity,
                        "Erro ao excluir categoria (código: ${response.code()})",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                binding.btnConfirmDelete.isEnabled = true
                binding.btnConfirmDelete.text = "Sim, Excluir Categoria"
                binding.btnCancelDelete.isEnabled = true
                Toast.makeText(
                    this@CategoryDeleteActivity,
                    "Erro de conexão: ${t.localizedMessage}",
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

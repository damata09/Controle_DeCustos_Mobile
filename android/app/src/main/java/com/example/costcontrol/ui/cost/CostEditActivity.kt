package com.example.costcontrol.ui.cost

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.costcontrol.api.RetrofitClient
import com.example.costcontrol.databinding.ActivityCostEditBinding
import com.example.costcontrol.model.Category
import com.example.costcontrol.model.Cost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CostEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCostEditBinding
    private var cost: Cost? = null
    private var categoriesList: List<Category> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCostEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Obter Custo dos Extras
        cost = intent.getSerializableExtra("EXTRA_COST") as? Cost
        if (cost == null) {
            Toast.makeText(this, "Erro ao carregar dados da despesa.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Preencher Campos Iniciais
        binding.tietCostDesc.setText(cost?.descricao)
        binding.tietCostValue.setText(cost?.valor.toString())

        // Carregar categorias e selecionar a atual
        loadCategoriesAndSelectCurrent()

        // Ação do Botão Atualizar
        binding.btnUpdateCost.setOnClickListener {
            updateCost()
        }
    }

    private fun loadCategoriesAndSelectCurrent() {
        binding.btnUpdateCost.isEnabled = false
        RetrofitClient.instance.getCategories().enqueue(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if (response.isSuccessful) {
                    categoriesList = response.body() ?: emptyList()
                    
                    val spinnerAdapter = ArrayAdapter(
                        this@CostEditActivity,
                        android.R.layout.simple_spinner_item,
                        categoriesList
                    )
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerCategory.adapter = spinnerAdapter

                    // Selecionar a categoria correspondente ao custo editado
                    val activeIndex = categoriesList.indexOfFirst { it.id == cost?.categoria_id }
                    if (activeIndex >= 0) {
                        binding.spinnerCategory.setSelection(activeIndex)
                    }

                    binding.btnUpdateCost.isEnabled = true
                } else {
                    Toast.makeText(this@CostEditActivity, "Erro ao listar categorias.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Toast.makeText(this@CostEditActivity, "Falha de rede: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    private fun updateCost() {
        val desc = binding.tietCostDesc.text.toString().trim()
        val valueStr = binding.tietCostValue.text.toString().trim()

        if (desc.isEmpty()) {
            binding.tilCostDesc.error = "A descrição é obrigatória"
            return
        } else {
            binding.tilCostDesc.error = null
        }

        val valor = valueStr.toDoubleOrNull()
        if (valor == null || valor <= 0) {
            binding.tilCostValue.error = "Insira um valor positivo válido"
            return
        } else {
            binding.tilCostValue.error = null
        }

        val selectedCategory = binding.spinnerCategory.selectedItem as? Category
        if (selectedCategory == null || selectedCategory.id == null) {
            Toast.makeText(this, "Selecione uma categoria válida", Toast.LENGTH_SHORT).show()
            return
        }

        val costId = cost?.id ?: return
        val updatedCost = Cost(
            id = costId,
            descricao = desc,
            valor = valor,
            categoria_id = selectedCategory.id
        )

        binding.btnUpdateCost.isEnabled = false
        binding.btnUpdateCost.text = "Salvando alterações..."

        RetrofitClient.instance.updateCost(costId, updatedCost).enqueue(object : Callback<Cost> {
            override fun onResponse(call: Call<Cost>, response: Response<Cost>) {
                binding.btnUpdateCost.isEnabled = true
                binding.btnUpdateCost.text = "Salvar Alterações"

                if (response.isSuccessful) {
                    Toast.makeText(
                        this@CostEditActivity,
                        "Custo atualizado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@CostEditActivity,
                        "Erro ao atualizar: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Cost>, t: Throwable) {
                binding.btnUpdateCost.isEnabled = true
                binding.btnUpdateCost.text = "Salvar Alterações"
                Toast.makeText(
                    this@CostEditActivity,
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

package com.example.costcontrol.ui.cost

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.costcontrol.api.RetrofitClient
import com.example.costcontrol.databinding.ActivityCostCreateBinding
import com.example.costcontrol.model.Category
import com.example.costcontrol.model.Cost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CostCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCostCreateBinding
    private var categoriesList: List<Category> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCostCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Carregar categorias dinamicamente para o Spinner
        loadCategoriesForSpinner()

        // Ação do Botão Salvar
        binding.btnSaveCost.setOnClickListener {
            saveCost()
        }
    }

    private fun loadCategoriesForSpinner() {
        binding.btnSaveCost.isEnabled = false
        RetrofitClient.instance.getCategories().enqueue(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if (response.isSuccessful) {
                    categoriesList = response.body() ?: emptyList()
                    if (categoriesList.isEmpty()) {
                        Toast.makeText(
                            this@CostCreateActivity,
                            "Cadastre pelo menos uma categoria antes de registrar custos!",
                            Toast.LENGTH_LONG
                        ).show()
                        finish() // Fecha a tela pois não é possível criar custo sem categoria
                        return
                    }

                    // Configurar Adaptador do Spinner
                    val spinnerAdapter = ArrayAdapter(
                        this@CostCreateActivity,
                        android.R.layout.simple_spinner_item,
                        categoriesList
                    )
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerCategory.adapter = spinnerAdapter
                    
                    binding.btnSaveCost.isEnabled = true
                } else {
                    Toast.makeText(
                        this@CostCreateActivity,
                        "Erro ao carregar categorias para seleção.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Toast.makeText(
                    this@CostCreateActivity,
                    "Falha ao obter categorias: ${t.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        })
    }

    private fun saveCost() {
        val desc = binding.tietCostDesc.text.toString().trim()
        val valueStr = binding.tietCostValue.text.toString().trim()

        // Validações
        if (desc.isEmpty()) {
            binding.tilCostDesc.error = "A descrição é obrigatória"
            return
        } else {
            binding.tilCostDesc.error = null
        }

        val valor = valueStr.toDoubleOrNull()
        if (valor == null || valor <= 0) {
            binding.tilCostValue.error = "Insira um valor numérico positivo válido"
            return
        } else {
            binding.tilCostValue.error = null
        }

        val selectedCategory = binding.spinnerCategory.selectedItem as? Category
        if (selectedCategory == null || selectedCategory.id == null) {
            Toast.makeText(this, "Selecione uma categoria válida", Toast.LENGTH_SHORT).show()
            return
        }

        val newCost = Cost(
            descricao = desc,
            valor = valor,
            categoria_id = selectedCategory.id
        )

        binding.btnSaveCost.isEnabled = false
        binding.btnSaveCost.text = "Salvando..."

        RetrofitClient.instance.createCost(newCost).enqueue(object : Callback<Cost> {
            override fun onResponse(call: Call<Cost>, response: Response<Cost>) {
                binding.btnSaveCost.isEnabled = true
                binding.btnSaveCost.text = "Salvar Despesa"

                if (response.isSuccessful) {
                    Toast.makeText(
                        this@CostCreateActivity,
                        "Custo registrado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish() // Retorna
                } else {
                    Toast.makeText(
                        this@CostCreateActivity,
                        "Erro ao criar custo: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Cost>, t: Throwable) {
                binding.btnSaveCost.isEnabled = true
                binding.btnSaveCost.text = "Salvar Despesa"
                Toast.makeText(
                    this@CostCreateActivity,
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

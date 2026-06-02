package com.example.costcontrol.ui.category

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.costcontrol.adapter.CategoryAdapter
import com.example.costcontrol.api.RetrofitClient
import com.example.costcontrol.databinding.ActivityCategoryListBinding
import com.example.costcontrol.model.Category
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryListBinding
    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Configurar RecyclerView
        binding.rvCategories.layoutManager = LinearLayoutManager(this)
        adapter = CategoryAdapter(
            categories = emptyList(),
            onEditClick = { category ->
                val intent = Intent(this, CategoryEditActivity::class.java).apply {
                    putExtra("EXTRA_CATEGORY", category)
                }
                startActivity(intent)
            },
            onDeleteClick = { category ->
                val intent = Intent(this, CategoryDeleteActivity::class.java).apply {
                    putExtra("EXTRA_CATEGORY", category)
                }
                startActivity(intent)
            }
        )
        binding.rvCategories.adapter = adapter

        // Botão flutuante para cadastrar
        binding.fabAddCategory.setOnClickListener {
            val intent = Intent(this, CategoryCreateActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadCategories()
    }

    private fun loadCategories() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvCategories.visibility = View.GONE
        binding.tvEmptyState.visibility = View.GONE

        RetrofitClient.instance.getCategories().enqueue(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    adapter.updateData(list)
                    
                    if (list.isEmpty()) {
                        binding.tvEmptyState.visibility = View.VISIBLE
                    } else {
                        binding.rvCategories.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(
                        this@CategoryListActivity,
                        "Erro ao carregar categorias (Código: ${response.code()})",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                binding.tvEmptyState.text = "Falha na conexão com o servidor."
                binding.tvEmptyState.visibility = View.VISIBLE
                Toast.makeText(
                    this@CategoryListActivity,
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

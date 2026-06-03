package com.example.costcontrol.ui.cost

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.costcontrol.adapter.CostAdapter
import com.example.costcontrol.api.RetrofitClient
import com.example.costcontrol.databinding.ActivityCostListBinding
import com.example.costcontrol.model.Cost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CostListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCostListBinding
    private lateinit var adapter: CostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCostListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Configurar RecyclerView
        binding.rvCosts.layoutManager = LinearLayoutManager(this)
        adapter = CostAdapter(
            costs = emptyList(),
            onEditClick = { cost ->
                val intent = Intent(this, CostEditActivity::class.java).apply {
                    putExtra("EXTRA_COST", cost)
                }
                startActivity(intent)
            },
            onDeleteClick = { cost ->
                val intent = Intent(this, CostDeleteActivity::class.java).apply {
                    putExtra("EXTRA_COST", cost)
                }
                startActivity(intent)
            }
        )
        binding.rvCosts.adapter = adapter

        // FAB para adicionar custo
        binding.fabAddCost.setOnClickListener {
            val intent = Intent(this, CostCreateActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadCosts()
    }

    private fun loadCosts() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvCosts.visibility = View.GONE
        binding.emptyStateLayout.visibility = View.GONE

        RetrofitClient.instance.getCosts().enqueue(object : Callback<List<Cost>> {
            override fun onResponse(call: Call<List<Cost>>, response: Response<List<Cost>>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    adapter.updateData(list)

                    if (list.isEmpty()) {
                        binding.emptyStateLayout.visibility = View.VISIBLE
                    } else {
                        binding.rvCosts.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(
                        this@CostListActivity,
                        "Erro ao carregar custos: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Cost>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                binding.tvEmptyState.text = "Falha na conexão com o servidor."
                binding.emptyStateLayout.visibility = View.VISIBLE
                Toast.makeText(
                    this@CostListActivity,
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

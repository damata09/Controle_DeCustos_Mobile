package com.example.costcontrol.ui.cost

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.costcontrol.api.RetrofitClient
import com.example.costcontrol.databinding.ActivityCostDeleteBinding
import com.example.costcontrol.model.Cost
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class CostDeleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCostDeleteBinding
    private var cost: Cost? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCostDeleteBinding.inflate(layoutInflater)
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

        // Preencher informações de confirmação
        binding.tvCostDescConfirm.text = cost?.descricao
        binding.tvCostValueConfirm.text = String.format(Locale.getDefault(), "Valor: R$ %.2f", cost?.valor)
        binding.tvCostCategoryConfirm.text = "Categoria: ${cost?.categoria?.nome ?: "Sem Categoria"}"

        // Formatação amigável da data
        val rawDate = cost?.data
        binding.tvCostDateConfirm.text = try {
            if (rawDate != null) {
                val cleanDate = rawDate.substringBefore(".")
                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                parser.timeZone = TimeZone.getTimeZone("UTC")
                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val date = parser.parse(cleanDate)
                if (date != null) "Data: " + formatter.format(date) else "Data: " + rawDate
            } else {
                "Data: Não informada"
            }
        } catch (e: Exception) {
            "Data: " + (rawDate?.substringBefore("T") ?: "Não informada")
        }

        // Ações dos botões
        binding.btnCancelDelete.setOnClickListener {
            finish()
        }

        binding.btnConfirmDelete.setOnClickListener {
            confirmDelete()
        }
    }

    private fun confirmDelete() {
        val costId = cost?.id ?: return

        binding.btnConfirmDelete.isEnabled = false
        binding.btnConfirmDelete.text = "Excluindo..."
        binding.btnCancelDelete.isEnabled = false

        RetrofitClient.instance.deleteCost(costId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                binding.btnConfirmDelete.isEnabled = true
                binding.btnConfirmDelete.text = "Sim, Excluir Registro"
                binding.btnCancelDelete.isEnabled = true

                if (response.isSuccessful) {
                    Toast.makeText(
                        this@CostDeleteActivity,
                        "Custo excluído com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@CostDeleteActivity,
                        "Erro ao excluir custo: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                binding.btnConfirmDelete.isEnabled = true
                binding.btnConfirmDelete.text = "Sim, Excluir Registro"
                binding.btnCancelDelete.isEnabled = true
                Toast.makeText(
                    this@CostDeleteActivity,
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

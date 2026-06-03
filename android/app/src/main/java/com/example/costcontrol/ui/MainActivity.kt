package com.example.costcontrol.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.costcontrol.databinding.ActivityMainBinding
import com.example.costcontrol.ui.category.CategoryListActivity
import com.example.costcontrol.ui.cost.CostListActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardCategories.setOnClickListener {
            startActivity(Intent(this, CategoryListActivity::class.java))
        }

        binding.cardCosts.setOnClickListener {
            startActivity(Intent(this, CostListActivity::class.java))
        }
    }
}

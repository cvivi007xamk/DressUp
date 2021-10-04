package com.example.pue

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.pue.data.DataSource
import com.example.pue.databinding.ActivityFinalBinding

class FinalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final)
// Move to start screen
        val button: Button = findViewById(R.id.button2)
       // val binding = ActivityFinalBinding.inflate(layoutInflater)
        button.setOnClickListener {
            moveToStartScreen()
            DataSource.chosenClothes.clear()
            DataSource.clothes.forEach { it.checked = false }
        }
    }

    private fun moveToStartScreen() {
        intent = Intent(this, MainActivity::class.java)
        // val NUMBER = DataSource.chosenClothesData.size

        // Export a list size as extra
       // intent.putExtra(DressingActivity.NUMBER, DataSource.chosenClothes.size.toString())
        startActivity(intent)
    }
}
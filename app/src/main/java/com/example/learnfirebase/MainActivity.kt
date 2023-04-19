package com.example.learnfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnInsertData.setOnClickListener {
            val i = Intent(this, InsertionActivity::class.java)
            startActivity(i)
        }

        btnFetchData.setOnClickListener {
            val i = Intent(this, FetchingActivity::class.java)
            startActivity(i)
        }
    }
}
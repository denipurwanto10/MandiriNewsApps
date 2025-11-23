package com.example.mandirinews.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mandirinews.R
import com.example.mandirinews.databinding.ActivityDetailNewsBinding

class DetailNewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set toolbar sebagai ActionBar
        setSupportActionBar(binding.topAppBar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)   // tombol back
            title = getString(R.string.details_news) // tampilkan "Details"
        }

        // Klik back
        binding.topAppBar.setNavigationOnClickListener { finish() }

        // Terima data dari intent
        val title = intent.getStringExtra("EXTRA_TITLE")
        val image = intent.getStringExtra("EXTRA_IMAGE")
        val source = intent.getStringExtra("EXTRA_SOURCE")
        val date = intent.getStringExtra("EXTRA_DATE")
        val description = intent.getStringExtra("EXTRA_DESCRIPTION")

        // Set data ke view
        binding.apply {
            tvDetailTitle.text = title
            tvDetailSource.text = source
            tvDetailDate.text = date
            tvDetailDescription.text = description

            Glide.with(this@DetailNewsActivity)
                .load(image)
                .placeholder(R.drawable.loading_image)
                .into(imgDetailNews)
        }
    }
}

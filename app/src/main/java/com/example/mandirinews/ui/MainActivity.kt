package com.example.mandirinews.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mandirinews.R
import com.example.mandirinews.data.di.Injection
import com.example.mandirinews.data.di.ViewModelFactory
import com.example.mandirinews.data.response.ArticlesItem
import com.example.mandirinews.databinding.ActivityMainBinding
import com.example.mandirinews.ui.MainAdapter.Companion.VIEW_TYPE_HORIZONTAL
import com.example.mandirinews.ui.MainAdapter.Companion.VIEW_TYPE_VERTICAL

// ⬇️ WAJIB: Import DetailNewsActivity
import com.example.mandirinews.ui.DetailNewsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val repository = Injection.provideNewsRepository()
    private val factory = ViewModelFactory(repository)
    private val viewModel: MainViewModel by viewModels { factory }

    private lateinit var headlineNewsAdapter: MainAdapter
    private lateinit var allNewsAdapter: MainAdapter

    private var originalAllNews: List<ArticlesItem?> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerViews()

        viewModel.headlineNewsResult.observe(this) { headlines ->
            if (headlines != null) {
                val listHeadline = headlines.map {
                    it?.copy(viewType = VIEW_TYPE_HORIZONTAL)
                }
                headlineNewsAdapter.submitList(listHeadline)
            }
        }

        viewModel.allNewsResult.observe(this) { allNews ->
            if (allNews != null) {
                originalAllNews = allNews
                val listAll = allNews.map {
                    it?.copy(viewType = VIEW_TYPE_VERTICAL)
                }
                allNewsAdapter.submitList(listAll)
            }
        }

        viewModel.isLoading.observe(this) { showLoading(it) }
        viewModel.isError.observe(this) { showError(it) }

        setSupportActionBar(binding.toolbarHome)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        viewModel.getHeadlineNews()
        viewModel.getAllNews()
    }

    private fun setupRecyclerViews() {
        headlineNewsAdapter = MainAdapter()
        binding.rvHeadlineNews.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = headlineNewsAdapter
        }

        headlineNewsAdapter.onItemClick = { item ->
            openDetail(item)
        }

        allNewsAdapter = MainAdapter()
        binding.rvAllNews.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = allNewsAdapter
        }

        allNewsAdapter.onItemClick = { item ->
            openDetail(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.queryHint = "Cari berita..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterNews(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterNews(newText.orEmpty())
                return true
            }
        })

        return true
    }

    private fun filterNews(query: String) {

        if (query.isEmpty()) {
            setHeadlineVisibility(true)

            val resetList = originalAllNews.map {
                it?.copy(viewType = VIEW_TYPE_VERTICAL)
            }
            allNewsAdapter.submitList(resetList)
            return
        }

        setHeadlineVisibility(false)

        val filtered = originalAllNews.filter {
            it?.title?.contains(query, ignoreCase = true) == true
        }.map {
            it?.copy(viewType = VIEW_TYPE_VERTICAL)
        }

        allNewsAdapter.submitList(filtered)
    }
    
    private fun openDetail(item: ArticlesItem?) {
        if (item == null) return

        val intent = Intent(this, DetailNewsActivity::class.java)
        intent.putExtra("EXTRA_TITLE", item.title)
        intent.putExtra("EXTRA_IMAGE", item.urlToImage)
        intent.putExtra("EXTRA_SOURCE", item.source?.name)
        intent.putExtra("EXTRA_DATE", item.publishedAt)
        intent.putExtra("EXTRA_DESCRIPTION", item.description)
        startActivity(intent)
    }



    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setHeadlineVisibility(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.tvHeadlineNews.visibility = visibility
        binding.rvHeadlineNews.visibility = visibility
    }
}

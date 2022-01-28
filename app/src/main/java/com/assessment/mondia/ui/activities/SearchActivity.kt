package com.assessment.mondia.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.assessment.mondia.R
import com.assessment.mondia.data.models.SearchItem
import com.assessment.mondia.databinding.ActivitySearchBinding
import com.assessment.mondia.ui.adapters.SearchItemsAdapter
import com.assessment.mondia.ui.fragments.DetailedViewSheet
import com.assessment.mondia.ui.viewmodels.MainViewModel
import com.assessment.mondia.utils.result.Resource
import com.assessment.mondia.utils.result.ResourceType.ERROR
import com.assessment.mondia.utils.result.ResourceType.LOADING
import com.assessment.mondia.utils.result.ResourceType.SUCCESS
import com.assessment.mondia.utils.showErrorMessage
import com.assessment.mondia.utils.showLoading

class SearchActivity : AppCompatActivity(), SearchItemsAdapter.SearchItemActionsListener {

  private lateinit var binding: ActivitySearchBinding
  private val mainViewModel: MainViewModel by viewModels()
  private val searchItemsAdapter: SearchItemsAdapter by lazy { SearchItemsAdapter(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    binding = ActivitySearchBinding.inflate(layoutInflater)
    setContentView(binding.root)
    getAccessToken()
    setupSearchView()
    setupRecyclerView()
  }

  /**
   * This method is used to call the token API to get the access token which will
   * be used in subsequent API requests
   */
  private fun getAccessToken() {
    mainViewModel.login().observe(this, {
      when (it.resourceType) {
        SUCCESS -> {
          setLoading(false)
        }
        LOADING -> {
          setLoading(true)
        }
        ERROR -> {
          setError(it.message)
          Log.e("TAG", "handleSearchResult: ${it.message}")
        }
      }
    })
  }

  /**
   * This method is used to setup the searchView's query listener and make the search request
   * when the user clicks on the search button on the soft keyboard
   */
  private fun setupSearchView() {
    binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextChange(newText: String?): Boolean {
        return false
      }

      override fun onQueryTextSubmit(query: String?): Boolean {
        mainViewModel.search(query)
          .observe(this@SearchActivity, this@SearchActivity::handleSearchResult)
        return false
      }
    })
  }

  private fun handleSearchResult(result: Resource<List<SearchItem>>) {
    when (result.resourceType) {
      SUCCESS -> {
        if (result.data.isNullOrEmpty())
          setError(getString(R.string.search_error))
        else submitList(result.data)
      }
      LOADING -> {
        setLoading(true)
      }
      ERROR -> {
        setError(result.message)
        Log.e("TAG", "handleSearchResult: ${result.message}")
      }
    }
  }

  private fun submitList(searchResultList: List<SearchItem>?) {
    searchItemsAdapter.submitList(searchResultList)
    binding.layoutSearchHint.visibility = View.GONE
    binding.rvSearchItems.visibility = View.VISIBLE
    setLoading(false)
  }

  /**
   * this method is used to setup the basics for the recycler views
   */
  private fun setupRecyclerView() {
    binding.rvSearchItems.layoutManager = LinearLayoutManager(this)
    binding.rvSearchItems.adapter = searchItemsAdapter
  }

  override fun onItemClick(searchItem: SearchItem) {
    DetailedViewSheet().apply { show(supportFragmentManager, DetailedViewSheet.TAG) }
    mainViewModel.setSelectedItem(searchItem)
    binding.searchView.clearFocus()
  }

  private fun setLoading(isLoading: Boolean) = binding.layoutProgress.showLoading(isLoading)

  private fun setError(msg: String?) {
    binding.layoutSearchHint.visibility = View.GONE
    binding.layoutProgress.showErrorMessage(msg)
  }
}
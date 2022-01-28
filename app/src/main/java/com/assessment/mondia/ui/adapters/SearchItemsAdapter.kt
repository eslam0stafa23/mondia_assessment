package com.assessment.mondia.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.assessment.mondia.data.models.SearchItem
import com.assessment.mondia.databinding.ItemSearchResultBinding
import com.assessment.mondia.utils.Constants
import com.assessment.mondia.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * This is the adapter used for the search result recycler view it takes :
 * @property searchItemActionsListener SearchItemActionsListener which is used to control the actions
 * performed on the item inside the adapter
 *
 */
class SearchItemsAdapter(private val searchItemActionsListener: SearchItemActionsListener) :
  ListAdapter<SearchItem, SearchItemsAdapter.SearchItemViewHolder>(callback) {

  companion object {
    private val callback = object : DiffUtil.ItemCallback<SearchItem>() {
      override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem) = false

      override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem) = false
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
    val binding =
      ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return SearchItemViewHolder(binding)
  }

  override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class SearchItemViewHolder(private val binding: ItemSearchResultBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(searchItem: SearchItem) {

      when (searchItem.type) {
        Constants.SONG, Constants.RELEASE -> {
          binding.tvItemTitle.text = searchItem.title
          binding.tvItemArtist.text = searchItem.mainArtist.name
          binding.tvItemType.text = searchItem.type
        }
        Constants.ARTIST -> {
          binding.tvItemTitle.text = searchItem.name
          binding.tvItemArtist.text = searchItem.type
          binding.tvItemType.visibility = View.GONE
        }
      }
      CoroutineScope(Dispatchers.IO).launch {
        val bitmap = Utils.downloadBitmap("https:${searchItem.cover.medium}")
        withContext(Dispatchers.Main) {
          binding.ivItemImage.setImageBitmap(bitmap)
        }
      }
      binding.layoutItem.setOnClickListener { searchItemActionsListener.onItemClick(searchItem) }
    }
  }

  interface SearchItemActionsListener {
    fun onItemClick(searchItem: SearchItem)
  }
}

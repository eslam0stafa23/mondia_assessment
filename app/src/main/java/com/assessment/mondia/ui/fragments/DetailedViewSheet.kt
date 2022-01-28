package com.assessment.mondia.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.assessment.mondia.R
import com.assessment.mondia.data.models.SearchItem
import com.assessment.mondia.databinding.SheetDetailedViewBinding
import com.assessment.mondia.ui.viewmodels.MainViewModel
import com.assessment.mondia.utils.Constants
import com.assessment.mondia.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.os.Build
import android.view.Window
import android.graphics.drawable.LayerDrawable

import android.graphics.drawable.Drawable

import android.graphics.drawable.GradientDrawable

import android.util.DisplayMetrics

import androidx.annotation.NonNull

import androidx.annotation.RequiresApi




class DetailedViewSheet : BottomSheetDialogFragment() {

  private lateinit var binding: SheetDetailedViewBinding

  private lateinit var mainViewModel: MainViewModel

  companion object {
    const val TAG = "DetailedViewSheet"
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = SheetDetailedViewBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    // init the view model
    mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

    // observe the selected item to show it's data
    mainViewModel.selectedItem.observe(requireActivity(), this::feedViewsWithData)
  }

  /**
   * This method is used to feed the data up to the views while checking each item's type and
   * dealing with it accordingly
   * @param searchItem SearchItem
   */

  private fun feedViewsWithData(searchItem: SearchItem) {
    loadImage(searchItem)

    // set the item genre
    if (!searchItem.genres.isNullOrEmpty()) binding.tvGenres.text =
      searchItem.genres[0] else binding.layoutGenres.visibility = View.GONE

    // set the popularity bar value
    val popularity = searchItem.statistics.popularity.toFloat()
    if (popularity > 0.0f) {
      binding.popularityBar.rating = popularity.times(5)
    } else {
      binding.layoutPopularity.visibility = View.GONE
    }

    // set the estimated total listening count
    if (searchItem.statistics.estimatedTotalCount > 0) {
      binding.tvTotalTimesListened.text = searchItem.statistics.estimatedTotalCount.toString()
    } else {
      binding.layoutTotalListens.visibility = View.GONE
    }

    when (searchItem.type) {
      Constants.SONG -> {
        binding.tvItemTitle.text = searchItem.title
        binding.tvItemArtist.text = searchItem.mainArtist.name
        binding.tvReleaseDate.text = Utils.formatApiDate(searchItem.publishingDate)
        binding.tvItemType.text = searchItem.type
        binding.tvItemAlbum.text = searchItem.release.title
      }
      Constants.ARTIST -> {
        binding.tvItemTitle.text = searchItem.name
        binding.tvItemArtist.text = searchItem.type
        binding.layoutTypeAndDate.visibility = View.GONE
        binding.tvItemAlbum.visibility = View.GONE

      }
      Constants.RELEASE -> {
        binding.tvItemTitle.text = searchItem.title
        binding.tvItemArtist.text = searchItem.mainArtist.name
        binding.tvReleaseDate.text = Utils.formatApiDate(searchItem.publishingDate)
        binding.tvItemType.text = searchItem.type
        binding.tvItemAlbum.text = searchItem.label
      }
    }
  }

  private fun loadImage(searchItem: SearchItem) {
    CoroutineScope(Dispatchers.IO).launch {
      val bitmap = Utils.downloadBitmap("https:${searchItem.cover.large}")
      withContext(Dispatchers.Main) {
        binding.ivItemImage.setImageBitmap(bitmap)
      }
    }
  }
}
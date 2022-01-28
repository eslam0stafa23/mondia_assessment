package com.assessment.mondia.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.assessment.mondia.data.MainRepository
import com.assessment.mondia.data.models.SearchItem
import com.assessment.mondia.utils.asMappedResourceLiveData
import com.assessment.mondia.utils.result.Resource

/**
 * This is the main view model class, it's used to provide data and functionality to be used by
 * the views
 * @property mainRepository is where this class gets the needed data from
 * @property selectedItem is the select item which will be shown inside the details bottom sheet
 */
class MainViewModel : ViewModel() {

  private val mainRepository = MainRepository()

  val selectedItem = MutableLiveData<SearchItem>()

  fun setSelectedItem(searchItem: SearchItem) {
    this.selectedItem.value = searchItem
  }

  fun login(): LiveData<Resource<String>> =
    mainRepository.login().asMappedResourceLiveData()

  fun search(searchQuery: String?): LiveData<Resource<List<SearchItem>>> =
    mainRepository.search(searchQuery).asMappedResourceLiveData()

}

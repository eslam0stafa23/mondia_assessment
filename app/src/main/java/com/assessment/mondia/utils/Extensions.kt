package com.assessment.mondia.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.assessment.mondia.utils.result.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

/**
 * This method is used to transform a flow to a mapped resource liveData object
 * @receiver Flow<Resource<T>>
 * @return LiveData<Resource<T>>
 */
fun <T> Flow<Resource<T>>.asMappedResourceLiveData(): LiveData<Resource<T>> {
  return catch { emit(Resource.error(it.message)) }.asLiveData(Dispatchers.IO)
}

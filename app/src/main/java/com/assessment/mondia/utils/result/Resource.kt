package com.assessment.mondia.utils.result

import androidx.annotation.Keep
import com.assessment.mondia.utils.result.ResourceType.ERROR
import com.assessment.mondia.utils.result.ResourceType.LOADING
import com.assessment.mondia.utils.result.ResourceType.SUCCESS

@Keep
class Resource<T> {
  val resourceType: ResourceType
  var data: T? = null
    private set
  var message: String? = null
    private set

  constructor(resourceType: ResourceType, data: T?, message: String?) {
    this.resourceType = resourceType
    this.data = data
    this.message = message
  }

  constructor(resourceType: ResourceType, data: T?) {
    this.resourceType = resourceType
    this.data = data
  }

  constructor(resourceType: ResourceType, message: String?) {
    this.resourceType = resourceType
    this.message = message
  }

  companion object {
    fun <T> success(data: T): Resource<T> = Resource(SUCCESS, data)
    fun <T> error(message: String?): Resource<T> = Resource(ERROR, message)
    fun <T> error(): Resource<T> = Resource(ERROR, null)
    fun <T> loading(): Resource<T> = Resource(LOADING, null)
  }
}
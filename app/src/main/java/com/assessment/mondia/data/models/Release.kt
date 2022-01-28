package com.assessment.mondia.data.models

import org.json.JSONObject

data class Release(val jsonObject: JSONObject?) {
  val id: Int? = jsonObject?.optInt("id")
  val title: String? = jsonObject?.optString("title")
}
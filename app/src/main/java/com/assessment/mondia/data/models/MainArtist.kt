package com.assessment.mondia.data.models

import org.json.JSONObject

data class MainArtist(val jsonObject: JSONObject?) {
  val id: Int? = jsonObject?.optInt("id")
  val name: String? = jsonObject?.optString("name")
  val role: String? = jsonObject?.optString("role")
}
package com.assessment.mondia.data.models

import org.json.JSONObject

data class Cover(val jsonObject: JSONObject?) {
  val large: String? = jsonObject?.optString("large")
  val medium: String? = jsonObject?.optString("medium")
  val small: String? = jsonObject?.optString("small")
  val tiny: String? = jsonObject?.optString("tiny")
}
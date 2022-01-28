package com.assessment.mondia.data.models

import org.json.JSONObject

data class Statistics(val jsonObject: JSONObject) {
  val estimatedTotalCount: Int = jsonObject.optInt("estimatedTotalCount")
  val popularity: String = jsonObject.optString("popularity")
}
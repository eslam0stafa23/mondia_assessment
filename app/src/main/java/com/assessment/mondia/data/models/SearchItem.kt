package com.assessment.mondia.data.models

import org.json.JSONObject

data class SearchItem(val json: String) : JSONObject(json) {

  val adfunded: Boolean = optBoolean("adfunded")
  val bundleOnly: Boolean = optBoolean("bundleOnly")
  val duration: Int = optInt("duration")
  val id: Int = optInt("id")
  val publishingDate: String? = optString("publishingDate")
  val streamable: Boolean = optBoolean("streamable")
  val title: String? = optString("title")
  val name: String? = optString("name")
  val trackNumber: Int = optInt("trackNumber")
  val type: String? = optString("type")
  val label: String? = optString("label")
  val volumeNumber: Int = optInt("volumeNumber")
  val cover: Cover = Cover(optJSONObject("cover"))
  val statistics: Statistics = Statistics(optJSONObject("statistics"))
  val mainArtist: MainArtist = MainArtist(optJSONObject("mainArtist"))
  val release: Release = Release(optJSONObject("release"))
  val additionalArtists: List<AdditionalArtist?>? = optJSONArray("genres")
    ?.let {
      0.until(it.length())
        .map { i -> it.optJSONObject(i) }
    }?.map { AdditionalArtist(it) }
  val genres: List<String?>? = optJSONArray("genres")?.let {
    0.until(it.length()).map { i -> it[i].toString() }
  }

}
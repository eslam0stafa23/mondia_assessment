package com.assessment.mondia.utils

object Constants {

  const val BASE_URL = "https://staging-gateway.mondiamedia.com/v0/api/"
  const val BASE_URL_VERSION_TWO = "https://staging-gateway.mondiamedia.com/v2/api/"
  const val LOGIN_ENDPOINT = "gateway/token/client"
  const val SEARCH_ENDPOINT = "sayt/flat?query="
  const val SEARCH_ENDPOINT_LIMIT = "&limit=20"

  const val REQUEST_METHOD_POST = "POST"

  const val GATEWAY_HEADER_KEY = "X-MM-GATEWAY-KEY"
  const val GATEWAY_HEADER_VALUE = "G2269608a-bf41-2dc7-cfea-856957fcab1e"

  const val AUTHORIZATION = "Authorization"
  const val TOKEN_TYPE = "Bearer "


  const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
  const val DATE_ONLY_FORMAT = "MMM yyyy"

  const val SONG = "song"
  const val RELEASE = "release"
  const val ARTIST = "artist"



}
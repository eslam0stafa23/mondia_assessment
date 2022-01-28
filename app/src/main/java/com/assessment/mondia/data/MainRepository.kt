package com.assessment.mondia.data

import android.util.Log
import com.assessment.mondia.data.models.SearchItem
import com.assessment.mondia.utils.Constants
import com.assessment.mondia.utils.Utils
import com.assessment.mondia.utils.result.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * This is the main repository class which is the single source of truth to all the data flows
 * inside the app, it's used to make the APIs requests and the business logic then deliver the
 * needed data to the view model
 */
class MainRepository {

  private lateinit var accessToken: String
  private val tag = MainRepository::class.java.simpleName

  /**
   * This method is used to make a call to the endpoint which returns the token
   * it uses flow to emit the loading, error, and success status
   *
   * @returns Flow<Resource<String>> with type success in case of success
   */
  fun login(): Flow<Resource<String>> {
    return flow {
      emit(Resource.loading())
      try {
        val response: String
        val url = URL("${Constants.BASE_URL}${Constants.LOGIN_ENDPOINT}")
        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.requestMethod = Constants.REQUEST_METHOD_POST
        urlConnection.addRequestProperty(
          Constants.GATEWAY_HEADER_KEY,
          Constants.GATEWAY_HEADER_VALUE
        )
        try {
          val inputStream: InputStream = BufferedInputStream(urlConnection.inputStream)
          response = Utils.convertStreamToString(inputStream)
          Log.i(tag, "login: $response")
        } finally {
          urlConnection.disconnect()
        }
        accessToken = JSONObject(response).optString("accessToken")
        emit(Resource.success(accessToken))
      } catch (e: IOException) {
        Log.e(tag, "login error: $e")
        emit(Resource.error())
      }
    }
  }

  /**
   * This method is used to make the search request, it takes the search query and uses the access
   * token which we got from the first request
   *
   * it uses flow to emit the loading, error, and success status
   * @param searchQuery String? the search query
   * @return Flow<Resource<List<SearchItem>> which is a list of the search result items limited
   * to 20 results
   */
  fun search(searchQuery: String?): Flow<Resource<List<SearchItem>>> {
    return flow {
      emit(Resource.loading())
      try {
        val response: String
        val url =
          URL("${Constants.BASE_URL_VERSION_TWO}${Constants.SEARCH_ENDPOINT}$searchQuery${Constants.SEARCH_ENDPOINT_LIMIT}")
        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.addRequestProperty(
          Constants.AUTHORIZATION,
          "${Constants.TOKEN_TYPE}$accessToken"
        )
        try {
          val inputStream: InputStream = BufferedInputStream(urlConnection.inputStream)
          response = Utils.convertStreamToString(inputStream)
          Log.i(tag, "search: $response")
        } finally {
          urlConnection.disconnect()
        }

        // get the response JSON Array
        val responseJson = JSONArray(response)

        // loop on the array and transform each item into SearchItem object
        val data = responseJson
          .let {
            // returns an array of JSONObjects
            0.until(it.length()).map { i -> it.optJSONObject(i) }
          }
          .map {
            // transforms each JSONObject of the array into a SearchItem
            SearchItem(it.toString())
          }

        emit(Resource.success(data))
      } catch (e: IOException) {
        Log.e(tag, "search error: $e")
        emit(Resource.error())
      }
    }
  }
}


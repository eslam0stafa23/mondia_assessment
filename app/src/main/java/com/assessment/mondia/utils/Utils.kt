package com.assessment.mondia.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Log
import java.io.InputStream
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Scanner

object Utils {
  fun convertStreamToString(inputStream: InputStream?): String {
    val s = Scanner(inputStream).useDelimiter("\\A")
    return if (s.hasNext()) s.next() else ""
  }

  fun downloadBitmap(imageUrl: String?): Bitmap? {
    return try {
      val conn = URL(imageUrl).openConnection()
      conn.connect()
      val inputStream = conn.getInputStream()
      val bitmap = BitmapFactory.decodeStream(inputStream)
      inputStream.close()
      bitmap
    } catch (e: Exception) {
      Log.e("TAG", "Exception $e")
      null
    }
  }

  fun formatApiDate(createdAt: String?): String? {
    if (!TextUtils.isEmpty(createdAt)) {
      val oldFormat = SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.ENGLISH)
      val newFormat = SimpleDateFormat(Constants.DATE_ONLY_FORMAT, Locale.ENGLISH)
      try {
        val date = oldFormat.parse(createdAt)
        return newFormat.format(date)
      } catch (e: ParseException) {
        e.printStackTrace()
      }
    }
    return null
  }
}
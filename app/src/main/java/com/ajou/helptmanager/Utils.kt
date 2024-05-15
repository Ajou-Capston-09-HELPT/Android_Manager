package com.ajou.helptmanager

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Point
import android.net.Uri
import android.os.SystemClock
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.FragmentActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


fun getFileName(uri: Uri, activity: Activity): String? {
    var result: String? = null
    if (uri.scheme.equals("content")) {
        val cursor: Cursor? = activity.contentResolver.query(uri, null, null, null, null)
        cursor.use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/')
        if (cut != -1) {
            if (cut != null) {
                result = result?.substring(cut + 1)
            }
        }
    }
    return result
}

class OnSingleClickListener(
    private var interval: Int = 600,
    private var onSingleClick: (View) -> Unit
) : View.OnClickListener {

    private var lastClickTime: Long = 0

    override fun onClick(v: View) {
        val elapsedRealtime = SystemClock.elapsedRealtime()
        if ((elapsedRealtime - lastClickTime) < interval) {
            return
        }
        lastClickTime = elapsedRealtime
        onSingleClick(v)
    }

}

fun View.setOnSingleClickListener(onSingleClick: (View) -> Unit) {
    val oneClick = OnSingleClickListener {
        onSingleClick(it)
    }
    setOnClickListener(oneClick)
}

fun getMultipartFile(imageUri: Uri, activity: Activity, key: String): MultipartBody.Part {
    val file = File(absolutelyPath(imageUri, activity)) // path 동일
    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
    val name = file.name
    return MultipartBody.Part.createFormData(key, name, requestFile)
}

private fun absolutelyPath(path: Uri?, activity:Activity ): String {
    var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
    var c: Cursor? = activity.contentResolver?.query(path!!, proj, null, null, null)
    var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    c?.moveToFirst()

    var result = c?.getString(index!!)
    return result!!
} // 절대경로로 변환하는 함수

fun resizeImage(imageUri: Uri, context: Context): Uri {
    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)

    val byteArrayOutputStream = ByteArrayOutputStream()
    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)

    val tempFile = File.createTempFile("resized_image", ".jpg", context.cacheDir)
    val fileOutputStream = FileOutputStream(tempFile)
    fileOutputStream.write(byteArrayOutputStream.toByteArray())
    fileOutputStream.close()

    return Uri.fromFile(tempFile)
}

fun getWindowSize(context: Context): Point{
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)

//    size.x  디바이스 가로 길이
//    size.y  디바이스 세로 길이
    return size
}
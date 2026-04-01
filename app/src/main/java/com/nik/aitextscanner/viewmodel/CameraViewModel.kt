
package com.nik.aitextscanner.viewmodel

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.ExecutorService

class CameraViewModel : ViewModel() {
    var isProcessing by mutableStateOf(false)
        private set

    fun captureAndProcessImage(
        imageCapture: ImageCapture,
        executor: ExecutorService,
        onResult: (String) -> Unit
    ) {
        isProcessing = true
        imageCapture.takePicture(executor, object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val bitmap = image.toBitmap().rotate(image.imageInfo.rotationDegrees.toFloat())
                image.close()
                
                recognizeText(bitmap) { text ->
                    isProcessing = false
                    onResult(text)
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraViewModel", "Capture failed", exception)
                isProcessing = false
                onResult("")
            }
        })
    }

    private fun recognizeText(bitmap: Bitmap, onResult: (String) -> Unit) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                onResult(visionText.text)
            }
            .addOnFailureListener { e ->
                Log.e("CameraViewModel", "Text recognition failed", e)
                onResult("")
            }
    }

    private fun ImageProxy.toBitmap(): Bitmap {
        val buffer = planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
}

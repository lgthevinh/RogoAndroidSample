package com.sample.clone.rogo.fragment.rface

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.os.Bundle
import android.util.Base64
import android.util.Size
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sample.clone.rogo.R
import com.sample.clone.rogo.dialog.common.ErrorDialog
import com.sample.clone.rogo.dialog.common.SuccessDialog
import com.sample.clone.rogo.dialog.rface.RegisterFaceDialog
import com.sample.clone.rogo.rface.RFaceServiceHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import kotlin.math.abs

class RFaceFragment: Fragment() {

    private lateinit var textureView: TextureView
    private lateinit var captureButton: Button
    private lateinit var switchCameraButton: Button

    private lateinit var cameraManager: CameraManager
    private lateinit var cameraDevice: CameraDevice
    private lateinit var captureSession: CameraCaptureSession
    private lateinit var imageReader: ImageReader

    private var cameraId: String = ""
    private var isFrontCamera: Boolean = true
    private var previewSize: Size? = null
    private var isServiceFound: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rface, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraManager = requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager

        CoroutineScope(Dispatchers.IO).launch {
            isServiceFound =  RFaceServiceHandler.scanRFaceService()
            println(isServiceFound)

            withContext(Dispatchers.Main) {
                updateUI(view)
            }
        }

    }

    private fun updateUI(view: View) {
        // Update UI based on isServiceFound
        if (isServiceFound) {
            // RFace service found
            view.findViewById<LinearLayout>(R.id.layout_waiting).visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.layout_code).visibility = View.VISIBLE

            textureView = view.findViewById(R.id.texture_view)
            captureButton = view.findViewById(R.id.btn_take_picture)
            switchCameraButton = view.findViewById(R.id.btn_switch_camera)

            textureView.viewTreeObserver.addOnGlobalLayoutListener {
                // Set TextureView ratio to 4:3
                val width = textureView.width
                val height = width * 4 / 3

                textureView.layoutParams = textureView.layoutParams.apply {
                    this.width = width
                    this.height = height
                }
            }
            textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(
                    surface: SurfaceTexture,
                    width: Int, height: Int) {
                    openCamera()
                }

                override fun onSurfaceTextureSizeChanged(
                    surface: SurfaceTexture,
                    width: Int, height: Int) {
                }

                override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                    return false
                }

                override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
                }
            }

            switchCameraButton.setOnClickListener {
                isFrontCamera = !isFrontCamera
                closeCamera()
                openCamera()
            }

            captureButton.setOnClickListener {
                val successDialog = SuccessDialog("RFace", "Face Registered Successfully!")
                val errorDialog = ErrorDialog("RFace", "Unable to register, please try again.")

                captureImage { image ->
                    val registerFaceDialog = RegisterFaceDialog { name ->
                        CoroutineScope(Dispatchers.IO).launch {
                            var response: Pair<Boolean?, String?> = Pair(null, null)
                            RFaceServiceHandler.registerFace(image, name) { success, message ->
                                response = Pair(success, message)
                            }
                            withContext(Dispatchers.Main) {
                                if (response.first == true) {
                                    successDialog.show(requireActivity().supportFragmentManager, "success_dialog")
                                } else {
                                    errorDialog.show(requireActivity().supportFragmentManager, "error_dialog")
                                }
                            }
                        }
                    }
                    registerFaceDialog.show(requireActivity().supportFragmentManager, "register_face_dialog")
                }
            }
        } else {
            // RFace service not found
            val errorDialog = ErrorDialog("RFace Service not found", "Please make sure RFace Service is running on the same network.")
            errorDialog.show(requireActivity().supportFragmentManager, "error_dialog")
        }
    }

    private fun openCamera() {
        try {
            // Request Camera Permission
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA
                    ), 100)
            }
            cameraId = cameraManager.cameraIdList[if (isFrontCamera) 1 else 0]

            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            previewSize = chooseOptimalSize(map?.getOutputSizes(SurfaceTexture::class.java) ?: arrayOf(), textureView.width, textureView.height)

            imageReader = ImageReader.newInstance(previewSize!!.width, previewSize!!.height, ImageFormat.JPEG, 1)

            cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    cameraDevice = camera
                    createCaptureSession()
                }

                override fun onDisconnected(camera: CameraDevice) {
                    cameraDevice.close()
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    cameraDevice.close()
                }
            }, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun closeCamera() {
        cameraDevice.close()
    }

    @SuppressLint("Recycle")
    private fun createCaptureSession() {
        val texture = textureView.surfaceTexture
        texture?.setDefaultBufferSize(previewSize!!.width, previewSize!!.height)
        val surface = Surface(texture)
        val captureRequestBuilder = cameraDevice.createCaptureRequest(
            CameraDevice.TEMPLATE_PREVIEW).apply {
            addTarget(surface)
        }

        cameraDevice.createCaptureSession(
            listOf(surface, imageReader.surface),
            object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                captureSession = session
                captureSession.setRepeatingRequest(captureRequestBuilder.build(), null, null)
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
            }
        }, null)
    }

    private fun chooseOptimalSize(choices: Array<Size>, width: Int, height: Int): Size {
        return choices.filter { it.width * 4 == it.height * 3 }.minByOrNull {
            abs(it.width - width) + abs(it.height - height)
        } ?: choices[0]
    }

    private fun captureImage(onImageCaptured: (ByteArray) -> Unit) {
        val captureRequestBuilder = cameraDevice.createCaptureRequest(
            CameraDevice.TEMPLATE_STILL_CAPTURE).apply {
            addTarget(imageReader.surface)
            set(CaptureRequest.JPEG_ORIENTATION, 90)
        }
        captureSession.capture(captureRequestBuilder.build(), object: CameraCaptureSession.CaptureCallback() {}, null)

        imageReader.setOnImageAvailableListener({ reader ->
            val image = reader.acquireLatestImage()
            val buffer: ByteBuffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            image.close() // Close image immediately to prevent memory leaks

            // Convert ByteArray to Bitmap
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

            // Rotate the image if needed
            val rotatedBitmap = rotateImage(bitmap, 270) // Change based on your issue

            // Convert rotated image back to ByteArray
            val outputStream = ByteArrayOutputStream()
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            val rotatedBytes = outputStream.toByteArray()

            onImageCaptured(rotatedBytes)

        }, null)
    }

    private fun rotateImage(bitmap: Bitmap, degrees: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}
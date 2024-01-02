import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tab_pj.MyAdapter
import com.example.tab_pj.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.net.Uri
import android.widget.Spinner
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.tab_pj.SharedViewModel
import androidx.lifecycle.Observer
import java.util.*
import android.text.format.DateFormat
import com.example.tab_pj.PhotoItem

class PhotoFragment : Fragment() {

    private val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
    private val GALLERY_REQUEST_CODE = 10
    private lateinit var popupView: View
    private var selectedImageUri: Uri? = null // 선택한 이미지의 URI를 저장하는 변수
    private val photoItems = mutableListOf<PhotoItem>()
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photo, container, false)

        recyclerView = view.findViewById(R.id.recyclerview_main)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = MyAdapter(photoItems)

        val viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel.loadNumData(requireContext()) // JSON 데이터 로드

        val fabPhoto = view.findViewById<ExtendedFloatingActionButton>(R.id.fabPhoto)
        fabPhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // 권한 요청
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
            } else {
                // 이미 권한이 부여되어 있음, 갤러리 열기
                showPhotoPopup()
            }
        }

        return view
    }

    private fun showPhotoPopup() {

        // 팝업 레이아웃을 인플레이트
        popupView = layoutInflater.inflate(R.layout.popup_photo_input, null)

        // 다이얼로그 생성
        val dialog = Dialog(requireContext(), android.R.style.Theme_Material_Light_Dialog_NoActionBar)
        dialog.setContentView(popupView)

        // 팝업 내부의 버튼 처리
        val galleryButton = popupView.findViewById<Button>(R.id.galleryButton)
//        val cameraButton = popupView.findViewById<Button>(R.id.cameraButton)
//        val saveButton = popupView.findViewById<Button>(R.id.saveButton)
        val cancelButton = popupView.findViewById<Button>(R.id.cancelButton)

        // 갤러리로 이동 버튼 클릭 시 동작
        galleryButton.setOnClickListener {
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            galleryIntent.type = "image/*"
            // 이미지 선택 결과를 처리할 액티비티를 지정하여 실행
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)

            dialog.dismiss()
        }

//        // 연락처로 이동 버튼 클릭 시 동작
//        cameraButton.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        // 저장 버튼 클릭 시 동작
//        saveButton.setOnClickListener {
//            dialog.dismiss()
//        }

        // 취소 버튼 클릭 시 동작
        cancelButton.setOnClickListener {
            // 팝업을 닫는다.
            dialog.dismiss()
        }

        // 다이얼로그 크기 설정 (원하는 크기로 조절)
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val dialogWidth = (displayMetrics.widthPixels * 0.8).toInt()
        dialog.window?.setLayout(dialogWidth, LinearLayout.LayoutParams.WRAP_CONTENT)

        // 다이얼로그 보이기
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.data
                if (selectedImageUri != null) {
                    showSavePopup()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSavePopup() {
        // 팝업 레이아웃을 인플레이트
        val popupViewSave = layoutInflater.inflate(R.layout.popup_photo_save_input, null)
        val contactSpinner = popupViewSave.findViewById<Spinner>(R.id.contactSpinner)
        val saveButton = popupViewSave.findViewById<Button>(R.id.saveButton)
        val cancelButton = popupViewSave.findViewById<Button>(R.id.cancelButton)
        val imageView = popupViewSave.findViewById<ImageView>(R.id.imageView)

        // Spinner에 데이터 설정을 위한 ViewModel 사용
        val viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel.getNumData().observe(viewLifecycleOwner, Observer { dataItems ->
            // 'dataItems'를 사용하여 Spinner의 데이터를 설정
            val contactList = dataItems.map { it.title }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, contactList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            contactSpinner.adapter = adapter
        })

        if (selectedImageUri != null) {
            imageView.setImageURI(selectedImageUri)
            imageView.visibility = View.VISIBLE
        }

        // 다이얼로그 생성
        val dialog = Dialog(requireContext(), android.R.style.Theme_Material_Light_Dialog_NoActionBar)
        dialog.setContentView(popupViewSave)

        // 저장 버튼 클릭 시 동작
        saveButton.setOnClickListener {
            val selectedItem = contactSpinner.selectedItem.toString()
            val currentTime = getCurrentTime()
            val newItem = PhotoItem(selectedImageUri, selectedItem, currentTime, R.drawable.ic_launcher_foreground)
            photoItems.add(newItem)

            viewModel.setPhotosForTitle(selectedItem, viewModel.getPhotosForTitle(selectedItem).value.orEmpty() + newItem)

            // Notify the adapter that data has changed
            recyclerView.adapter?.notifyDataSetChanged()

            dialog.dismiss()
        }

        // 취소 버튼 클릭 시 동작
        cancelButton.setOnClickListener {
            // 팝업을 닫는다.
            dialog.dismiss()
        }

        // 다이얼로그 크기 설정 및 보이기
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val dialogWidth = (displayMetrics.widthPixels * 0.8).toInt()
        dialog.window?.setLayout(dialogWidth, LinearLayout.LayoutParams.WRAP_CONTENT)

        // 다이얼로그 보이기
        dialog.show()
    }

    private fun getCurrentTime(): String {
        val cal = Calendar.getInstance()
        return DateFormat.format("yyyy-MM-dd HH:mm", cal).toString()
    }
}
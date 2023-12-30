import android.Manifest
import android.content.pm.PackageManager
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tab_pj.MyAdapter
import com.example.tab_pj.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.widget.ImageView
import android.net.Uri
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class PhotoFragment : Fragment() {

    private val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
    private val GALLERY_REQUEST_CODE = 1001
    private lateinit var popupView: View
    private var selectedImageUri: Uri? = null // 선택한 이미지의 URI를 저장하는 변수

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photo, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview_main)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = MyAdapter()

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
        val contactButton = popupView.findViewById<Button>(R.id.contactButton)
        val saveButton = popupView.findViewById<Button>(R.id.saveButton)
        val cancelButton = popupView.findViewById<Button>(R.id.cancelButton)
        val imageView = popupView.findViewById<ImageView>(R.id.imageView)

        // 갤러리로이동 버튼 클릭 시 동작
        galleryButton.setOnClickListener {
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
            // 이미지 선택 후 팝업을 닫지 않음
        }

        // 연락처로이동 버튼 클릭 시 동작
        contactButton.setOnClickListener {
            dialog.dismiss()
        }

        // 저장 버튼 클릭 시 동작
        saveButton.setOnClickListener {
            dialog.dismiss()
        }

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

        // 이미지 레이아웃이 비어있지 않으면 이미지가 선택된 것으로 처리
        if (selectedImageUri != null) {
            imageView.setImageURI(selectedImageUri)
            imageView.visibility = View.VISIBLE
        }

        // 다이얼로그 보이기
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.data
                if (selectedImageUri != null) {
                    // 이미지 선택 후 팝업을 닫지 않음
                }
            }
        }
    }
}
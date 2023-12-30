import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tab_pj.MyAdapter
import com.example.tab_pj.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
class PhotoFragment : Fragment () {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photo, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview_main)
        val layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManager
        val adapter = MyAdapter()
        recyclerView.adapter = adapter

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            showDialog()
        }

        return view
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_number_input)

        val firstTextField = dialog.findViewById<EditText>(R.id.namefield)
        val secondTextField = dialog.findViewById<EditText>(R.id.numberfield)

        // 여기서 버튼 클릭 리스너를 설정하여 텍스트 필드의 값을 사용할 수 있음

        dialog.show()
    }
}
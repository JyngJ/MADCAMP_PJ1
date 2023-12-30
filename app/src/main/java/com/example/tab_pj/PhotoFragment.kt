import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tab_pj.MyAdapter
import com.example.tab_pj.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject

class PhotoFragment : Fragment() {
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


        return view
    }
}
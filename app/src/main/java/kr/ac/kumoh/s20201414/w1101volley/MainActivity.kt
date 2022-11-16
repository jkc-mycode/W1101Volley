package kr.ac.kumoh.s20201414.w1101volley

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kr.ac.kumoh.s20201414.w1101volley.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var queue: RequestQueue
    private val movies = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        //setContentView(R.layout.activity_main)
        setContentView(binding.root);

        queue = Volley.newRequestQueue(application)
        binding.btnConnect.setOnClickListener{
            val url = "https://yts.torrentbay.to/api/v2/list_movies.json?sort=rating&limit=30"
            val request = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { //처리가 잘됐을 때 실행
                    movies.clear()
                    parseJson(it)
                    //Toast.makeText(application, movies.toString(), Toast.LENGTH_LONG).show()
                    binding.listMovies.adapter = ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        movies
                    )
                },
                { //에러가 났을 때 실행
                    Toast.makeText(application, it.toString(), Toast.LENGTH_LONG).show()
                }
            )
            queue.add(request)
        }
    }
    private fun parseJson(obj: JSONObject){
        val data = obj.getJSONObject("data")
        val items = data.getJSONArray("movies")
        for(i in 0 until items.length()){
            val item: JSONObject = items[i] as JSONObject //타입 캐스팅
            val title = item.getString("title_long")
            movies.add(title)
        }
    }
}
package com.example.searchbar

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.example.searchbar.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val baseURL: String = "https://api.spacexdata.com/v3/"
    val  imageList: ArrayList<SlideModel> = ArrayList()
    lateinit var textViewResult: TextView
    lateinit var imageSlider: ImageSlider
    lateinit var playButton: Button
    lateinit var speedButton: Button
    lateinit var restartButton: Button
    lateinit var binding: ActivityMainBinding


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       val user = arrayOf("Abhay","Joseph","Maria","Avni","Apoorva","Chris","David","Kaira","Dwayne","Christopher",
           "Jim","Russel","Donald","Brack","Vladimir")

        val userAdapter : ArrayAdapter<String> = ArrayAdapter(
            this,android.R.layout.simple_list_item_1,
            user)

        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val jsonPlaceHolderApi: JsonPlaceHolder = retrofit.create(JsonPlaceHolder::class.java)

        val call: Call<List<Posts>> = jsonPlaceHolderApi.getPosts()

        imageSlider = findViewById(R.id.image_slider)
        playButton = findViewById(R.id.playBtn)
        speedButton = findViewById(R.id.speedBtn)
        restartButton = findViewById(R.id.restartBtn)
        playBtn.text = "PLAY"
        speedBtn.text = "X2"
        restartBtn.text = "Restart"

        binding.userList.adapter = userAdapter

        binding.searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                if (user.contains(query)){

                    userAdapter.filter.filter(query)

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                userAdapter.filter.filter(newText)
                return false
            }


        })


        var speed: Long = 3000
        var clickCounter = 2

        speedButton.setOnClickListener {
            clickCounter++
            if (clickCounter < 6){
                speed /= 2

            }else {
                speed = 3000
                clickCounter = 2
            }
            speedButton.text = "X$clickCounter"
            imageSlider.startSliding(speed)
        }

        playButton.setOnClickListener {
            if (playButton.text == "PLAY") {
                imageSlider.stopSliding()
                playButton.text = "PAUSE"
            }else  {
                imageSlider.startSliding()
                playButton.text = "PLAY"
                speedButton.text = "X2"
            }

        }

        call.enqueue(object : Callback<List<Posts>> {

            override fun onResponse(call: Call<List<Posts>>, response: Response<List<Posts>>) {

                if (!response.isSuccessful) {
                    textViewResult.text = "Code: " + response.code()
                    return
                }
                val post1: List<Posts>? = response.body()
                //textViewResult.text = post1.toString()


                if (post1 != null) {
                    for (post in post1) {
                        var content: String?
                        content = post.image
                        imageList.add(SlideModel("$content", " "))
                        imageSlider.setImageList(imageList)
                    }
                }
            }

            override fun onFailure(call: Call<List<Posts>>, t: Throwable) {

                textViewResult.text = t.message

            }

        })
}
}
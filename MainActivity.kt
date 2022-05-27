package com.example.sharememe

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    var currentImageUrl :String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }

    @SuppressLint("WrongViewCast")
    private fun loadMeme()
    {
        // Instantiate the RequestQueue.
        val prg=findViewById<ProgressBar>(R.id.progressBar)
        prg.visibility= View.VISIBLE
//        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"


        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener { response ->
                currentImageUrl= response.getString("url")
                val meme= findViewById<ImageView>(R.id.memeImage)
                Glide.with(this).load(currentImageUrl).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        prg.visibility= View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        prg.visibility= View.GONE
                        return false
                    }
                }).into(meme)
            },
            Response.ErrorListener {

            })

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }
    fun shareMeme(view: View) {
        val intent= Intent(Intent.ACTION_SEND)
        intent.type= "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey, checkout this cool meme $currentImageUrl")
        val chooser= Intent.createChooser(intent,"share this meme using....")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}
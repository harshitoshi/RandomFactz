package com.example.randoms

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.Volley
import com.android.volley.VolleyError
import com.android.volley.Response
import org.json.JSONObject

import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    var imageurl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        load()
    }

    private fun load() {
        val progressBar = findViewById<ProgressBar>(R.id.progbar)
        progressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)
        val image = findViewById<ImageView>(R.id.imageView2)
        val url = "https://meme-api.herokuapp.com/gimme"
        val jsonReq = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                imageurl = response.getString("url")
                Glide.with(this).load(imageurl).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@MainActivity,
                            "Something wrong happened",
                            Toast.LENGTH_SHORT
                        ).show()
                        return false
                    }
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                }).into(image)
            },
            {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                finish()
            })
        queue.add(jsonReq)
    }

    fun nextFact(view: android.view.View) {
        load()
    }

    fun share(view: android.view.View) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Checkout this meme $imageurl")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Share using..")
        startActivity(shareIntent)
    }
}


package com.example.randomwallpaper

import android.app.AlertDialog
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.randomwallpaper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.randomImage) {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT)
            requestLayout()
        }

        setNewImage() // Initial image

        with(binding.getRandomImageButton) {
            setOnClickListener {
                setNewImage()
            }
            setOnLongClickListener {
                // Need to validate image
                openSetWallpaperDialog()
            }
        }
    }

    private fun setNewImage() {
        Glide.with(this)
            .load("https://source.unsplash.com/random/800x600")
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(binding.randomImage.drawable)
            .into(binding.randomImage)
    }

    private fun openSetWallpaperDialog(): Boolean {
        showSetWallpaperDialog(this)
        return true
    }

    private fun showSetWallpaperDialog(context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_set_wallpaper, null)
        val btnSetWallpaper: Button = dialogView.findViewById(R.id.btnSetWallpaper)

        val alertDialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(true)

        val alertDialog = alertDialogBuilder.create()

        // Set onClickListener for the "Set as Wallpaper" button
        btnSetWallpaper.setOnClickListener {
            setWallpaper(context)
            alertDialog.dismiss()
        }

        // Show the dialog
        alertDialog.show()
    }

    private fun setWallpaper(context: Context) {
        try {
            val wallpaperManager = WallpaperManager.getInstance(context)

            // Get the drawable from the ImageView
            val drawable = binding.randomImage.drawable

            // Convert the drawable to a Bitmap
            val bitmap = when (drawable) {
                is BitmapDrawable -> drawable.bitmap
                else -> {
                    // If it's not a BitmapDrawable, create a new Bitmap
                    val width = drawable.intrinsicWidth
                    val height = drawable.intrinsicHeight

                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    bitmap
                }
            }

            wallpaperManager.setBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
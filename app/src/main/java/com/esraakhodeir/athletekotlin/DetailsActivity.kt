package com.esraakhodeir.athletekotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.esraakhodeir.athletekotlin.MainActivity.Companion.athletesList
import com.squareup.picasso.Picasso

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        var itemImage: ImageView = findViewById(R.id.athlete_image)
        var itemTitle: TextView = findViewById(R.id.athlete_title)
        var itemDetail: TextView = findViewById(R.id.athlete_detail)

        itemTitle.text = athletesList[Selector.index].name
        itemDetail.text = athletesList[Selector.index].brief
        if (athletesList[Selector.index].image != "") {
            Picasso.get().load(athletesList[Selector.index].image).into(itemImage)
        }else
            itemImage.visibility = View.GONE

    }
}

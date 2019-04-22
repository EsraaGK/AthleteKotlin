package com.esraakhodeir.athletekotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    companion object {
        var athletesList = ArrayList<Athlete>()
    }

    val BASE_URL = "https://gist.githubusercontent.com/"
    var layoutManager: RecyclerView.LayoutManager? = null
    var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager

        adapter = RecyclerAdapter(athletesList)
        recycler_view.adapter = adapter

        getAthletes()

    }


    private fun getAthletes() {

        var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var api = retrofit.create(Api::class.java)
        var call = api.getAthletes()
        call.enqueue(object : Callback<AthletesList> {
            override fun onResponse(call: Call<AthletesList>?, response: Response<AthletesList>?) {

                var athletes = response?.body()
                var athlete = athletes?.athletes
                var length = athlete!!.size

                for (i in 0 until length) {
                    athletesList.add(athlete[i])
                }
                adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<AthletesList>, t: Throwable?) {

                Log.v("Error", t.toString())
            }
        })
    }

    class RecyclerAdapter(private val athletesList: ArrayList<Athlete>) :
        RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
            val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.athlete_card, viewGroup, false)
            return ViewHolder(v).listen { pos, type ->
                Selector.index = pos
                val intent = Intent(v.context, DetailsActivity::class.java)
                v.context.startActivity(intent)
            }
        }

        fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
            itemView.setOnClickListener {
                event.invoke(getAdapterPosition(), getItemViewType())
            }
            return this
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
            viewHolder.itemTitle.text = athletesList[i].name
            viewHolder.itemDetail.text = athletesList[i].brief
            if (athletesList[i].image != "") {
                Picasso.get().load(athletesList[i].image).into(viewHolder.itemImage)
            } else
                viewHolder.itemImage.visibility = View.GONE
        }

        override fun getItemCount(): Int {
            return athletesList.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var itemImage: ImageView
            var itemTitle: TextView
            var itemDetail: TextView

            init {
                itemImage = itemView.findViewById(R.id.item_image)
                itemTitle = itemView.findViewById(R.id.item_title)
                itemDetail = itemView.findViewById(R.id.item_detail)
            }
        }
    }
}
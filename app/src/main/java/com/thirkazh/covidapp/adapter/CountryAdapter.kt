package com.thirkazh.covidapp.adapter

import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thirkazh.covidapp.R
import com.thirkazh.covidapp.model.Negara
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.list_country.view.*
import java.util.*
import kotlin.collections.ArrayList

class CountryAdapter (

    private var negara: ArrayList<Negara>,
    private val clickListener: (Negara) -> Unit

) : RecyclerView.Adapter<CountryAdapter.ViewHolder>(), Filterable{

    var countryFilterlists= ArrayList<Negara>()
    init {
        countryFilterlists= negara
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.list_country, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return countryFilterlists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(countryFilterlists[position], clickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(negara: Negara, clickListener: (Negara) -> Unit) {
            val country: TextView= itemView.countryName
            val cTotalCase: TextView = itemView.county_total_case
            val cTotalRecovered: TextView = itemView.county_total_recovered
            val cTotalDeaths: TextView = itemView.county_total_deaths
            val flag: CircleImageView = itemView.img_flag_circle

            val formatter: NumberFormat = DecimalFormat("#,###")

            country.countryName.text = negara.Country
            cTotalCase.county_total_case.text = formatter.format(negara.TotalConfirmed?.toDouble())
            cTotalRecovered.county_total_recovered.text = formatter.format(negara.TotalRecovered?.toDouble())
            cTotalDeaths.county_total_deaths.text = formatter.format(negara.TotalDeaths?.toDouble())
            Glide.with(itemView).load("https://www.countryflags.io/" + negara.CountryCode + "/flat/16.png").into(flag)

            country.setOnClickListener {clickListener(negara)}
            cTotalCase.setOnClickListener { clickListener(negara)}
            cTotalRecovered.setOnClickListener { clickListener(negara)}
            cTotalDeaths.setOnClickListener { clickListener(negara)}
            flag.setOnClickListener { clickListener(negara)}
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch: String = constraint.toString()
                countryFilterlists = if (charSearch.isEmpty()) {
                    negara
                }else {
                    val resulList = ArrayList<Negara>()
                    for (row in negara){
                        val search = row.Country?.toLowerCase(Locale.ROOT) ?:""
                        if (search.contains(charSearch.toLowerCase(Locale.ROOT))){
                            resulList.add(row)
                        }
                    }
                    resulList
                }
                val filerResult = FilterResults()
                filerResult.values = countryFilterlists
                return filerResult
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                countryFilterlists = results?.values as ArrayList<Negara>
                notifyDataSetChanged()
            }
        }
    }

}
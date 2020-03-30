package com.bhanupro.covid_19.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.bhanupro.covid_19.R
import com.bhanupro.covid_19.model.CovidData
import com.bhanupro.covid_19.viewmodel.MainViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_covid_list.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class CovidListFragment : BottomSheetDialogFragment() {

    private lateinit var covidAdapter:CovidDataAdapter

    private val viewModel:MainViewModel by sharedViewModel()

    var onButtonClick: (data:CovidData) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_covid_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }
    fun show(fragmentManager: FragmentManager, onClick: (data:CovidData) -> Unit){
        try {
            onButtonClick = onClick
            show(fragmentManager,"covid_list_fragment")
        } catch (e: Exception) {

        }
    }
    private fun observeViewModel(){
        viewModel.getWorldData().observe(viewLifecycleOwner, Observer {
            if (it != null){
                setUpRv(it)
            }
        })
    }
    private fun setUpRv(data: ArrayList<CovidData>){
        covidAdapter = CovidDataAdapter(data)
        covid_list_rv.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        covid_list_rv.adapter = covidAdapter
        covid_list_rv.itemAnimator = DefaultItemAnimator()
        covidAdapter.notifyDataSetChanged()
    }

    inner class CovidDataAdapter(val results:ArrayList<CovidData>):RecyclerView.Adapter<CovidDataAdapter.ViewHolder>(){

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CovidDataAdapter.ViewHolder {
           val view = LayoutInflater.from(requireContext()).inflate(R.layout.covid_data_list_item,parent,false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
           return results.size
        }

        override fun onBindViewHolder(holder: CovidDataAdapter.ViewHolder, position: Int) {
            val covidData = results[position]
            holder.bindData(covidData)
            holder.root.setOnClickListener {
                onButtonClick(covidData)
            }
        }

        inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
            private val countryTv:TextView = itemView.findViewById(R.id.country_name)
            private val totalCases:TextView = itemView.findViewById(R.id.total_cases)
            private val countryLogo:ImageView = itemView.findViewById(R.id.country_logo)
            val root:MaterialCardView  = itemView.findViewById(R.id.covid_cv)

            fun bindData(data:CovidData){
                countryTv.text = data.country
                totalCases.text = NumberFormat.getNumberInstance(Locale.US).format(data.cases).toString()
                Glide.with(countryLogo).load(data.countryInfo.flag).placeholder(R.drawable.logo).into(countryLogo)
            }
        }

    }

}

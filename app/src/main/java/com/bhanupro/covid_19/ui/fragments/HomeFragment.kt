package com.bhanupro.covid_19.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController

import com.bhanupro.covid_19.R
import com.bhanupro.covid_19.extentions.toDate
import com.bhanupro.covid_19.model.CovidData
import com.bhanupro.covid_19.viewmodel.MainViewModel
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow
import org.osmdroid.views.overlay.infowindow.InfoWindow
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private val viewModel:MainViewModel by sharedViewModel()

    private lateinit var map:MapView
    private val covidBottomSheet = CovidListFragment()

    inner class Callback : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            activity?.finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, Callback())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map = view.findViewById(R.id.map_view)
        configMaps()
        arrowClick()
        currentLocation(20.0,77.0)
        observeViewModel()
    }
    private fun configMaps(){
        Configuration.getInstance().load(requireContext().applicationContext,PreferenceManager.getDefaultSharedPreferences(requireContext()))
        map.setTileSource(TileSourceFactory.MAPNIK)
    }
    private fun arrowClick(){
        val anim = AnimationUtils.loadAnimation(context, R.anim.anim_blink)
        arrow_up_img?.startAnimation(anim)

        arrow_up_img?.setOnClickListener {
            showBottomSheet()
        }
    }
    private fun observeViewModel(){
        viewModel.getWorldData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null){
                setCovidMarkers(it)
            }
        })
    }
    private fun showBottomSheet(){
        covidBottomSheet.show(childFragmentManager){
            covidBottomSheet.dismiss()
            currentLocation(it.countryInfo.lat,it.countryInfo.long)
        }
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
        showBottomSheet()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
    private fun currentLocation(lat:Double,long: Double){
        map.controller.apply {
            setZoom(6.5)
            setCenter(GeoPoint(lat,long))
        }
    }
    private fun makePolygon(lat:Double,long:Double){
        //currentLocation(lat,long)
        //map.invalidate()
        val polygon = Polygon().apply {
            outlinePaint.color = Color.parseColor("#e33e41")
            outlinePaint.strokeWidth = 2.0f
            fillPaint.color = Color.parseColor("#651b14")
            fillPaint.alpha = 95
        }
        val radius = 250000
        val circlePoints = ArrayList<GeoPoint>()
        for (i in 0..360){
            circlePoints.add(GeoPoint(lat,long).destinationPoint(radius.toDouble(),i.toDouble()))
        }
        polygon.points = circlePoints
        map.overlays.add(polygon)

    }
    private fun setCovidMarkers(list:ArrayList<CovidData>){
        list.forEach {
            setMarker(it)
        }
    }
    private fun setMarker(data: CovidData){
        makePolygon(data.countryInfo.lat,data.countryInfo.long)

        val covidWindow = CovidMapWindow(R.layout.maps_bubble_layout,map,data)
        val marker = Marker(map)
        marker.apply {
            position = GeoPoint(data.countryInfo.lat,data.countryInfo.long)
            icon = ContextCompat.getDrawable(requireContext(),R.drawable.ic_place)
            setAnchor(Marker.ANCHOR_CENTER,1.0f)
            infoWindow = covidWindow
        }
        map.overlays.add(marker)
        marker.title = data.country
    }

    inner class CovidMapWindow(val resId:Int,val map:MapView,val covidData: CovidData):InfoWindow(resId,map){
        override fun onOpen(item: Any?) {
            val layout = mView.findViewById<MaterialCardView>(R.id.bubble_layout)
            val country = mView.findViewById<TextView>(R.id.country_name_bubble)
            val totalCases = mView.findViewById<TextView>(R.id.total_cases_bubble)
            val activeCases = mView.findViewById<TextView>(R.id.active_cases_bubble)
            val fatalCases = mView.findViewById<TextView>(R.id.fatal_cases_bubble)
            val recoveredCases = mView.findViewById<TextView>(R.id.recovered_cases_bubble)
            val date = mView.findViewById<TextView>(R.id.updated_date_bubble)

            country.text = covidData.country
            totalCases.text = NumberFormat.getIntegerInstance(Locale.US).format(covidData.cases).toString()
            activeCases.text = NumberFormat.getIntegerInstance(Locale.US).format(covidData.active).toString()
            fatalCases.text = NumberFormat.getIntegerInstance(Locale.US).format(covidData.critical).toString()
            recoveredCases.text = NumberFormat.getIntegerInstance(Locale.US).format(covidData.recovered).toString()
            date.text = covidData.updated.toDate()
        }

        override fun onClose() {
            super.close()
            mIsVisible = false
        }

    }

}

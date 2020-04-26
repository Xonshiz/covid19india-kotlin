package com.xonshiz.covid_19tracker

import android.R.layout
import android.content.Context
import android.graphics.Color
import android.icu.text.MeasureFormat
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.xonshiz.covid_19tracker.AppData.ApiEndPoints
import com.xonshiz.covid_19tracker.interfaces.CovidRestService
import com.xonshiz.covid_19tracker.models.MainDataModel
import com.xonshiz.covid_19tracker.models.Statewise
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.state_list_ticket.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    var listOfStatewise = ArrayList<Statewise>()
    var adapter:StateWiseAdapter?= null
    var mainContext: Context?= null


    /*
    fun init(statewiseResult: List<Statewise>) {
        val stk = table_main as TableLayout
        val tbrow0 = TableRow(this)
        val tv0 = TextView(this)
        tv0.text = "    STATE"
        tv0.setTextColor(Color.BLACK)
        tbrow0.addView(tv0)
        val tv1 = TextView(this)
        tv1.text = "CNF  "
        tv1.setTextColor(Color.parseColor("#FF073A"))
        tbrow0.addView(tv1)
        val tv2 = TextView(this)
        tv2.text = " REC "
        tv2.setTextColor(Color.parseColor("#007bff"))
        tbrow0.addView(tv2)
        val tv3 = TextView(this)
        tv3.text = " DEATHS "
        tv3.setTextColor(Color.parseColor("#6c757d"))
        tbrow0.addView(tv3)
        stk.addView(tbrow0)
        for (i in statewiseResult) {
            if (i.state != "Total"){
                val tbrow = TableRow(this)
                val t1v = TextView(this)
                t1v.text = i.state
                t1v.setTextColor(Color.BLACK)
                t1v.gravity = Gravity.CENTER
                tbrow.addView(t1v)
                val t2v = TextView(this)
                t2v.text = i.active
                t2v.setTextColor(Color.BLACK)
                t2v.gravity = Gravity.CENTER
                tbrow.addView(t2v)
                val t3v = TextView(this)
                t3v.text = i.recovered
                t3v.setTextColor(Color.BLACK)
                t3v.gravity = Gravity.CENTER
                tbrow.addView(t3v)
                val t4v = TextView(this)
                t4v.text = i.deaths
                t4v.setTextColor(Color.BLACK)
                t4v.gravity = Gravity.CENTER
                tbrow.addView(t4v)
                stk.addView(tbrow)
            }
        }
    }
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.mainContext = this;
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiEndPoints().baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(CovidRestService::class.java)
        val call = service.getMainData()
        call.enqueue(object : Callback<MainDataModel> {
            override fun onResponse(call: Call<MainDataModel>, response: Response<MainDataModel>) {
                if (response.code() == 200) {
                    //Toast.makeText(applicationContext, "Passed", Toast.LENGTH_LONG).show()
                    val lastIndex = response.body().cases_time_series.lastIndex;
                    totalConfirmedCases.text = response.body().cases_time_series[lastIndex].totalconfirmed
                    totalRecoveredCases.text = response.body().cases_time_series[lastIndex].totalrecovered
                    totalDeceasedCases.text = response.body().cases_time_series[lastIndex].totaldeceased

                    todayConfirmedCases.text = response.body().cases_time_series[lastIndex].dailyconfirmed
                    todayRecoveredCases.text = response.body().cases_time_series[lastIndex].dailyrecovered
                    todayDeceasedCases.text = response.body().cases_time_series[lastIndex].dailydeceased
                    listOfStatewise = response.body().statewise
                    populateListView()
                    //init(response.body().statewise)
                } else {
                    Toast.makeText(applicationContext, response.message(), Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<MainDataModel>, t: Throwable) {
                Toast.makeText(applicationContext, "Failed! Check Whether Your Internet Is Working!", Toast.LENGTH_LONG).show()
            }
        })

//        while (listOfStatewise.size == 0){
//            if(listOfStatewise.size != 0){
//                adapter = StateWiseAdapter(this, listOfStatewise)
//                casesListView.adapter = adapter
//            }
//        }
    }

    fun populateListView(){
        var breakLoop = false
        for (item in listOfStatewise){
            if(item.state.toLowerCase().contains("total")){
                item.state = "State Name"
                item.confirmed = "CNF"
                item.recovered = "REC"
                item.deaths = "DEC"
                break
            }
        }
        adapter = StateWiseAdapter(this, listOfStatewise)
        casesListView.adapter = adapter
    }
}

class StateWiseAdapter:BaseAdapter{
    var listOfStatewise = ArrayList<Statewise>()
    var context:Context?= null

    constructor(context: Context,listOfStatewise: ArrayList<Statewise>):super(){
        this.listOfStatewise = listOfStatewise
        this.context = context
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var state = listOfStatewise[position]
        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var myView = inflator.inflate(R.layout.state_list_ticket, null)
        myView.confirmedCasesCount.text = state.confirmed!!
        myView.recoveredCasesCount.text = state.recovered!!
        myView.deceasedCasesCount.text = state.deaths!!
        myView.stateName.text = state.state!!
        if(state.state != "State Name"){
            myView.confirmedCasesCount.width = 20
            myView.recoveredCasesCount.width = 20
            myView.deceasedCasesCount.width = 20
        } else{
            myView.stateName.setTextColor(Color.parseColor("#000000"))
            myView.confirmedCasesCount.setTextColor(Color.parseColor("#FF073A"))
            myView.recoveredCasesCount.setTextColor(Color.parseColor("#007bff"))
            myView.deceasedCasesCount.setTextColor(Color.parseColor("#6c757d"))
        }
        return myView
    }

    override fun getItem(position: Int): Any {
        return listOfStatewise[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listOfStatewise.size
    }

}

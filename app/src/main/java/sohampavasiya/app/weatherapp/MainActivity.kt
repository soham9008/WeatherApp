package sohampavasiya.app.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sohampavasiya.app.weatherapp.databinding.ActivityMainBinding
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.days

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    // api key 3c170b590ab59ee00121262c09f89410
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        serchCity()


        fetchWeatherData("surat")

    }

    private fun serchCity() {
        val serchView = binding.searchBar
        serchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWeatherData(CityName : String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response = retrofit.getWeatherData(CityName,"3c170b590ab59ee00121262c09f89410", "metric")
        response.enqueue(object : Callback<WeatherApp>{
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){


                    val weatherMain = responseBody.main
                    val weatherWind = responseBody.wind
                    val weather = responseBody.weather
                    val weatherHumidity = weatherMain.humidity
                    val weatherWindSpeed = weatherWind.speed
                    val tempMax = weatherMain.temp_max
                    val tempMin = weatherMain.temp_min
                    val temp = weatherMain.temp
                    val weatherSunny = weather.firstOrNull()?.main?: "unknow"
                    val sunRise = responseBody.sys.sunrise
                    val sunSet = responseBody.sys.sunset
                    val seeLevel = responseBody.main.pressure


                    binding.humidity.text = weatherHumidity.toString()
                    binding.windSpeed.text = weatherWindSpeed.toString()
                    binding.maxTemp.text = "Max : $tempMax °C"
                    binding.minTemp.text = "Min : $tempMax °C"
                    binding.temp.text = "$temp°C"
                    binding.dayColud.text = weatherSunny
                    binding.sunrise.text = sunRise.toString()
                    binding.sunset.text = sunSet.toString()
                    binding.seaLevel.text = seeLevel.toString()
                    binding.rainConditions.text = weatherSunny.toString()
                    binding.city.text = "$CityName"
                    binding.todayWeekday.text = todaysDay(System.currentTimeMillis())
                    binding.todayDate.text = date()

                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {

            }

        })




    }

    private fun date() : String {

        val sdf = SimpleDateFormat("dd MMMM YYYY", Locale.getDefault())
        return sdf.format(Date())
    }

    fun todaysDay (timestamp: Long) : String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }
}
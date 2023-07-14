package com.example.misheroes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.misheroes.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    lateinit var heroes: List<CharacterResponse>
    lateinit var heroeAdapter: HeroeAdapter

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchBreed.setOnQueryTextListener(this)
    }

    private fun initCharacter(heroes: List<CharacterResponse>, powerStats: List<CharacterPowerStats>, images: List<CharacterImage>) {
        // Combina los datos de CharacterResponse, CharacterPowerStats y CharacterImage según el ID del personaje
        val combinedData = mutableListOf<Triple<CharacterResponse, CharacterPowerStats, CharacterImage>>()
        for (i in heroes.indices) {
            val character = heroes[i]
            val powerStat = powerStats[i]
            val image = images.find { it.id == character.id }
            if (image != null) {
                combinedData.add(Triple(character, powerStat, image))
            }
        }

        // Configura el adaptador con los datos combinados
        heroeAdapter = HeroeAdapter(combinedData)
        binding.rvHeroe.setHasFixedSize(true)
        binding.rvHeroe.layoutManager = LinearLayoutManager(this)
        binding.rvHeroe.adapter = heroeAdapter
    }


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://superheroapi.com/api/10159916783383579/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        searchByName(query.lowercase())
        return true
    }

    private fun searchByName(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val searchCall = getRetrofit().create(APIService::class.java).searchCharacterByName(query).execute()
            val searchResponse = searchCall.body() as SearchResponse?

            val powerStatsList = mutableListOf<CharacterPowerStats>()
            val imageList = mutableListOf<CharacterImage>()
            if (searchResponse?.response == "success") {
                for (character in searchResponse.results) {
                    val powerStatsCall = getRetrofit().create(APIService::class.java).getPowerStatsById(character.id).execute()
                    val powerStatsResponse = powerStatsCall.body() as CharacterPowerStats?
                    powerStatsResponse?.let {
                        powerStatsList.add(it)
                    }
                    val imageCall = getRetrofit().create(APIService::class.java).getImageById(character.id).execute()
                    val imageResponse = imageCall.body() as CharacterImage?
                    imageResponse?.let {
                        imageList.add(it)
                    }
                }
            }

            runOnUiThread {
                if (searchResponse?.response == "success") {
                    initCharacter(searchResponse.results, powerStatsList, imageList)
                } else {
                    showErrorDialog()
                }
                hideKeyboard()
            }

        }
    }



    private fun showErrorDialog() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken, 0)
    }
}

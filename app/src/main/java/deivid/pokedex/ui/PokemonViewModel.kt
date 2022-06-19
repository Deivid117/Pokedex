package deivid.pokedex.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import deivid.pokedex.api.Pokemon
import deivid.pokedex.service.PokeApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonViewModel : ViewModel() {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: PokeApi = retrofit.create(PokeApi::class.java)

    val pokemonInfo = MutableLiveData<Pokemon>()

    fun getPokemon(id: Int){
        val call = service.getPokemonInfo(id)

        call.enqueue(object : Callback<Pokemon>{
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                response.body()?.let { pokemon -> pokemonInfo.postValue(pokemon) }
            }

            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}
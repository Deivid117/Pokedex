package deivid.pokedex.service

import retrofit2.Call
import deivid.pokedex.api.PokeApiResponse
import deivid.pokedex.api.Pokemon
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {
    //Información de cada pokemon
    @GET("pokemon/{id}")
    fun getPokemonInfo(@Path("id") id: Int): Call<Pokemon>
    //Lista de los pokemon
    @GET("pokemon")
    fun getPokemonList(@Query("limit") limit: Int, @Query("offset") offset: Int): Call<PokeApiResponse>
}
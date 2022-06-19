package deivid.pokedex.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import deivid.pokedex.R
import java.util.*

class PokemonActivity : AppCompatActivity() {

    lateinit var viewModel: PokemonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon)

        viewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)

        pokeInfo();
    }

    fun idRandom(valor: IntRange) : Int {
        val r = Random()
        val valorRandom = r.nextInt(valor.last - valor.first) + valor.first
        return valorRandom
    }

    private fun pokeInfo(){
        val id = idRandom(0..100)
        val name: TextView = findViewById(R.id.namePoke) as TextView
        val image: ImageView = findViewById(R.id.imagePoke) as ImageView
        val height: TextView = findViewById(R.id.heightPoke) as TextView
        val weight: TextView = findViewById(R.id.weightPoke) as TextView

        viewModel.getPokemon(id)

        viewModel.pokemonInfo.observe(this, {pokemon ->
            name.text = pokemon.name
            height.text = "${pokemon.height/10.0} M"
            weight.text = "${pokemon.weight/10.0} KG"
            Glide.with(this).load(pokemon.sprites.front_shiny).into(image)
        })
    }
}
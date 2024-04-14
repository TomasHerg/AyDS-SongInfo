package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room.databaseBuilder
import ayds.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

class OtherInfoWindow : Activity() {
    private var otherInfoTextPanel: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        otherInfoTextPanel = findViewById(R.id.textPane1)
        open(intent.getStringExtra("artistName"))
    }

    private fun getARtistInfo(artistName: String?) {

        // create
        val retrofit = initializeRetrofitBuilder()
        val lastFmAPI = retrofit.create(LastFmAPI::class.java)
        Log.e("TAG", "artistName $artistName")
        Thread {
            val article = dataBase!!.ArticleDao().getArticleByArtistName(artistName!!)
            var articleBiography = ""
            if (article != null) { // exists in db
                articleBiography = "[*]" + article.biography
                val urlString = article.articleUrl
                findViewById<View>(R.id.openUrlButton1).setOnClickListener {
                    val intent = initializeIntent(urlString)
                }
            } else { // get from service
                val callResponse: Response<String>
                try {
                    callResponse = lastFmAPI.getArtistInfo(artistName).execute()
                    Log.e("TAG", "JSON " + callResponse.body())
                    val gson = Gson()
                    val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
                    val artist = jobj["artist"].getAsJsonObject()
                    val bio = artist["bio"].getAsJsonObject()
                    val extract = bio["content"]
                    val url = artist["url"]
                    if (extract == null) {
                        articleBiography = "No Results"
                    } else {
                        articleBiography = extract.asString.replace("\\n", "\n")
                        articleBiography = textToHtml(articleBiography, artistName)


                        // save to DB  <o/
                        val biographyForDB = articleBiography
                        saveBiographyOnDB(artistName, biographyForDB, url.asString)
                    }
                    val urlString = url.asString
                    findViewById<View>(R.id.openUrlButton1).setOnClickListener {
                        val intent = initializeIntent(urlString)
                    }
                } catch (e1: IOException) {
                    Log.e("TAG", "Error $e1")
                    e1.printStackTrace()
                }
            }
            val imageUrl =
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
            Log.e("TAG", "Get Image from $imageUrl")
            val finalText = articleBiography
            showInfoOnUI(imageUrl, otherInfoTextPanel, finalText)
        }.start()
    }

    private var dataBase: ArticleDatabase? = null
    private fun open(artist: String?) {
        dataBase =
            databaseBuilder(this, ArticleDatabase::class.java, "database-name-thename").build()
        threadGetArticleByArtisName(dataBase)
        getARtistInfo(artist)
    }

    private fun threadGetArticleByArtisName(dataBase: ArticleDatabase?){
        Thread {
            dataBase!!.ArticleDao().insertArticle(ArticleEntity("test", "sarasa", ""))
            Log.e("TAG", "" + dataBase!!.ArticleDao().getArticleByArtistName("test"))
            Log.e("TAG", "" + dataBase!!.ArticleDao().getArticleByArtistName("nada"))
        }.start()
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        fun textToHtml(text: String, term: String?): String {
            val builder = initializeStringBuilder()
            val textWithBold = setTextWithBold(text, term)
            return appendTextOnBuilder(builder, textWithBold)
        }

        private fun initializeStringBuilder(): StringBuilder{
            val builder = StringBuilder()
            builder.append("<html><div width=400>")
            builder.append("<font face=\"arial\">")
            return builder
        }

        private fun setTextWithBold(text: String, term: String?) : String = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
            )

        private fun appendTextOnBuilder(builder: StringBuilder, textWithBold: String): String{
            builder.append(textWithBold)
            builder.append("</font></div></html>")
            return builder.toString()
        }
    }

    private fun initializeRetrofitBuilder() : Retrofit {
        return Retrofit.Builder()
                 .baseUrl("https://ws.audioscrobbler.com/2.0/")
                 .addConverterFactory(ScalarsConverterFactory.create())
                 .build()
    }

    private fun initializeIntent(url: String): Intent{
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
        return intent;
    }

    private fun saveBiographyOnDB(artistName: String, biographyForDB: String, url: String){
        Thread {
            dataBase!!.ArticleDao().insertArticle(
                ArticleEntity(
                    artistName, biographyForDB, url
                )
            )
        }.start()
    }

    private fun showInfoOnUI(imageUrl : String, otherInfoTextPanel: TextView?, finalText: String){
        runOnUiThread {
            showImageUrl(imageUrl)
            setTextOnPanel(otherInfoTextPanel, finalText)
        }
    }

    private fun showImageUrl(imageUrl: String){
        Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView1) as ImageView)
    }

    private fun setTextOnPanel(otherInfoTextPanel: TextView?, finalText: String){
        otherInfoTextPanel!!.text= (Html.fromHtml(finalText))
    }

}

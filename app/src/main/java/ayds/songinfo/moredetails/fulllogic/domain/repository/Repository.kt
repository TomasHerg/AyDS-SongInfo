package ayds.songinfo.moredetails.fulllogic.domain.repository

import ayds.songinfo.moredetails.fulllogic.data.external.LastFMAPI
import ayds.songinfo.moredetails.fulllogic.data.local.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.data.local.ArticleEntity
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.IOException
import ayds.songinfo.moredetails.fulllogic.domain.entities.ArtistBiography

interface Repository {
    fun getArtistInfo(artistName: String): ArtistBiography
}

class RepositoryImpl : Repository {

    private lateinit var articleDatabase: ArticleDatabase
    private lateinit var lastFMAPI: LastFMAPI

    override fun getArtistInfo(artistName: String): ArtistBiography {
        val dbArticle = getArticleFromDB(artistName)
        return if (dbArticle != null) {
            dbArticle.markItAsLocal()
        } else {
            val artistBiography = getArticleFromService(artistName)
            if (artistBiography.biography.isNotEmpty()) {
                insertArtistIntoDB(artistBiography)
            }
            artistBiography
        }
    }

    private fun getArticleFromDB(artistName: String): ArtistBiography? {
        val artistEntity = articleDatabase.ArticleDao().getArticleByArtistName(artistName)
        return artistEntity?.let {
            ArtistBiography(artistName, artistEntity.biography, artistEntity.articleUrl)
        }
    }

    private fun getArticleFromService(artistName: String): ArtistBiography {
        var artistBiography = ArtistBiography(artistName, "", "")
        try {
            val callResponse = lastFMAPI.getArtistInfo(artistName).execute()
            artistBiography = getArtistBioFromExternalData(callResponse.body(), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return artistBiography
    }

    private fun getArtistBioFromExternalData(
        serviceData: String?,
        artistName: String
    ): ArtistBiography {
        val gson = Gson()
        val jobj = gson.fromJson(serviceData, JsonObject::class.java)

        val artist = jobj["artist"].getAsJsonObject()
        val bio = artist["bio"].getAsJsonObject()
        val extract = bio["content"]
        val url = artist["url"]
        val text = extract?.asString ?: "No Results"

        return ArtistBiography(artistName, text, url.asString)
    }

    private fun insertArtistIntoDB(artistBiography: ArtistBiography) {
        articleDatabase.ArticleDao().insertArticle(
            ArticleEntity(
                artistBiography.artistName, artistBiography.biography, artistBiography.articleUrl
            )
        )
    }

    private fun ArtistBiography.markItAsLocal() = copy(biography = "[*]$biography")
}


package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.Card

interface OtherInfoLocalStorage {
    fun getArticle(artistName: String): Card?
    fun insertArtist(artistName: String, artistBiography: Card)
}

internal class OtherInfoLocalStorageImpl(
    private val articleDatabase: ArticleDatabase,
) : OtherInfoLocalStorage {

    override fun getArticle(artistName: String): Card? {
        val artistCard = articleDatabase.ArticleDao().getArticleByArtistName(artistName)
        return artistCard?.let {
            Card(artistCard.biography, artistCard.articleUrl, "LastFM","")
        }
    }

    override fun insertArtist(artistName: String, artistBiography: Card) {
        articleDatabase.ArticleDao().insertArticle(
            ArticleEntity(
                artistName, artistBiography.description, artistBiography.infoUrl
            )
        )
    }
}
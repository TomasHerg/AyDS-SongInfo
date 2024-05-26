package ayds.songinfo.moredetails.data

import external.src.main.java.ayds.artist.external.externalSourceInfo.OtherInfoService
import external.src.main.java.ayds.artist.external.externalSourceInfo.LastFMBiography
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import ayds.songinfo.moredetails.domain.Card
import ayds.artist.external.LastFMBiographyToCardResolver

internal class OtherInfoRepositoryImpl(
    private val otherInfoLocalStorage: OtherInfoLocalStorage,
    private val otherInfoService: OtherInfoService
) : OtherInfoRepository {

    override fun getArtistInfo(artistName: String): Card {
        val dbArticle = otherInfoLocalStorage.getArticle(artistName)

        val artistCard: Card
        val lastFMToCardResolver : LastFMBiographyToCardResolver = LastFMBiographyToCardResolverImpl()

        if (dbArticle != null) {
            artistCard = dbArticle.apply { markSource() }
        } else {
            val artistFMBio = otherInfoService.getArticle(artistName)
            artistCard = lastFMToCardResolver.map(artistFMBio)
            if (artistCard.description.isNotEmpty()) {
                otherInfoLocalStorage.insertArtist(artistName, artistCard)
            }
        }
        return artistCard
    }

    private fun Card.markSource() {
        source = "Last FM"
    }
}
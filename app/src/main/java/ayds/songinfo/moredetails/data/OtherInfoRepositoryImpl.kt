package ayds.songinfo.moredetails.data

import external.src.main.java.ayds.artist.external.externalSourceInfo.OtherInfoService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository

internal class OtherInfoRepositoryImpl(
    private val otherInfoLocalStorage: OtherInfoLocalStorage,
    private val otherInfoService: OtherInfoService,
) : OtherInfoRepository {

    override fun getArtistInfo(artistName: String): ArtistBiography {
        val dbArticle = otherInfoLocalStorage.getArticle(artistName)

        val artistBiography: ArtistBiography

        if (dbArticle != null) {
            artistBiography = dbArticle.apply { markItAsLocal() }
        } else {
            artistBiography = otherInfoService.getArticle(artistName)
            if (artistBiography.biography.isNotEmpty()) {
                otherInfoLocalStorage.insertArtist(artistBiography)
            }
        }
        return artistBiography
    }

    private fun ArtistBiography.markItAsLocal() {
        isLocallyStored = true
    }
}
package ayds.artist.external

import ayds.artist.external.LastFMBiography
import java.io.IOException

interface OtherInfoService {
    fun getArticle(artistName: String): LastFMBiography
}
internal class OtherInfoServiceImpl(
    private val lastFMAPI: LastFMAPI,
    private val lastFMToArtistBiographyResolver: LastFMToArtistBiographyResolver
) : OtherInfoService {

    override fun getArticle(artistName: String): LastFMBiography {

        var lastFMBiography = LastFMBiography(artistName, "", "")
        try {
            val callResponse = getSongFromService(artistName)
            lastFMBiography = lastFMToArtistBiographyResolver.map(callResponse.body(), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return lastFMBiography
    }

    private fun getSongFromService(artistName: String) =
        lastFMAPI.getArtistInfo(artistName).execute()

}
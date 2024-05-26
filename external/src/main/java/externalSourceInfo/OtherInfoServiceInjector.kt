package ayds.artist.external.externalSourceInfo.OtherInfoServiceInjector

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import ayds.artist.external.externalSourceInfo.LastFMAPI
import ayds.artist.external.externalSourceInfo.LastFMToArtistBiographyResolver
import ayds.artist.external.externalSourceInfo.OtherInfoService

private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"

object OtherInfoServiceInjector{

    lateinit var lastFMAPI : LastFMAPI
    lateinit var lastFMToArtistBiographyResolver : LastFMToArtistBiographyResolver
    lateinit var otherInfoService : OtherInfoService

    fun initOtherInfoService(){

        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        lastFMAPI = retrofit.create(LastFMAPI::class.java)

        lastFMToArtistBiographyResolver = LastFMToArtistBiographyResolverImpl()
        otherInfoService = OtherInfoServiceImpl(lastFMAPI, lastFMToArtistBiographyResolver)
    }
}
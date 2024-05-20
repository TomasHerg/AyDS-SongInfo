package ayds.songinfo.moredetails.domain

import ayds.songinfo.moredetails.data.OtherInfoRepositoryImpl
import ayds.songinfo.moredetails.data.external.OtherInfoService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import org.junit.Assert
import org.junit.Test

class OtherInfoRepositoryTest {

    private val localRepository : OtherInfoLocalStorage = mockk()
    private val otherInfoService : OtherInfoService = mockk()

    private lateinit var otherInfoRepository : OtherInfoRepository

    @Before
    fun setUp() {
        otherInfoRepository = OtherInfoRepositoryImpl(localRepository, otherInfoService)
    }

    @Test
    fun 'getArtisInfo should return a string if the song exists locally'(){

        val localArtistBiography = ArtistBiography(
            "artist name",
            "local artist biography",
            "http://url.com",
            true
        )

        every {otherInfoRepository.getArtistInfo(localArtistBiography.artistName) returns localArtistBiography}

        val expectedResultLocal = "[*]"+ localArtistBiography.biography
        val resultLocalBiography = otherInfoRepository.getArtistInfo(localArtistBiography.artistName).biography

        Assert.assertEquals(expectedResultLocal, resultLocalBiography)
    }

    @Test
    fun 'getArtisInfo should return a string if the song exists remotelly and not locally'(){

        val localArtistBiography = ArtistBiography(
            "artist name",
            "local artist biography",
            "http://url.com",
            false
        )

        every {otherInfoRepository.getArtistInfo(localArtistBiography.artistName)} returns localArtistBiography

        val resultRemoteBiography = otherInfoRepository.getArtistInfo(localArtistBiography.artistName)

        Assert.assertEquals(localArtistBiography.artistName, resultRemoteBiography)
    }

    @Test
    fun 'when the biography is not stored locally it should be inserted locally'(){

        val artistBiography = ArtistBiography(
            "artist name",
            "local artist biography",
            "http://url.com",
            false
        )

        every {otherInfoRepository.getArtistInfo(artistBiography.artistName)} returns artistBiography

        val firstSearch = otherInfoRepository.getArtistInfo(artistBiography.artistName)
        //ahora debería estar almacenada localmente
        val secondSearch = otherInfoRepository.getArtistInfo(artistBiography.artistName)

        Assert.assertEquals("[*]"+firstSearch.biography, secondSearch.biography)
    }

}
package ayds.songinfo.moredetails.presentation
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository

class OtherInfoPresenterTest {

    private val otherInfoRepository : OtherInfoRepository = mockk()

    private val artistBiographyDescriptionHelper: ArtistBiographyDescriptionHelper = mockk()

    private val otherInfoPresenter: OtherInfoPresenter = OtherInfoPresenterImpl(otherInfoRepository, artistBiographyDescriptionHelper)

    @Test
    fun 'getArtistInfo should return astist biography ui state'(){

        val artistBiography = ArtistBiography(
            "artistName",
            "biography",
            "articleUrl"
        )

        every {otherInfoRepository.getArtistInfo("artistName")} returns artistBiography
        every { artistBiographyDescriptionHelper.getDescription(artistBiography) } returns "description"
        val artistBiographyTester: (ArtistBiographyUiState) -> Unit = mockk(relaxed = true)

        otherInfoPresenter.artistBiographyObservable.suscribe(artistBiographyTester)
        otherInfoPresenter.getArtistInfo("artistName")

        val result = ArtistBiographyUiState("artistName", "description", "articleUrl")

    }

}
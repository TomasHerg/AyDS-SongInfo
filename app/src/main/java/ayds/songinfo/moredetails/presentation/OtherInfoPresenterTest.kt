package ayds.songinfo.moredetails.presentation
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository

class OtherInfoPresenterTest {

    private val otherInfoRepository : OtherInfoRepository = mockk()

    private val artistBiographyDescriptionHelper: CardDescriptionHelper = mockk()

    private val otherInfoPresenter: OtherInfoPresenter = OtherInfoPresenterImpl(otherInfoRepository, artistBiographyDescriptionHelper)

    @Test
    fun 'getArtistInfo should return astist biography ui state'(){

        val card = Card(
            "artistName",
            "biography",
            "articleUrl"
        )

        every {otherInfoRepository.getArtistInfo("artistName")} returns card
        every { artistBiographyDescriptionHelper.getDescription(card) } returns "description"
        val artistBiographyTester: (CardUiState) -> Unit = mockk(relaxed = true)

        otherInfoPresenter.artistBiographyObservable.suscribe(artistBiographyTester)
        otherInfoPresenter.getArtistInfo("artistName")

        val result = CardUiState("artistName", "description", "articleUrl")

    }

}
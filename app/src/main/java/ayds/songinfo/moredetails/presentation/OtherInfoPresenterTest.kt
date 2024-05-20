package ayds.songinfo.moredetails.presentation
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.OtherInfoRepository

class OtherInfoPresenterTest {

    private val repository : OtherInfoRepository = mockk(relaxUnitFun = true)

    private val artistBiographyDescriptionHelper: ArtistBiographyDescriptionHelper = mockk(relaxUnitFun = true)

    private val otherInfoPresenter: OtherInfoPresenterImpl = mockk(){
        every { artistBiographyObservable } returns Subject<ArtistBiographyUiState>
    }

    @Test
    fun 'getArtistInfo should notify the view'(){

        otherInfoPresenter.getArtistInfo("artistName")

        val uiState : ArtistBiographyUiState = mockk(relaxUnitFun = true)

        verify { artistBiographyObservable.notify(uiState) }
    }

    @Test
    fun 'when getArtistInfo from OtherInfoPresenter is used, getArtistInfo from repository is used'(){

        otherInfoPresenter.getArtistInfo("artistName")

        verify{ repository.getArtistInfo("artistName") }
    }

}
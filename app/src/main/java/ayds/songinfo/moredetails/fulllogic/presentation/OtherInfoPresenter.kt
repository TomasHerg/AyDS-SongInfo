package ayds.songinfo.moredetails.fulllogic.presentation

import ayds.songinfo.moredetails.fulllogic.data.Repository

class OtherInfoPresenter(private val view: OtherInfoView, private val repository: Repository) {

    fun onViewCreated(artistName: String) {
        val artistBiography = repository.getArtistInfo(artistName)
        view.updateUi(artistBiography)
    }
}



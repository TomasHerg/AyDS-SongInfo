package ayds.artist.external

import app.src.main.java.ayds.songinfo.moredetails.domain.Card

interface LastFMBiographyToCardResolver {

    fun map(artisLastFMBiography : LastFMBiography) : Card
}

internal class LastFMBiographyToCardResolverImpl : LastFMBiographyToCardResolver{

    override fun map(artisLastFMBiography: LastFMBiography) : Card {
        return Card(artisLastFMBiography.biography, artisLastFMBiography.articleUrl,"","")
    }
}


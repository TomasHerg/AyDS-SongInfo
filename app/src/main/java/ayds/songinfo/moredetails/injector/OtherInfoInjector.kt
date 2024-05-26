package ayds.songinfo.moredetails.injector

import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.data.OtherInfoRepositoryImpl
import external.src.moan.java.ayds.artist.external.OtherInfoServiceInjector //quiero importar el injector pero no me deja
import ayds.songinfo.moredetails.data.local.ArticleDatabase
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorageImpl
import ayds.songinfo.moredetails.presentation.ArtistBiographyDescriptionHelperImpl
import ayds.songinfo.moredetails.presentation.OtherInfoPresenter
import ayds.songinfo.moredetails.presentation.OtherInfoPresenterImpl
//estos dos imports deberían borrarse cuando funcione
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


private const val ARTICLE_BD_NAME = "database-article"

object OtherInfoInjector {

    lateinit var presenter: OtherInfoPresenter

    fun initGraph(context: Context) {

        val articleDatabase =
            Room.databaseBuilder(context, ArticleDatabase::class.java, ARTICLE_BD_NAME).build()

        val articleLocalStorage = OtherInfoLocalStorageImpl(articleDatabase)

        val otherInfoService = OtherInfoServiceInjector.otherInfoService

        val repository = OtherInfoRepositoryImpl(articleLocalStorage, otherInfoService)

        val artistBiographyDescriptionHelper = ArtistBiographyDescriptionHelperImpl()

        presenter = OtherInfoPresenterImpl(repository, artistBiographyDescriptionHelper)
    }
}
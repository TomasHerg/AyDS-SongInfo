import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.presentation.ArtistBiographyDescriptionHelperImpl
import org.junit.Test

class ArtistBiographyDescriptionHelperImplTest {

    private val descriptionHelper = ArtistBiographyDescriptionHelperImpl()

    @Test
    fun `getTextBiography should add prefix if isLocallyStored is true`() {

        val artistBiography = ArtistBiography(
            "Nombre de artista",
            "Biografia del artista",
            "http://ejemplourl.com",
            true
        )

        val result = descriptionHelper.getDescription(artistBiography)

        Assert.assertTrue(result.contains("[*]"+artistBiography.biography))
    }

    @Test
    fun `getTextBiography should not add prefix if isLocallyStored is false`() {

        val artistBiography = ArtistBiography(
            "Nombre de artista",
            "Biografia del artista",
            "http://ejemplourl.com",
            false
        )

        // Act
        val result = descriptionHelper.getDescription(artistBiography)

        Assert.assertTrue(result.contains("Biografia del artista") && !result.contains("[*]"))
    }

}
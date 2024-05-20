import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.data.external.LastFMToArtistBiographyResolverImpl
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.junit.Assert.assertEquals
import org.junit.Test

class LastFMToArtistBiographyResolverImplTest {

    private val resolver = LastFMToArtistBiographyResolverImpl()

    @Test
    fun 'map should correctly parse valid serviceData'() {
        // Arrange
        val serviceData = """
            {
                "artist": {
                    "bio": {
                        "content": "Artist biography."
                    },
                    "url": "http://example.com/artist"
                }
            }
        """.trimIndent()
        val artistName = "Artist Name"

        // Act
        val artistBiography = resolver.map(serviceData, artistName)

        // Assert
        assertEquals(artistName, artistBiography.artistName)
        assertEquals("This is the artist biography.", artistBiography.biography)
        assertEquals("http://example.com/artist", artistBiography.articleUrl)
    }

    @Test
    fun 'map should return NO_RESULTS when content is null'() {
        // Arrange
        val serviceData = """
            {
                "artist": {
                    "bio": {},
                    "url": "http://example.com/artist"
                }
            }
        """
        val artistName = "Artist Name"

        // Act
        val artistBiography = resolver.map(serviceData, artistName)

        // Assert
        assertEquals(artistName, artistBiography.artistName)
        assertEquals("No Results", artistBiography.biography)
        assertEquals("http://example.com/artist", artistBiography.articleUrl)
    }

    @Test
    fun 'map should return the service data even if its NULL()' {
        // Arrange
        val serviceData: String? = null
        val artistName = "Artist Name"

        val artistBiography = resolver.map(serviceData, artistName)

        // Assert
        // If the implementation does not handle null serviceData, this test will fail
        // To pass this, the implementation should check for null and return a default value or throw an exception
        assertEquals(artistName, artistBiography.artistName)
        assertEquals("No Results", artistBiography.biography)
        assertEquals("", artistBiography.articleUrl)  // Assuming the implementation handles null by setting URL to an empty string
    }
}

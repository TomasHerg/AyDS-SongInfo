package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song.EmptySong
import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.entities.Song.SpotifySong

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl : SongDescriptionHelper {
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Release date: ${getFormattedReleaseDate(song.releaseDate, song.releaseDatePrecision)}" +
                        "Realese date precision: ${song.releaseDatePrecision}"
            else -> "Song not found"
        }
    }
}

private fun getFormattedReleaseDate(releaseDate: String, precision: String): String {
    return when (precision) {
        "day" -> {
            val fecha = releaseDate.split("-")
            val day = fecha[2]
            val month = fecha[1]
            val year = fecha[0]
            " $day/$month/$year"
        }
        "month" -> {
            val fecha = releaseDate.split("-")
            val month = fecha[1]
            val year = fecha[0]
            " $month/$year"
        }
        "year" -> {
            val fecha = releaseDate.split("-")
            val year = fecha[0]
            " $year"
        }
        else -> ""
    }
}
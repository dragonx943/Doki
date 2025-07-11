package org.dokiteam.doki.core.model.parcelable

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import org.dokiteam.doki.core.model.MangaSource
import org.dokiteam.doki.core.util.ext.readParcelableCompat
import org.dokiteam.doki.core.util.ext.readSerializableCompat
import org.dokiteam.doki.core.util.ext.readStringSet
import org.dokiteam.doki.core.util.ext.writeStringSet
import org.dokiteam.doki.parsers.model.Manga

@Parcelize
data class ParcelableManga(
	val manga: Manga,
	private val withDescription: Boolean = true,
) : Parcelable {

	companion object : Parceler<ParcelableManga> {

		override fun ParcelableManga.write(parcel: Parcel, flags: Int) = with(manga) {
			parcel.writeLong(id)
			parcel.writeString(title)
			parcel.writeStringSet(altTitles)
			parcel.writeString(url)
			parcel.writeString(publicUrl)
			parcel.writeFloat(rating)
			parcel.writeSerializable(contentRating)
			parcel.writeString(coverUrl)
			parcel.writeString(largeCoverUrl)
			parcel.writeString(description.takeIf { withDescription })
			parcel.writeParcelable(ParcelableMangaTags(tags), flags)
			parcel.writeSerializable(state)
			parcel.writeStringSet(authors)
			parcel.writeString(source.name)
		}

		override fun create(parcel: Parcel) = ParcelableManga(
			Manga(
				id = parcel.readLong(),
				title = requireNotNull(parcel.readString()),
				altTitles = parcel.readStringSet(),
				url = requireNotNull(parcel.readString()),
				publicUrl = requireNotNull(parcel.readString()),
				rating = parcel.readFloat(),
				contentRating = parcel.readSerializableCompat(),
				coverUrl = parcel.readString(),
				largeCoverUrl = parcel.readString(),
				description = parcel.readString(),
				tags = requireNotNull(parcel.readParcelableCompat<ParcelableMangaTags>()).tags,
				state = parcel.readSerializableCompat(),
				authors = parcel.readStringSet(),
				chapters = null,
				source = MangaSource(parcel.readString()),
			),
			withDescription = true,
		)
	}
}

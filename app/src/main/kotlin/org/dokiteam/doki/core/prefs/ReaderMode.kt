package org.dokiteam.doki.core.prefs

import androidx.annotation.Keep

@Keep
enum class ReaderMode(val id: Int) {

	STANDARD(1),
	REVERSED(3),
	VERTICAL(4),
	WEBTOON(2),
	;

	companion object {

		fun valueOf(id: Int) = entries.firstOrNull { it.id == id }
	}
}

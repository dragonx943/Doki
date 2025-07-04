package org.dokiteam.doki.core.exceptions

import okio.IOException
import org.dokiteam.doki.parsers.model.MangaSource

class InteractiveActionRequiredException(
	val source: MangaSource,
	val url: String,
) : IOException("Interactive action is required for ${source.name}")

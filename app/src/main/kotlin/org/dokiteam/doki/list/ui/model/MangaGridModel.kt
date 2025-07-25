package org.dokiteam.doki.list.ui.model

import org.dokiteam.doki.core.ui.model.MangaOverride
import org.dokiteam.doki.list.domain.ReadingProgress
import org.dokiteam.doki.list.ui.ListModelDiffCallback.Companion.PAYLOAD_ANYTHING_CHANGED
import org.dokiteam.doki.list.ui.ListModelDiffCallback.Companion.PAYLOAD_PROGRESS_CHANGED
import org.dokiteam.doki.parsers.model.Manga

data class MangaGridModel(
	override val manga: Manga,
	override val override: MangaOverride?,
	override val counter: Int,
	val progress: ReadingProgress?,
	val isFavorite: Boolean,
	val isSaved: Boolean,
) : MangaListModel() {

	override fun getChangePayload(previousState: ListModel): Any? = when {
		previousState !is MangaGridModel || previousState.manga != manga -> null

		previousState.progress != progress -> PAYLOAD_PROGRESS_CHANGED
		previousState.isFavorite != isFavorite ||
			previousState.isSaved != isSaved -> PAYLOAD_ANYTHING_CHANGED

		else -> super.getChangePayload(previousState)
	}
}

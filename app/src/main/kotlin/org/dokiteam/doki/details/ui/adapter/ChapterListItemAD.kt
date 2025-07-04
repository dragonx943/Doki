package org.dokiteam.doki.details.ui.adapter

import android.graphics.Typeface
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import org.dokiteam.doki.R
import org.dokiteam.doki.core.ui.list.AdapterDelegateClickListenerAdapter
import org.dokiteam.doki.core.ui.list.OnListItemClickListener
import org.dokiteam.doki.core.util.ext.drawableStart
import org.dokiteam.doki.core.util.ext.getThemeColorStateList
import org.dokiteam.doki.core.util.ext.textAndVisible
import org.dokiteam.doki.databinding.ItemChapterBinding
import org.dokiteam.doki.details.ui.model.ChapterListItem
import org.dokiteam.doki.list.ui.model.ListModel
import com.google.android.material.R as materialR

fun chapterListItemAD(
	clickListener: OnListItemClickListener<ChapterListItem>,
) = adapterDelegateViewBinding<ChapterListItem, ListModel, ItemChapterBinding>(
	viewBinding = { inflater, parent -> ItemChapterBinding.inflate(inflater, parent, false) },
	on = { item, _, _ -> item is ChapterListItem && !item.isGrid },
) {

	AdapterDelegateClickListenerAdapter(this, clickListener).attach(itemView)

	bind {
		binding.textViewTitle.text = item.getTitle(context.resources)
		binding.textViewDescription.textAndVisible = item.description
		when {
			item.isCurrent -> {
				binding.textViewTitle.drawableStart = ContextCompat.getDrawable(context, R.drawable.ic_current_chapter)
				binding.textViewTitle.setTextColor(context.getThemeColorStateList(android.R.attr.textColorPrimary))
				binding.textViewDescription.setTextColor(context.getThemeColorStateList(android.R.attr.textColorPrimary))
				binding.textViewTitle.typeface = Typeface.DEFAULT_BOLD
				binding.textViewDescription.typeface = Typeface.DEFAULT_BOLD
			}

			item.isUnread -> {
				binding.textViewTitle.drawableStart = if (item.isNew) {
					ContextCompat.getDrawable(context, R.drawable.ic_new)
				} else {
					null
				}
				binding.textViewTitle.setTextColor(context.getThemeColorStateList(android.R.attr.textColorPrimary))
				binding.textViewDescription.setTextColor(context.getThemeColorStateList(materialR.attr.colorOutline))
				binding.textViewTitle.typeface = Typeface.DEFAULT
				binding.textViewDescription.typeface = Typeface.DEFAULT
			}

			else -> {
				binding.textViewTitle.drawableStart = null
				binding.textViewTitle.setTextColor(context.getThemeColorStateList(android.R.attr.textColorHint))
				binding.textViewDescription.setTextColor(context.getThemeColorStateList(android.R.attr.textColorHint))
				binding.textViewTitle.typeface = Typeface.DEFAULT
				binding.textViewDescription.typeface = Typeface.DEFAULT
			}
		}
		binding.imageViewBookmarked.isVisible = item.isBookmarked
		binding.imageViewDownloaded.isVisible = item.isDownloaded
	}
}

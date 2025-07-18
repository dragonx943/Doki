package org.dokiteam.doki.search.ui.suggestion.adapter

import android.widget.TextView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import org.dokiteam.doki.R
import org.dokiteam.doki.core.util.ext.getDisplayMessage
import org.dokiteam.doki.search.ui.suggestion.model.SearchSuggestionItem

fun searchSuggestionTextAD() = adapterDelegate<SearchSuggestionItem.Text, SearchSuggestionItem>(
	R.layout.item_search_suggestion_text,
) {

	bind {
		val tv = itemView as TextView
		val isError = item.error != null
		tv.setCompoundDrawablesRelativeWithIntrinsicBounds(
			if (isError) R.drawable.ic_error_small else 0,
			0,
			0,
			0,
		)
		if (item.textResId != 0) {
			tv.setText(item.textResId)
		} else {
			tv.text = item.error?.getDisplayMessage(tv.resources)
		}
	}
}

package org.dokiteam.doki.search.ui.suggestion

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.MenuProvider
import org.dokiteam.doki.R
import org.dokiteam.doki.core.ui.dialog.buildAlertDialog
import org.dokiteam.doki.core.util.ext.resolve
import org.dokiteam.doki.core.util.ext.tryLaunch

class SearchSuggestionMenuProvider(
	private val context: Context,
	private val voiceInputLauncher: ActivityResultLauncher<String?>,
	private val viewModel: SearchSuggestionViewModel,
) : MenuProvider {

	override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
		menuInflater.inflate(R.menu.opt_search_suggestion, menu)
	}

	override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
		return when (menuItem.itemId) {
			R.id.action_clear -> {
				clearSearchHistory()
				true
			}

			R.id.action_voice_search -> {
				voiceInputLauncher.tryLaunch(context.getString(R.string.search_manga), null)
			}

			else -> false
		}
	}

	override fun onPrepareMenu(menu: Menu) {
		super.onPrepareMenu(menu)
		menu.findItem(R.id.action_voice_search)?.isVisible = voiceInputLauncher.resolve(context, null) != null
	}

	private fun clearSearchHistory() {
		buildAlertDialog(context, isCentered = true) {
			setTitle(R.string.clear_search_history)
			setIcon(R.drawable.ic_clear_all)
			setCancelable(true)
			setMessage(R.string.text_clear_search_history_prompt)
			setNegativeButton(android.R.string.cancel, null)
			setPositiveButton(R.string.clear) { _, _ -> viewModel.clearSearchHistory() }
		}.show()
	}
}

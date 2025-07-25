package org.dokiteam.doki.settings

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import dagger.hilt.android.AndroidEntryPoint
import org.dokiteam.doki.BuildConfig
import org.dokiteam.doki.R
import org.dokiteam.doki.core.prefs.AppSettings
import org.dokiteam.doki.core.ui.BasePreferenceFragment
import org.dokiteam.doki.core.util.ext.addMenuProvider
import org.dokiteam.doki.core.util.ext.getQuantityStringSafe
import org.dokiteam.doki.core.util.ext.observe
import org.dokiteam.doki.settings.search.SettingsSearchMenuProvider
import org.dokiteam.doki.settings.search.SettingsSearchViewModel

@AndroidEntryPoint
class RootSettingsFragment : BasePreferenceFragment(0) {

	private val viewModel: RootSettingsViewModel by viewModels()
	private val activityViewModel: SettingsSearchViewModel by activityViewModels()

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		addPreferencesFromResource(R.xml.pref_root)
		addPreferencesFromResource(R.xml.pref_root_debug)
		bindPreferenceSummary("appearance", R.string.theme, R.string.list_mode, R.string.language)
		bindPreferenceSummary("reader", R.string.read_mode, R.string.scale_mode, R.string.switch_pages)
		bindPreferenceSummary("network", R.string.proxy, R.string.dns_over_https, R.string.prefetch_content)
		bindPreferenceSummary("userdata", R.string.protect_application, R.string.backup_restore, R.string.data_deletion)
		bindPreferenceSummary("downloads", R.string.manga_save_location, R.string.downloads_wifi_only)
		bindPreferenceSummary("tracker", R.string.track_sources, R.string.notifications_settings)
		bindPreferenceSummary("services", R.string.suggestions, R.string.sync, R.string.tracking)
		findPreference<Preference>("about")?.summary = getString(R.string.app_version, BuildConfig.VERSION_NAME)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		findPreference<Preference>(AppSettings.KEY_REMOTE_SOURCES)?.let { pref ->
			val total = viewModel.totalSourcesCount
			viewModel.enabledSourcesCount.observe(viewLifecycleOwner) {
				pref.summary = if (it >= 0) {
					getString(R.string.enabled_d_of_d, it, total)
				} else {
					resources.getQuantityStringSafe(R.plurals.items, total, total)
				}
			}
		}
		addMenuProvider(SettingsSearchMenuProvider(activityViewModel))
	}

	override fun setTitle(title: CharSequence?) {
		if (!resources.getBoolean(R.bool.is_tablet)) {
			super.setTitle(title)
		}
	}

	private fun bindPreferenceSummary(key: String, @StringRes vararg items: Int) {
		findPreference<Preference>(key)?.summary = items.joinToString { getString(it) }
	}
}

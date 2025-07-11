package org.dokiteam.doki.reader.ui

import android.content.res.Configuration
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import org.dokiteam.doki.core.prefs.AppSettings
import org.dokiteam.doki.core.prefs.ReaderMode
import org.dokiteam.doki.core.util.ext.findKeyByValue
import org.dokiteam.doki.reader.ui.pager.BaseReaderFragment
import org.dokiteam.doki.reader.ui.pager.doublepage.DoubleReaderFragment
import org.dokiteam.doki.reader.ui.pager.doublereversed.ReversedDoubleReaderFragment
import org.dokiteam.doki.reader.ui.pager.reversed.ReversedReaderFragment
import org.dokiteam.doki.reader.ui.pager.standard.PagerReaderFragment
import org.dokiteam.doki.reader.ui.pager.vertical.VerticalReaderFragment
import org.dokiteam.doki.reader.ui.pager.webtoon.WebtoonReaderFragment
import java.util.EnumMap

class ReaderManager(
	private val fragmentManager: FragmentManager,
	private val container: FragmentContainerView,
	settings: AppSettings,
) {

	private val modeMap = EnumMap<ReaderMode, Class<out BaseReaderFragment<*>>>(ReaderMode::class.java)

	init {
		val useDoublePages = isLandscape() && settings.isReaderDoubleOnLandscape
		invalidateTypesMap(useDoublePages)
	}

	val currentReader: BaseReaderFragment<*>?
		get() = fragmentManager.findFragmentById(container.id) as? BaseReaderFragment<*>

	val currentMode: ReaderMode?
		get() {
			val readerClass = currentReader?.javaClass ?: return null
			return modeMap.findKeyByValue(readerClass)
		}

	fun replace(newMode: ReaderMode) {
		val readerClass = requireNotNull(modeMap[newMode])
		fragmentManager.commit {
			setReorderingAllowed(true)
			replace(container.id, readerClass, null, null)
		}
	}

	fun setDoubleReaderMode(isEnabled: Boolean) {
		val mode = currentMode
		val prevReader = currentReader?.javaClass
		invalidateTypesMap(isEnabled && isLandscape())
		val newReader = modeMap[mode]
		if (mode != null && newReader != prevReader) {
			replace(mode)
		}
	}

	private fun invalidateTypesMap(useDoublePages: Boolean) {
		modeMap[ReaderMode.STANDARD] = if (useDoublePages) {
			DoubleReaderFragment::class.java
		} else {
			PagerReaderFragment::class.java
		}
		modeMap[ReaderMode.REVERSED] = if (useDoublePages) {
			ReversedDoubleReaderFragment::class.java
		} else {
			ReversedReaderFragment::class.java
		}
		modeMap[ReaderMode.WEBTOON] = WebtoonReaderFragment::class.java
		modeMap[ReaderMode.VERTICAL] = VerticalReaderFragment::class.java
	}

	private fun isLandscape() = container.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

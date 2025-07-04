package org.dokiteam.doki.reader.ui.pager.vertical

import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import org.dokiteam.doki.reader.ui.pager.BasePagerReaderFragment

@AndroidEntryPoint
class VerticalReaderFragment : BasePagerReaderFragment() {

	override fun onInitPager(pager: ViewPager2) {
		super.onInitPager(pager)
		pager.orientation = ViewPager2.ORIENTATION_VERTICAL
	}

	override fun onCreateAdvancedTransformer(): ViewPager2.PageTransformer = VerticalPageAnimTransformer()
}

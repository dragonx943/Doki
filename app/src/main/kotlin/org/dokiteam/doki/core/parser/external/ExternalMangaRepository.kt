package org.dokiteam.doki.core.parser.external

import android.content.ContentResolver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runInterruptible
import org.dokiteam.doki.core.cache.MemoryContentCache
import org.dokiteam.doki.core.parser.CachingMangaRepository
import org.dokiteam.doki.core.util.ext.printStackTraceDebug
import org.dokiteam.doki.parsers.model.Manga
import org.dokiteam.doki.parsers.model.MangaChapter
import org.dokiteam.doki.parsers.model.MangaListFilter
import org.dokiteam.doki.parsers.model.MangaListFilterCapabilities
import org.dokiteam.doki.parsers.model.MangaListFilterOptions
import org.dokiteam.doki.parsers.model.MangaPage
import org.dokiteam.doki.parsers.model.SortOrder
import org.dokiteam.doki.parsers.util.suspendlazy.suspendLazy
import java.util.EnumSet

class ExternalMangaRepository(
	contentResolver: ContentResolver,
	override val source: ExternalMangaSource,
	cache: MemoryContentCache,
) : CachingMangaRepository(cache) {

	private val contentSource = ExternalPluginContentSource(contentResolver, source)

	private val capabilities by lazy {
		runCatching {
			contentSource.getCapabilities()
		}.onFailure {
			it.printStackTraceDebug()
		}.getOrNull()
	}

	private val filterOptions = suspendLazy(initializer = contentSource::getListFilterOptions)

	override val sortOrders: Set<SortOrder>
		get() = capabilities?.availableSortOrders ?: EnumSet.of(SortOrder.POPULARITY)

	override val filterCapabilities: MangaListFilterCapabilities
		get() = capabilities?.listFilterCapabilities ?: MangaListFilterCapabilities()

	override var defaultSortOrder: SortOrder
		get() = capabilities?.availableSortOrders?.firstOrNull() ?: SortOrder.ALPHABETICAL
		set(value) = Unit

	override suspend fun getFilterOptions(): MangaListFilterOptions = filterOptions.get()

	override suspend fun getList(offset: Int, order: SortOrder?, filter: MangaListFilter?): List<Manga> =
		runInterruptible(Dispatchers.IO) {
			contentSource.getList(offset, order ?: defaultSortOrder, filter ?: MangaListFilter.EMPTY)
		}

	override suspend fun getDetailsImpl(manga: Manga): Manga = runInterruptible(Dispatchers.IO) {
		contentSource.getDetails(manga)
	}

	override suspend fun getPagesImpl(chapter: MangaChapter): List<MangaPage> = runInterruptible(Dispatchers.IO) {
		contentSource.getPages(chapter)
	}

	override suspend fun getPageUrl(page: MangaPage): String = runInterruptible(Dispatchers.IO) {
		contentSource.getPageUrl(page.url)
	}

	override suspend fun getRelatedMangaImpl(seed: Manga): List<Manga> = emptyList() // TODO
}

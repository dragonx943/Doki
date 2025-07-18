package org.dokiteam.doki.core.prefs

import android.net.ConnectivityManager
import androidx.annotation.Keep

@Keep
enum class NetworkPolicy(
	private val key: Int,
) {

	NEVER(0),
	ALWAYS(1),
	NON_METERED(2);

	fun isNetworkAllowed(cm: ConnectivityManager) = when (this) {
		NEVER -> false
		ALWAYS -> true
		NON_METERED -> !cm.isActiveNetworkMetered
	}

	companion object {

		fun from(key: String?, default: NetworkPolicy): NetworkPolicy {
			val intKey = key?.toIntOrNull() ?: return default
			return NetworkPolicy.entries.find { it.key == intKey } ?: default
		}
	}
}

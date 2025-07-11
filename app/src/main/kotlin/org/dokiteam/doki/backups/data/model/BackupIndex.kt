package org.dokiteam.doki.backups.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.dokiteam.doki.BuildConfig

@Serializable
class BackupIndex(
	@SerialName("app_id") val appId: String,
	@SerialName("app_version") val appVersion: Int,
	@SerialName("created_at") val createdAt: Long,
) {

	constructor() : this(
		appId = BuildConfig.APPLICATION_ID,
		appVersion = BuildConfig.VERSION_CODE,
		createdAt = System.currentTimeMillis(),
	)
}

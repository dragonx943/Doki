package org.dokiteam.doki.sync.domain

data class SyncAuthResult(
	val syncURL: String,
	val email: String,
	val password: String,
	val token: String,
)

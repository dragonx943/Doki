package org.dokiteam.doki.sync.data

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import org.dokiteam.doki.BuildConfig
import org.dokiteam.doki.R
import org.dokiteam.doki.core.db.DATABASE_VERSION
import org.dokiteam.doki.core.network.CommonHeaders

class SyncInterceptor(
	context: Context,
	private val account: Account,
) : Interceptor {

	private val accountManager = AccountManager.get(context)
	private val tokenType = context.getString(R.string.account_type_sync)

	override fun intercept(chain: Interceptor.Chain): Response {
		val token = accountManager.peekAuthToken(account, tokenType)
		val requestBuilder = chain.request().newBuilder()
		if (token != null) {
			requestBuilder.header(CommonHeaders.AUTHORIZATION, "Bearer $token")
		}
		requestBuilder.header("X-App-Version", BuildConfig.VERSION_CODE.toString())
		requestBuilder.header("X-Db-Version", DATABASE_VERSION.toString())
		return chain.proceed(requestBuilder.build())
	}
}

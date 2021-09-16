package com.imkit.demo

import android.app.Application
import com.imkit.sdk.IMKit
import java.nio.charset.Charset

class DemoApplication : Application() {

    private val SAMPLE_ENCRYPTION_KEY = "sample-encryption-key"

    override fun onCreate() {
        super.onCreate()
        val name = packageName
        IMKit.instance()
            .setUrl(getString(R.string.IMKIT_URL))
            .setClientKey(getString(R.string.IMKIT_CLIENT_KEY))
            .setBucketName(getString(R.string.IMKIT_BUCKET_NAME))
            .setProviderAuthority("${name}.fileProvider")
            .setSSLTrustAll(true)
            .init(
                this,
                SAMPLE_ENCRYPTION_KEY.toByteArray(Charset.forName("UTF-8"))
            )

        IMKit.setDebugLog(true)
    }
}
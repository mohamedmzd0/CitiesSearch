package com.mohamed.data.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject

class AssetManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun openAssetStream(fileName: String): InputStream {
        return context.assets.open(fileName)
    }
}
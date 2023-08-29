package com.achmad.base.service

import com.achmad.base.cache.AppDao
import io.mockk.clearAllMocks
import io.mockk.mockk
import org.junit.Before

open class AppRepositoryTest {

    protected val cache: AppDao = mockk()
    protected val remote: AppRemote = mockk()

    protected val repository = AppRepository(
        cache,
        remote
    )

    @Before
    fun setup() {
        clearAllMocks()
    }
}

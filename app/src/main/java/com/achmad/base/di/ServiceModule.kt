package com.achmad.base.di

import com.achmad.base.cache.AppDao
import com.achmad.base.service.AppRemote
import com.achmad.base.service.AppRepository
import com.achmad.base.service.AppService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideAppService(retrofit: Retrofit): AppService {
        return retrofit.create(AppService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppRemote(service: AppService): AppRemote {
        return AppRemote(service)
    }

    @Provides
    fun provideAppRepository(
        local: AppDao,
        remote: AppRemote,
    ): AppRepository {
        return AppRepository(local, remote)
    }
}

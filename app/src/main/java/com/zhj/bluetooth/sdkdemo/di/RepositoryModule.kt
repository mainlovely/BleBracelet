package com.zhj.bluetooth.sdkdemo.di

import com.zhj.bluetooth.sdkdemo.data.source.repository.*
import org.koin.dsl.module

val repositoryModule = module {
    single<UploadBleRepository> { UploadBleRepositoryImpl(get()) }
    single<UploadBleRxRepository> { UploadBleRxRepositoryImpl() }
}
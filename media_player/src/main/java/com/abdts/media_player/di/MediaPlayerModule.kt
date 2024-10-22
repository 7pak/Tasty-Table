package com.abdts.media_player.di

import com.abdts.media_player.navigation.MediaPlayerFeatureApi
import com.abdts.media_player.navigation.MediaPlayerFeatureApiImpl
import org.koin.dsl.module

val mediaPlayerModule = module {

    single <MediaPlayerFeatureApi>{
        MediaPlayerFeatureApiImpl()
    }
}
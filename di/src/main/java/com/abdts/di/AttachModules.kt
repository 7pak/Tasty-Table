package com.abdts.di

import com.abdts.media_player.di.mediaPlayerModule
import com.abdts.search.data.di.dataModule
import com.abdts.search.domain.di.domainModule
import com.abdts.search.ui.di.uiModule
import org.koin.dsl.module

val searchFeatureModule = module {
    includes(dataModule, domainModule, uiModule, mediaPlayerModule)
}
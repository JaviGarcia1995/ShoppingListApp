package com.fcojaviergarciarodriguez.shoppinglistapp.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Este módulo está reservado para dependencias específicas de la app
    // Las dependencias de framework están en FrameworkModule
    // Las dependencias de data están en DataModule
    
    // Por ahora no hay dependencias específicas de app
} 
package com.example.dantalk.mvi.main.home

sealed class HomeIntent {
    data object Initialize : com.example.dantalk.mvi.main.home.HomeIntent()
    data object NavigateToSearch : com.example.dantalk.mvi.main.home.HomeIntent()
    data object NavigateToProfile : com.example.dantalk.mvi.main.home.HomeIntent()
}
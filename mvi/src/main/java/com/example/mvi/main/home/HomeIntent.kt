package com.example.mvi.main.home

sealed class HomeIntent {
    data object InitializeChats : HomeIntent()
    data object NavigateToSearch : HomeIntent()
    data object NavigateToProfile : HomeIntent()
}
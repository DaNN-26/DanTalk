package com.example.feature.main.settings.component

import com.arkivanov.decompose.ComponentContext

class DefaultSettingsComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext, SettingsComponent {

}
package com.example.feature.mapper

import com.example.core.ui.model.UiUserData
import com.example.data.user.api.model.UserData

internal fun UserData.toUi() =
    UiUserData(
        id = id,
        email = email,
        username = username,
        firstname = firstname,
        lastname = lastname,
        patronymic = patronymic
    )
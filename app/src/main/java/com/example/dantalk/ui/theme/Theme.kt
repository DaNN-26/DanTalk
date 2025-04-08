package com.example.dantalk.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val lightColorPalette = ColorPalette(
    main = MainColor,
    hint = HintColor,
    subText = SubTextColor,
    singleTheme = Color.White,
    altSingleTheme = LightAltSingleTheme,
    oppositeTheme = Color.Black,
    red = RedColor,
    topBar = Color.White,
)

val darkColorPalette = ColorPalette(
    main = AltMainColor,
    hint = AltHintColor,
    subText = SubTextColor,
    singleTheme = Color.Black,
    altSingleTheme = DarkAltSingleTheme,
    oppositeTheme = Color.White,
    red = RedColor,
    topBar = DarkAltSingleTheme
)

@Composable
fun DanTalkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if(darkTheme) darkColorPalette
    else lightColorPalette

    CompositionLocalProvider(
        LocalColors provides colors,
        content = content
    )
}

object DanTalkTheme {
    val colors: ColorPalette
        @Composable @ReadOnlyComposable
        get() = LocalColors.current
}

internal val LocalColors = staticCompositionLocalOf<ColorPalette> {
    error("Colors composition error")
}
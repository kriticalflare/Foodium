package dev.shreyaspatil.foodium.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import dev.shreyaspatil.foodium.R

// Set of Material typography styles to start with
private val font = fontFamily(font(R.font.google_sans))
val typography = typographyFromDefaults(
        h1 = TextStyle(
                fontFamily = font,
        ),
        h2 = TextStyle(
                fontFamily = font,
        ),
        h3 = TextStyle(
                fontFamily = font,
        ),
        h4 = TextStyle(
                fontFamily = font,
        ),
        h5 = TextStyle(
                fontFamily = font,
        ),
        h6 = TextStyle(
                fontFamily = font,
        ),
        subtitle1 = TextStyle(
                fontFamily = font,
        ),
        subtitle2 = TextStyle(
                fontFamily = font,
        ),
        body1 = TextStyle(
                fontFamily = font,
        ),
        body2 = TextStyle(
                fontFamily = font,
        ),
        button = TextStyle(
                fontFamily = font,
        ),
        caption = TextStyle(
                fontFamily = font
        ),
        overline = TextStyle(
                fontFamily = font
        )
)

fun typographyFromDefaults(
        h1: TextStyle?,
        h2: TextStyle?,
        h3: TextStyle?,
        h4: TextStyle?,
        h5: TextStyle?,
        h6: TextStyle?,
        subtitle1: TextStyle?,
        subtitle2: TextStyle?,
        body1: TextStyle?,
        body2: TextStyle?,
        button: TextStyle?,
        caption: TextStyle?,
        overline: TextStyle?
): Typography {
        val defaults = Typography()
        return Typography(
                h1 = defaults.h1.merge(h1),
                h2 = defaults.h2.merge(h2),
                h3 = defaults.h3.merge(h3),
                h4 = defaults.h4.merge(h4),
                h5 = defaults.h5.merge(h5),
                h6 = defaults.h6.merge(h6),
                subtitle1 = defaults.subtitle1.merge(subtitle1),
                subtitle2 = defaults.subtitle2.merge(subtitle2),
                body1 = defaults.body1.merge(body1),
                body2 = defaults.body2.merge(body2),
                button = defaults.button.merge(button),
                caption = defaults.caption.merge(caption),
                overline = defaults.overline.merge(overline)
        )
}

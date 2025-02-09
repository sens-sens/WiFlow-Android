package com.androsmith.wiflow.ui.screens.home.composables

import android.graphics.BlurMaskFilter
import android.graphics.BlurMaskFilter.Blur.NORMAL
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.androsmith.wiflow.R
import com.androsmith.wiflow.ui.theme.Gray20
import com.androsmith.wiflow.ui.theme.Gray10
import com.androsmith.wiflow.ui.theme.Gray80
import com.androsmith.wiflow.ui.theme.Gray90

@Composable
fun NeumorphicButton(
    onClick : () -> Unit,
    modifier: Modifier = Modifier,
    pressed: Boolean = false,

    ) {

    var primaryColor = Gray10
    var primaryColorDim = Gray20

    var primaryContrast = Color.White
    var primaryContrastInverse = Color.Black.copy(alpha = .2f)

    var gradientTop = Color.White
    var gradientBottom = Color.Black .copy(alpha = .15f)

    val blue10 = Color(0xFF8B96A1)
    val blue = Color(0xFF539CE5)
    var blueGlow = Color(0xFF79B2EC)

    if (isSystemInDarkTheme()){
        primaryColor = Gray80
        primaryColorDim = Gray90

        blueGlow = Color(0xFF1D61C2)

        gradientTop = Color(0xFF2C2C2C)
        gradientBottom = Color(0xff191919)

        primaryContrast = Color(0xFF1A1A1A)
        primaryContrastInverse = Color(0xff101010)

    }

    val shadowColorBottom by animateColorAsState( targetValue =
        if (pressed)
            blueGlow.copy(alpha = .4f)
        else
            primaryContrastInverse
    )
    val shadowColorTop by animateColorAsState(
        targetValue = if (pressed) primaryContrast else primaryContrast.copy(
            alpha = .4f
        )
    )
    val interactionSource = remember { MutableInteractionSource() }


    val offset by animateIntAsState(targetValue = if (pressed) 1 else 4)
    val blur by animateDpAsState(targetValue = if (pressed) 4.dp else 10.dp)
    val padding by animateDpAsState(targetValue = if (pressed) 12.dp else 8.dp)
    val color by animateColorAsState(
        targetValue = if (pressed) blue else blue10
    )




    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(padding)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = interactionSource
            ).semantics {
                contentDescription = "Start / Stop server Button"
            }
        ,

        ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(200.dp)
                .shadow(
                    color = shadowColorTop,
                    offsetX = (0).dp,
                    offsetY = (-offset).dp,
                    blurRadius = blur,
                )
                .shadow(
                    color = shadowColorBottom,
                    offsetX = (0).dp,
                    offsetY = (offset).dp,
                    blurRadius = blur,
                )
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            primaryColor,
                            primaryColorDim,
                        )
                    ), shape = CircleShape
                )
                .border(
                    width = 1.dp, shape = CircleShape, brush = Brush.verticalGradient(
                        colors = listOf(
                            gradientTop,
                            gradientBottom,

                        )
                    )
                )

        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(56.dp),

                ) {
                Icon(
                    painter = painterResource(R.drawable.flash),
                    tint = color,

                    contentDescription = "Server state",

                    modifier = Modifier.size(52.dp - padding),
                )
            }
        }
    }
}

fun Modifier.shadow(
    color: Color = Color.Black,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
) = then(drawBehind {
    drawIntoCanvas { canvas ->
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        if (blurRadius != 0.dp) {
            frameworkPaint.maskFilter = (BlurMaskFilter(blurRadius.toPx(), NORMAL))
        }
        frameworkPaint.color = color.toArgb()

        val leftPixel = offsetX.toPx()
        val topPixel = offsetY.toPx()
        val rightPixel = size.width + topPixel
        val bottomPixel = size.height + leftPixel

        canvas.drawCircle(
            center = Offset(leftPixel + size.width / 2, topPixel + size.width / 2),
            radius = size.width / 2,
            paint = paint
        )
//                .drawRect(
//                left = leftPixel,
//                top = topPixel,
//                right = rightPixel,
//                bottom = bottomPixel,
//                paint = paint,
//            )
    }
}
)

fun Modifier.boxShadow(
    color: Color = Color.Black,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
) = then(drawBehind {
    drawIntoCanvas { canvas ->
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        if (blurRadius != 0.dp) {
            frameworkPaint.maskFilter = (BlurMaskFilter(blurRadius.toPx(), NORMAL))
        }
        frameworkPaint.color = color.toArgb()

        val leftPixel = offsetX.toPx()
        val topPixel = offsetY.toPx()
        val rightPixel = size.width + topPixel
        val bottomPixel = size.height + leftPixel

        canvas
            .drawRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                paint = paint,
            )
    }
}
)
package com.example.klinik_app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.klinik_app.R

object GlassNavColors {
    val Cyan = Color(0xFF06B6D4)
    val Blue = Color(0xFF2563EB)
    val PrimaryGlowIntense = Color(0x5006B6D4)

    val IconActive = Color(0xFFFFFFFF)
    val IconInactive = Color(0xFF000000)
    val FabGlow = Color(0x4014B8A6) // Teal glow around FAB
}

data class GlassNavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badge: Int? = null
)

private object NavBarDimensions {
    val NavBarHeight = 80.dp
    val NavBarCornerRadius = 32.dp
    val NavBarHorizontalPadding = 16.dp
    val NavBarVerticalPadding = 12.dp
    
    val ItemWidth = 56.dp
    val ItemHeight = 52.dp
    val IconSize = 28.dp
    val ActiveIconSize = 32.dp
    
    val FabSize = 52.dp
    val FabIconSize = 26.dp
    val FabSpacerWidth = 64.dp
    val FabOffset = 0.dp
    
    val IndicatorWidth = 16.dp
    val IndicatorHeight = 2.5.dp
    
    val BadgeSize = 16.dp
    val BadgeFontSize = 9.sp
}

@Composable
fun GlassBottomNavBar(
    items: List<GlassNavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onFabClick: (() -> Unit)? = null,
    fabIcon: ImageVector = Icons.Default.Add,
    showFab: Boolean = true
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = NavBarDimensions.NavBarHorizontalPadding, vertical = NavBarDimensions.NavBarVerticalPadding),
        contentAlignment = Alignment.BottomCenter
    ) {
        FrostedGlassContainer(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = NavBarDimensions.NavBarCornerRadius
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(NavBarDimensions.NavBarHeight)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, item ->
                    if (showFab && index == items.size / 2) {
                        Spacer(modifier = Modifier.width(NavBarDimensions.FabSpacerWidth))
                    }
                    
                    GlassNavItemComponent(
                        item = item,
                        isSelected = selectedIndex == index,
                        onClick = { onItemSelected(index) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        if (showFab && onFabClick != null) {
            FrostedGlassFab(
                onClick = onFabClick,
                icon = fabIcon,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-20).dp),
                size = NavBarDimensions.FabSize
            )
        }
    }
}

@Composable
fun FrostedGlassContainer(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = NavBarDimensions.NavBarCornerRadius,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    
    Box(
        modifier = modifier
            .shadow(
                elevation = 20.dp,
                shape = shape,
                ambientColor = Color(0x15000000),
                spotColor = Color(0x1506B6D4)
            )
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xE8FFFFFF),
                        Color(0xE0FFFFFF),
                        Color(0xD9F8FFFE)
                    )
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0x99FFFFFF),
                        Color(0x40E0F2FE),
                        Color(0x20FFFFFF)
                    )
                ),
                shape = shape
            )
    ) {
        content()
    }
}

@Composable
fun GlassNavItemComponent(
    item: GlassNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) GlassNavColors.IconActive else GlassNavColors.IconInactive,
        animationSpec = tween(durationMillis = 250),
        label = "iconColor"
    )
    
    val glowAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "glowAlpha"
    )

    val indicatorWidth by animateDpAsState(
        targetValue = if (isSelected) NavBarDimensions.IndicatorWidth else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "indicatorWidth"
    )
    
    val iconSize by animateDpAsState(
        targetValue = if (isSelected) NavBarDimensions.ActiveIconSize else NavBarDimensions.IconSize,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "iconSize"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .scale(scale)
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .graphicsLayer { alpha = glowAlpha * 0.5f }
                        .blur(20.dp)
                        .background(
                            color = GlassNavColors.Cyan,
                            shape = CircleShape
                        )
                )
                // Inner focused glow
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .graphicsLayer { alpha = glowAlpha * 0.7f }
                        .blur(14.dp)
                        .background(
                            color = GlassNavColors.Blue,
                            shape = CircleShape
                        )
                )
            }
            
            Icon(
                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                contentDescription = item.label,
                tint = iconColor,
                modifier = Modifier.size(iconSize)
            )

            item.badge?.let { count ->
                if (count > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 10.dp, y = (-6).dp)
                            .size(NavBarDimensions.BadgeSize)
                            .shadow(6.dp, CircleShape, ambientColor = Color(0x30EF4444))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFEF4444),
                                        Color(0xFFDC2626)
                                    )
                                ),
                                CircleShape
                            )
                            .border(
                                1.5.dp, 
                                Color(0x50FFFFFF), 
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (count > 9) "9+" else count.toString(),
                            fontSize = NavBarDimensions.BadgeFontSize,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .width(indicatorWidth)
                .height(NavBarDimensions.IndicatorHeight)
                .clip(RoundedCornerShape(NavBarDimensions.IndicatorHeight / 2))
                .background(
                    if (isSelected) {
                        Brush.horizontalGradient(
                            colors = listOf(
                                GlassNavColors.Cyan,
                                GlassNavColors.Blue
                            )
                        )
                    } else {
                        Brush.horizontalGradient(
                            colors = listOf(Color.Transparent, Color.Transparent)
                        )
                    }
                )
        )
    }
}

@Composable
fun FrostedGlassFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = NavBarDimensions.FabSize
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier.size(size + 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size + 16.dp)
                .blur(16.dp)
                .background(
                    color = GlassNavColors.FabGlow,
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(size + 8.dp)
                .blur(10.dp)
                .background(
                    color = GlassNavColors.PrimaryGlowIntense,
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(size)
                .shadow(
                    elevation = 12.dp,
                    shape = CircleShape,
                    ambientColor = Color(0x20000000),
                    spotColor = Color(0x30000000)
                )
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFFFFFF),
                            Color(0xFFF8F9FA),
                            Color(0xFFF1F3F5)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(size.value * 2, size.value * 2)
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0x70FFFFFF),
                            Color(0x30FFFFFF),
                            Color(0x10FFFFFF)
                        )
                    ),
                    shape = CircleShape
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(size - 8.dp)
                    .graphicsLayer { alpha = 0.15f }
                    .blur(6.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White,
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
            )
            
            Image(
                painter = painterResource(id = R.drawable.klinik_logo),
                contentDescription = "Klinik Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(NavBarDimensions.FabIconSize + 8.dp)
            )
        }
    }
}

@Composable
fun FrostedGlassFab(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = NavBarDimensions.FabSize
) {
    FrostedGlassFab(onClick = onClick, modifier = modifier, size = size)
}

@Composable
fun GlassFab(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = NavBarDimensions.FabSize
) {
    FrostedGlassFab(onClick, icon, modifier, size)
}

@Composable
fun GlassContainer(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = NavBarDimensions.NavBarCornerRadius,
    content: @Composable () -> Unit
) {
    FrostedGlassContainer(modifier, cornerRadius, content)
}

@Composable
fun SimpleGlassBottomNavBar(
    items: List<GlassNavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    GlassBottomNavBar(
        items = items,
        selectedIndex = selectedIndex,
        onItemSelected = onItemSelected,
        modifier = modifier,
        showFab = false
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFE8F5F2)
@Composable
fun PreviewGlassBottomNavBar() {
    val items = listOf(
        GlassNavItem("Home", Icons.Rounded.Home, Icons.Outlined.Home),
        GlassNavItem("Appointments", Icons.Rounded.CalendarMonth, Icons.Outlined.CalendarMonth),
        GlassNavItem("Inbox", Icons.Rounded.Chat, Icons.Outlined.Chat, badge = 3),
        GlassNavItem("Profile", Icons.Rounded.Person, Icons.Outlined.Person)
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE8F5F2),
                        Color(0xFFF0FDF9)
                    )
                )
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        GlassBottomNavBar(
            items = items,
            selectedIndex = 0,
            onItemSelected = {},
            onFabClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFE8F5F2)
@Composable
fun PreviewSimpleGlassBottomNavBar() {
    val items = listOf(
        GlassNavItem("Home", Icons.Rounded.Home, Icons.Outlined.Home),
        GlassNavItem("Appointments", Icons.Rounded.CalendarMonth, Icons.Outlined.CalendarMonth),
        GlassNavItem("Inbox", Icons.Rounded.Chat, Icons.Outlined.Chat, badge = 5),
        GlassNavItem("Profile", Icons.Rounded.Person, Icons.Outlined.Person)
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color(0xFFF0FDF9)),
        contentAlignment = Alignment.BottomCenter
    ) {
        SimpleGlassBottomNavBar(
            items = items,
            selectedIndex = 2,
            onItemSelected = {}
        )
    }
}

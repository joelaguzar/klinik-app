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
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Widgets
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

/**
 * Modern glassmorphism color scheme for bottom navigation
 * Designed to match the Klinik app's cyan/blue/purple theme
 */
object GlassNavColors {
    // Glassmorphism backgrounds - visible frosted glass with your theme colors
    val GlassBackground = Color(0xCCFFFFFF) // 80% white - more visible glass
    val GlassBackgroundTint = Color(0xF5F8FFFE) // Very subtle cyan tint
    val GlassBorderLight = Color(0x80FFFFFF) // 50% white border shimmer
    val GlassBorderSubtle = Color(0x40E0F2FE) // Subtle cyan border tint
    
    // Primary accent colors - matching your Cyan/Blue theme
    val Cyan = Color(0xFF06B6D4) // Your app's cyan
    val Blue = Color(0xFF2563EB) // Your app's blue
    val Purple = Color(0xFF7C3AED) // Your app's purple
    
    // Gradients
    val PrimaryGradientStart = Color(0xFF06B6D4) // Cyan
    val PrimaryGradientEnd = Color(0xFF2563EB) // Blue
    val PrimaryGlow = Color(0x3306B6D4) // Soft cyan glow
    val PrimaryGlowIntense = Color(0x5006B6D4) // Stronger cyan glow
    
    // Text and icons
    val TextActive = Color(0xFF0891B2) // Cyan-600 when selected
    val TextInactive = Color(0xFF94A3B8) // Slate-400 for inactive
    val IconActive = Color(0xFFFFFFFF) // White when selected
    val IconInactive = Color(0xFF000000) // Black for inactive icons
    
    // FAB colors - teal/greenish theme
    val FabGradientStart = Color(0xFF14B8A6) // Teal-500
    val FabGradientMiddle = Color(0xFF10B981) // Emerald-500
    val FabGradientEnd = Color(0xFF059669) // Emerald-600
    val FabGlow = Color(0x4014B8A6) // Teal glow around FAB
}

/**
 * Data class representing a navigation item
 */
data class GlassNavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badge: Int? = null // Optional badge count
)

// Consistent sizing constants for the navbar
private object NavBarDimensions {
    val NavBarHeight = 80.dp // Increased height
    val NavBarCornerRadius = 32.dp // Bigger corner radius at top
    val NavBarHorizontalPadding = 16.dp
    val NavBarVerticalPadding = 12.dp
    
    val ItemWidth = 56.dp
    val ItemHeight = 52.dp
    val IconSize = 28.dp
    val ActiveIconSize = 32.dp
    
    val FabSize = 52.dp
    val FabIconSize = 26.dp
    val FabSpacerWidth = 64.dp
    val FabOffset = 0.dp // Aligned with icons horizontally
    
    val IndicatorWidth = 16.dp
    val IndicatorHeight = 2.5.dp
    
    val BadgeSize = 16.dp
    val BadgeFontSize = 9.sp
}

/**
 * Modern glassmorphism bottom navigation bar with floating action button
 * Features a frosted glass effect with consistent sizing
 *
 * @param items List of navigation items to display
 * @param selectedIndex Currently selected item index
 * @param onItemSelected Callback when an item is selected
 * @param onFabClick Callback when the FAB is clicked
 * @param fabIcon Icon for the floating action button
 * @param modifier Modifier for the component
 * @param showFab Whether to show the center FAB
 */
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
        // Main frosted glass container
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
                // All items evenly distributed
                items.forEachIndexed { index, item ->
                    // Add spacer in center for FAB
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

        // Floating Action Button - centered and raised higher than other icons
        if (showFab && onFabClick != null) {
            FrostedGlassFab(
                onClick = onFabClick,
                icon = fabIcon,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-20).dp), // Raised higher than other icons
                size = NavBarDimensions.FabSize
            )
        }
    }
}

/**
 * Frosted glass container with realistic blur effect and light refraction
 * Creates a seamless modern glassmorphism aesthetic matching Klinik theme
 */
@Composable
fun FrostedGlassContainer(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = NavBarDimensions.NavBarCornerRadius,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    
    Box(
        modifier = modifier
            // Soft shadow for elevation - subtle cyan tint
            .shadow(
                elevation = 20.dp,
                shape = shape,
                ambientColor = Color(0x15000000),
                spotColor = Color(0x1506B6D4) // Cyan tinted shadow
            )
            .clip(shape)
            // Main glass background - more opaque and visible
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xE8FFFFFF), // 91% white at top
                        Color(0xE0FFFFFF), // 88% white
                        Color(0xD9F8FFFE)  // Slight cyan tint at bottom
                    )
                )
            )
            // Glass border with gradient - top highlight effect
            .border(
                width = 1.5.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0x99FFFFFF), // Bright top edge
                        Color(0x40E0F2FE), // Cyan tinted middle
                        Color(0x20FFFFFF)  // Subtle bottom
                    )
                ),
                shape = shape
            )
    ) {
        content()
    }
}

/**
 * Individual navigation item with smooth animations and consistent sizing
 */
@Composable
fun GlassNavItemComponent(
    item: GlassNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    
    // Smooth animations
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
            // Cyan glow effect for selected item
            if (isSelected) {
                // Outer soft glow
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
            
            // Badge with cyan/blue styling
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
                                        Color(0xFFEF4444), // Red
                                        Color(0xFFDC2626)  // Darker red
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

        // Selection indicator with cyan-blue gradient
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

/**
 * Frosted glass floating action button with Klinik logo
 */
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
        // Cyan glow effect
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
        
        // Main FAB with white/gray background for logo visibility
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
                            Color(0xFFFFFFFF), // White
                            Color(0xFFF8F9FA), // Very light gray
                            Color(0xFFF1F3F5)  // Light gray
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(size.value * 2, size.value * 2)
                    )
                )
                // Glass highlight border
                .border(
                    width = 1.5.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0x70FFFFFF), // Bright top highlight
                            Color(0x30FFFFFF), // Fade
                            Color(0x10FFFFFF)  // Subtle bottom
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
            // Inner highlight for glass depth
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

/**
 * Overloaded FrostedGlassFab that accepts an icon parameter for backward compatibility
 */
@Composable
fun FrostedGlassFab(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = NavBarDimensions.FabSize
) {
    FrostedGlassFab(onClick = onClick, modifier = modifier, size = size)
}

/**
 * Alias for backward compatibility
 */
@Composable
fun GlassFab(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = NavBarDimensions.FabSize
) {
    FrostedGlassFab(onClick, icon, modifier, size)
}

/**
 * Alias for backward compatibility
 */
@Composable
fun GlassContainer(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = NavBarDimensions.NavBarCornerRadius,
    content: @Composable () -> Unit
) {
    FrostedGlassContainer(modifier, cornerRadius, content)
}

/**
 * Simplified glass bottom nav without FAB
 */
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

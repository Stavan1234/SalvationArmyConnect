package com.example.salvationarmyconnect

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.salvationarmyconnect.ui.theme.SARed
import com.example.salvationarmyconnect.ui.theme.SABlue
import com.example.salvationarmyconnect.ui.theme.SAYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String = "Soldier", // We will pass the real name here later
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current

    // Bottom Nav State
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        // 1. TOP BAR (Logo + Profile)
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.sa_logo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Salvation Army",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = SARed
                        )
                    }
                },
                actions = {
                    // Profile Icon (Placeholder for now)
                    IconButton(onClick = {
                        Toast.makeText(context, "Profile & Connect to Corps coming soon!", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = Color.Gray,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        // 2. BOTTOM NAVIGATION BAR
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                // Helper to create items
                val items = listOf(
                    "Home" to Icons.Filled.Home,
                    "Songs" to Icons.Filled.Menu, // Using Menu/List icon for Songbook
                    "Giving" to Icons.Filled.Favorite,
                    "Profile" to Icons.Filled.Person
                )

                items.forEachIndexed { index, pair ->
                    NavigationBarItem(
                        icon = { Icon(pair.second, contentDescription = pair.first) },
                        label = { Text(pair.first, fontWeight = FontWeight.SemiBold) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SARed,
                            selectedTextColor = SARed,
                            indicatorColor = SARed.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        // 3. MAIN SCROLLABLE CONTENT
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9F9F9)) // Soft Gray Background
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // A. WELCOME MESSAGE
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    Text(
                        text = "Welcome Back,",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = userName,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            // B. HERO CARD (Daily Verse)
            item {
                VerseOfTheDayCard()
            }

            // C. NOTICES & EVENTS (The "Updates" Section)
            item {
                Text(
                    text = "Notice Board",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Important Update Card (Red Gradient Style)
                NoticeCard(
                    title = "Special Youth Meeting",
                    subtitle = "This Friday @ 6:00 PM",
                    type = "Event",
                    color = SAYellow.copy(alpha = 0.3f), // Soft Yellow Background
                    accentColor = Color(0xFFE65100) // Orange/Red text
                )

                Spacer(modifier = Modifier.height(12.dp))

                // General Update Card
                NoticeCard(
                    title = "Corps Council Meeting",
                    subtitle = "Sunday after Holiness Meeting",
                    type = "Admin",
                    color = SABlue.copy(alpha = 0.1f),
                    accentColor = SABlue
                )
            }

            // D. THE COMMAND GRID (Big Buttons)
            item {
                Text(
                    text = "Quick Actions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Row 1
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        GridButton(
                            title = "Song Book",
                            icon = Icons.Default.Menu, // Replace with Book Icon later
                            color = Color(0xFFFFF3E0), // Light Orange
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigate("songs") }
                        )
                        GridButton(
                            title = "Giving",
                            icon = Icons.Default.Favorite,
                            color = Color(0xFFFFEBEE), // Light Red
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigate("giving") }
                        )
                    }
                    // Row 2
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        GridButton(
                            title = "Bible",
                            icon = Icons.Default.Info, // Replace with Book Icon
                            color = Color(0xFFE8F5E9), // Light Green
                            modifier = Modifier.weight(1f),
                            onClick = { }
                        )
                        GridButton(
                            title = "Corps Info",
                            icon = Icons.Default.LocationOn,
                            color = Color(0xFFE3F2FD), // Light Blue
                            modifier = Modifier.weight(1f),
                            onClick = { }
                        )
                    }
                }

                // Bottom Spacing for scrolling
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// --- HELPER COMPOSABLES (The Lego Blocks) ---

@Composable
fun VerseOfTheDayCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Gradient (Optional: replace with Image later)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(SARed, Color(0xFF8E0000))
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.CenterStart)
            ) {
                Text(
                    text = "DAILY MANNA",
                    color = SAYellow,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "\"The Lord is my shepherd; I shall not want.\"",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "- Psalm 23:1",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun NoticeCard(
    title: String,
    subtitle: String,
    type: String,
    color: Color,
    accentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicator Line
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .background(accentColor, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            // Type Badge
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = type,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun GridButton(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(0.dp) // Flat style like your design
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f)
            )
        }
    }
}
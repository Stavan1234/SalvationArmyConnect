package com.example.salvationarmyconnect

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.salvationarmyconnect.ui.theme.SARed
import com.example.salvationarmyconnect.ui.theme.SABlue
import com.example.salvationarmyconnect.ui.theme.SAYellow

@Composable
fun SongbookScreen(onBackHome: () -> Unit) {
    val context = LocalContext.current
    val allSongs = remember { SongRepository.getSongs(context) }
    var selectedSong by remember { mutableStateOf<Song?>(null) }

    // --- ATMOSPHERE ENGINE ---
    // We animate the colors so the transition is smooth like a misty morning
    val targetCategory = selectedSong?.category ?: "Default"
    val atmosphere = getAtmosphereForCategory(targetCategory)

    val animatedStartColor by animateColorAsState(atmosphere.first, animationSpec = tween(1000), label = "colorStart")
    val animatedEndColor by animateColorAsState(atmosphere.second, animationSpec = tween(1000), label = "colorEnd")

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(animatedStartColor, animatedEndColor)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        // BACKGROUND WATERMARK (The Shield)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.sa_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .alpha(0.05f) // Subtle watermark
            )
        }

        if (selectedSong == null) {
            SongListWithCapsules(
                allSongs = allSongs,
                onSongClick = { song -> selectedSong = song },
                onBack = onBackHome
            )
        } else {
            SongLyrics(
                song = selectedSong!!,
                onBack = { selectedSong = null }
            )
        }
    }
}

// --- ATMOSPHERE COLORS MAPPING ---
fun getAtmosphereForCategory(category: String): Pair<Color, Color> {
    return when (category) {
        "Salvation" -> Pair(Color(0xFFFFF8E1), Color(0xFFFFEBEE)) // Gold -> Soft Red
        "Warfare" -> Pair(Color(0xFFFFEBEE), Color(0xFFFFCDD2))   // Soft Red -> Courage Red
        "Praise" -> Pair(Color(0xFFFFFDE7), Color(0xFFFFF9C4))    // Bright Yellow -> Gold
        "Prayer" -> Pair(Color(0xFFF3E5F5), Color(0xFFE1BEE7))    // Mist -> Violet
        "Christmas" -> Pair(Color(0xFFE8F5E9), Color(0xFFFFEBEE)) // Soft Green -> Soft Red (Festive)
        "Heaven" -> Pair(Color(0xFFE0F7FA), Color(0xFFB2EBF2))    // Sky Blue -> Clouds
        "Identity" -> Pair(Color(0xFFE3F2FD), Color(0xFFBBDEFB))  // SA Blue tint
        else -> Pair(Color.White, Color(0xFFF5F5F5))              // Default Pure Light
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongListWithCapsules(
    allSongs: List<Song>,
    onSongClick: (Song) -> Unit,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    // Define the official categories list for the Capsules
    val categories = listOf("All", "Salvation", "Warfare", "Praise", "Prayer", "Christmas", "Heaven", "Identity")

    val filteredList = allSongs.filter { song ->
        val matchesSearch = song.id.contains(searchQuery) ||
                song.title_marathi.contains(searchQuery, ignoreCase = true) ||
                song.title_english.contains(searchQuery, ignoreCase = true)

        val matchesCategory = if (selectedCategory == "All") true else song.category == selectedCategory
        matchesSearch && matchesCategory
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // HEADER
        Surface(
            modifier = Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
            color = SARed,
            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Spacer(modifier = Modifier.height(32.dp)) // Avoid Status Bar

                // Top Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                    }
                    Text(
                        "Song Book",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                // Search Bar
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search Number or Name...") },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = SARed) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, null, tint = Color.Gray)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .clip(RoundedCornerShape(30.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White.copy(alpha = 0.95f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )

                // CAPSULE ROW (The "Chips")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SAYellow, // Active Color
                                selectedLabelColor = Color.Black,
                                containerColor = Color.White.copy(alpha = 0.2f), // Inactive Glass
                                labelColor = Color.White
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedCategory == category,
                                borderColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(50) // Full Capsule Shape
                        )
                    }
                }
            }
        }

        // SONG LIST
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredList) { song ->
                SongCard(song, onSongClick)
            }
        }
    }
}

@Composable
fun SongCard(song: Song, onClick: (Song) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick(song) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            // Number Badge
            Surface(
                shape = CircleShape,
                color = SARed.copy(alpha = 0.1f),
                modifier = Modifier.size(50.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = song.id, fontWeight = FontWeight.Bold, color = SARed, fontSize = 18.sp)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))

            // Text Info
            Column {
                Text(text = song.title_marathi, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                Text(text = song.title_english, fontSize = 14.sp, color = Color.Gray)
                // Small Category Tag
                Text(text = "• ${song.category}", fontSize = 12.sp, color = SARed, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun SongLyrics(song: Song, onBack: () -> Unit) {
    var fontSize by remember { mutableFloatStateOf(22f) }

    Scaffold(
        containerColor = Color.Transparent, // Let the Atmosphere Gradient show through
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(32.dp)) // Avoid Status Bar
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.background(Color.White.copy(alpha=0.6f), CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = SARed)
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    // RICH METADATA CHIP (Tune & Eng Ref)
                    if (!song.tune_ref.isNullOrEmpty() || !song.eng_ref.isNullOrEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha=0.6f),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                                if (song.eng_ref != null) {
                                    Text(text = song.eng_ref, fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                                if (song.eng_ref != null && song.tune_ref != null) {
                                    Text(text = " | ", fontSize = 12.sp, color = Color.Gray)
                                }
                                if (song.tune_ref != null) {
                                    Text(text = song.tune_ref, fontSize = 12.sp, color = SARed, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // Zoom Controls
                    Row(modifier = Modifier.background(Color.White.copy(alpha=0.6f), CircleShape)) {
                        IconButton(onClick = { if(fontSize > 16) fontSize -= 2 }) {
                            Text("A-", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        IconButton(onClick = { if(fontSize < 40) fontSize += 2 }) {
                            Text("A+", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onBack, containerColor = SARed, contentColor = Color.White) {
                Icon(Icons.Default.List, "Index")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Song Header
            Text(text = "Song No. ${song.id}", color = SARed, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, letterSpacing = 2.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = song.title_marathi, fontSize = 28.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, lineHeight = 36.sp, color = Color.Black)

            if (song.title_english.isNotEmpty()) {
                Text(text = song.title_english, fontSize = 18.sp, color = Color.Gray, fontStyle = FontStyle.Italic, textAlign = TextAlign.Center)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Divider(color = SARed.copy(alpha = 0.3f), thickness = 2.dp, modifier = Modifier.width(150.dp))
            Spacer(modifier = Modifier.height(24.dp))

            // THE LYRICS LOGIC
            val lines = song.lyrics.split("\n")
            lines.forEach { line ->
                when {
                    // 1. Chorus (Red Color)
                    line.contains("।।धृ.।।") || line.contains("Chorus") -> {
                        Text(
                            text = line,
                            fontSize = fontSize.sp,
                            fontWeight = FontWeight.Bold,
                            color = SARed,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                    // 2. Stanza Number (Bold Black)
                    line.trim().matches(Regex("^\\d+\\.")) -> {
                        Text(
                            text = line,
                            fontSize = (fontSize + 2).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                        )
                    }
                    // 3. Normal Lyric (Dark Gray)
                    else -> {
                        Text(
                            text = line,
                            fontSize = fontSize.sp,
                            lineHeight = (fontSize * 1.6).sp,
                            color = Color.Black.copy(alpha = 0.85f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp)) // Space for FAB
        }
    }
}
package com.example.salvationarmyconnect

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.salvationarmyconnect.ui.theme.SARed
import com.example.salvationarmyconnect.ui.theme.SABlue
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    var isLoading by remember { mutableStateOf(false) }

    // --- 1. GOOGLE SIGN IN SETUP ---
    // This reads the 'google-services.json' automatically.
    // If 'default_web_client_id' is red, try Build -> Rebuild Project.
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    // --- 2. THE RESULT HANDLER ---
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken!!
            val credential = GoogleAuthProvider.getCredential(idToken, null)

            isLoading = true
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    isLoading = false
                    if (task.isSuccessful) {
                        onLoginSuccess() // Navigate to Home
                    } else {
                        Toast.makeText(context, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: ApiException) {
            isLoading = false
            // Common Error 10 = SHA-1 Fingerprint missing in Firebase Console
            Toast.makeText(context, "Google Error: ${e.statusCode}", Toast.LENGTH_LONG).show()
        }
    }

    // --- 3. UI LAYOUT ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Decorative Background Design (Top Right Red Circle)
        Box(
            modifier = Modifier
                .offset(x = 100.dp, y = (-100).dp)
                .size(300.dp)
                .clip(CircleShape)
                .background(SARed.copy(alpha = 0.1f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // LOGO
            Image(
                painter = painterResource(id = R.drawable.sa_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(130.dp)
                    .shadow(12.dp, CircleShape)
                    .padding(bottom = 32.dp),
                contentScale = ContentScale.Fit
            )

            // HEADLINES
            Text(
                text = "Salvation Army\nConnect",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    lineHeight = 36.sp
                ),
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "Welcome, Soldier.",
                fontSize = 18.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // --- GOOGLE BUTTON (Primary) ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clickable {
                        // Launch Google Login
                        googleLauncher.launch(googleSignInClient.signInIntent)
                    },
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = SARed)
                    } else {
                        // Simple "G" text for logo (you can use an icon if you have one)
                        Text("G", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4285F4))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Continue with Google",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- OR DIVIDER ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
                Text("  OR  ", color = Color.Gray, fontSize = 14.sp)
                Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- PHONE BUTTON (Secondary) ---
            Button(
                onClick = {
                    // We will implement the Phone Logic next if you need it
                    Toast.makeText(context, "Use Google for now, Phone coming soon!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F5F5)),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text("📞  Use Phone Number", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }

        // FOOTER
        Text(
            text = "Terms of Service  •  Privacy Policy",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}
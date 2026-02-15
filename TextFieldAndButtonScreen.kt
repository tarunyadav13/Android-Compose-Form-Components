package com.example.mytestapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldAndButtonScreen() {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var limitedText by remember { mutableStateOf("") }
    var googleLoading by remember { mutableStateOf(false) }

    val nameFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            NameTextField(
                value = name,
                onValueChange = { name = it },
                onNext = { passwordFocus.requestFocus() },
                focusRequester = nameFocus
            )

            OutlinedTextFieldWithIcons()
            FilledTextFieldWithIcons()

            PasswordTextField(
                value = password,
                onValueChange = { password = it },
                focusRequester = passwordFocus
            )

            PasswordStrengthIndicator(password)

            LimitedCharacterTextField(
                value = limitedText,
                onValueChange = { limitedText = it }
            )

            GoogleSignInButton(
                loading = googleLoading,
                onClick = {
                    googleLoading = true
                    scope.launch {
                        snackbarHostState.showSnackbar("Signing in…")
                        googleLoading = false
                        snackbarHostState.showSnackbar("Login Successful")
                    }
                }
            )

            GradientButton(text = "Gradient Button") {}
            RadialGradientButton(text = "Radial Button") {}
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onNext: () -> Unit,
    focusRequester: FocusRequester
) {
    val isError = value.isNotEmpty() && value.length < 3

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Name") },
        isError = isError,
        supportingText = {
            if (isError) Text("Minimum 3 characters required")
        },
        leadingIcon = {
            Icon(Icons.Default.Person, contentDescription = null)
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { onNext() }),
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldWithIcons() {
    var text by remember { mutableStateOf("John") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Name") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(onClick = { text = "" }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilledTextFieldWithIcons() {
    var text by remember { mutableStateOf("John") }

    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Name") },
        leadingIcon = {
            Icon(Icons.Default.Person, contentDescription = null)
        },
        trailingIcon = {
            IconButton(onClick = { text = "" }) {
                Icon(Icons.Default.Close, contentDescription = null)
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester
) {
    var visible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Password") },
        visualTransformation = if (visible)
            VisualTransformation.None
        else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    imageVector = if (visible)
                        Icons.Default.Visibility
                    else Icons.Default.VisibilityOff,
                    contentDescription = null
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
    )
}



@Composable
fun PasswordStrengthIndicator(password: String) {
    val strength = remember(password) {
        when {
            password.length < 6 -> "Weak"
            password.any { it.isDigit() } && password.any { it.isUpperCase() } -> "Strong"
            else -> "Medium"
        }
    }

    Text(
        text = "Strength: $strength",
        color = when (strength) {
            "Weak" -> Color.Red
            "Medium" -> Color.Yellow
            else -> Color.Green
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LimitedCharacterTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= 10) onValueChange(it)
        },
        label = { Text("Username") },
        placeholder = { Text("Max 10 characters") },
        modifier = Modifier.fillMaxWidth()
    )
}




@Composable
fun GoogleSignInButton(
    loading: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = "Sign in with Google",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}




@Composable
fun GradientButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF6200EE), Color(0xFF03DAC5))
                    )
                )
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, color = Color.White)
        }
    }
}

@Composable
fun RadialGradientButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFF6200EE), Color(0xFF03DAC5)),
                        radius = 300f
                    )
                )
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, color = Color.White)
        }
    }
}





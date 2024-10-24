package com.example.farmermarket.presentation.screens.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.farmermarket.R
import com.example.farmermarket.Screens


@Composable
fun VerificationScreen(navController: NavController){

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        Image(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.25f),
            painter = painterResource(id = R.drawable.verification_screen_graphic),
            contentDescription = null, // or provide a meaningful description
            contentScale = ContentScale.Crop,
            alignment = Alignment.BottomCenter

        )
        
        
        Spacer(modifier = Modifier.height(70.dp))

        Text(text = "We will send you a one time password on this Mobile Number:",
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.5f))
        
        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "87777777777",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.5f))

        Spacer(modifier = Modifier.height(32.dp))

        var code by remember { mutableStateOf(listOf("", "", "", "")) }



        VerificationCodeInput(onComplete = {code = listOf(it) })

        Spacer(modifier = Modifier.height(100.dp))

        Text(text = "0:30",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.5f))


        Spacer(modifier = Modifier.height(20.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                .border(
                    width = 2.dp,
                    color = Color(0xFF4CAD73),  // Green border
                    shape = RoundedCornerShape(10.dp)
                ),
            colors = ButtonDefaults.buttonColors(
               Color.Transparent  // Transparent background
            ),
            shape = RoundedCornerShape(10.dp),
            onClick = {  navController.navigate(
                Screens.SUCCESS_SCREEN.name
            ) }
        ) {
            Spacer(modifier = Modifier.height(35.dp))
            androidx.compose.material3.Text(text = "Send", color = Color(0xFF4CAD73), fontSize = 18.sp)
            Spacer(modifier = Modifier.height(35.dp))
        }



    }




}

@Composable
fun VerificationCodeInput(
    onComplete: (String) -> Unit
) {
    var code by remember { mutableStateOf(listOf("", "", "", "")) }
    val focusRequesters = List(4) { FocusRequester() }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        code.forEachIndexed { index, value ->
            Spacer(modifier = Modifier.width(5.dp))
            TextField(
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color(0XFFF2F2F2),           // Background color (container)
                    focusedBorderColor = Color(0xFF53B97C),   // Border color when focused
                    unfocusedBorderColor = Color.Transparent // Border color when unfocused
                ),
                value = value,
                onValueChange = { newValue ->
                    if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                        code = code.toMutableList().apply { this[index] = newValue }
                        if (newValue.isNotEmpty() && index < 3) {
                            focusRequesters[index + 1].requestFocus()
                        }
                        if (index == 3 && newValue.isNotEmpty()) {
                            onComplete(code.joinToString(""))
                        }
                    }
                },
                modifier = Modifier
                    .width(60.dp)
                    .focusRequester(focusRequesters[index]),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
    }

    // Request focus on the first field when the composable first loads
//    LaunchedEffect(Unit) {
//        focusRequesters[0].requestFocus()
//    }
}

package com.example.farmermarket.presentation.screens.auth_farmer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.farmermarket.R
import com.example.farmermarket.Screens

@Composable
fun FarmerLoginScreen(navController: NavHostController){


    var loginValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var passwordVisibility by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        Image(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth().height((LocalConfiguration.current.screenHeightDp.dp )/ 2),
            painter = painterResource(id = R.drawable.farmer_login_graphic),
            contentDescription = null, // or provide a meaningful description
            contentScale = ContentScale.Crop,
            alignment = Alignment.BottomCenter

        )
        Text(text = "Email", fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 24.dp, bottom = 16.dp) )

        TextField(

            shape = RoundedCornerShape(10.dp),
            placeholder = { Text(text = "yourmail@mail.com", color = Color(0xFFBDBDBD)) },
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedLabelColor = Color.DarkGray,
                focusedContainerColor = Color(0xFFF2F2F2),
                unfocusedContainerColor = Color(0xFFF2F2F2),
                disabledContainerColor = Color(0xFFF2F2F2),
                cursorColor = Color.Black,
                focusedIndicatorColor = Color(0xFFF2F2F2),
                unfocusedIndicatorColor = Color(0xFFF2F2F2),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
                .focusRequester(focusRequester)
            ,
            value = loginValue,
            onValueChange = { loginValue = it }
        )
        Text(text = "Password", fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 24.dp, bottom = 16.dp) )


        OutlinedTextField(

            shape = RoundedCornerShape(10.dp),
            placeholder = { Text(text = "yourpassword", color = Color(0xFFBDBDBD)) },
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedLabelColor = Color.DarkGray,
                focusedContainerColor = Color(0xFFF2F2F2),
                unfocusedContainerColor = Color(0xFFF2F2F2),
                disabledContainerColor = Color(0xFFF2F2F2),
                cursorColor = Color.Black,
                focusedIndicatorColor = Color(0xFFF2F2F2),
                unfocusedIndicatorColor = Color(0xFFF2F2F2),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(start = 24.dp, end = 24.dp, bottom = 32.dp)
                .focusRequester(focusRequester)
            ,
            visualTransformation = if (passwordVisibility) {
                VisualTransformation.None // Show password
            } else {
                PasswordVisualTransformation() // Hide password
            },
            trailingIcon = {
                val image = if (passwordVisibility) {
                    Icons.Default.Visibility // Icon to indicate password is visible
                } else {
                    Icons.Default.VisibilityOff // Icon to indicate password is hidden
                }

                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            },
            value = passwordValue,
            onValueChange = { passwordValue = it }
        )


        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
            colors = ButtonDefaults.buttonColors( Color(0xFF4CAD73)),
            shape = RoundedCornerShape(10.dp),
            onClick = { navController.navigate(Screens.FARMER_NAVIGATION.name) }) {
            Spacer(modifier = Modifier.height(35.dp))
            Text(text = "Login", color = Color.White, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(35.dp))
        }

        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center){
            Text(text = "Do not have an account? ", color = Color(0xFF0EB177))
            Text(text = "Register",
                modifier = Modifier.clickable {
                    navController.navigate(Screens.FARMER_NAVIGATION.name)
                },
                color = Color(0xFF0EB177),
                fontWeight = FontWeight.Bold)

        }

    }



}
package com.example.farmermarket.presentation.screens.common

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.farmermarket.R
import com.example.farmermarket.Role
import com.example.farmermarket.Screens
import com.example.farmermarket.common.Constants.role

@SuppressLint("ResourceType")
@Composable
fun SuccessScreen(navController: NavController){
    
    Column (modifier = Modifier
        .fillMaxSize()
        .background(color = Color(0xFF4CAD73)),
        horizontalAlignment = Alignment.CenterHorizontally,
        ){

        Image(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.6f),
            painter = painterResource(id = R.drawable.success_screen_graphic),
            contentDescription = null, // or provide a meaningful description
            contentScale = ContentScale.Crop,
            alignment = Alignment.BottomCenter

        )

        Spacer(modifier = Modifier.height(60.dp))

        Text(text = "Successfully registered!", color = Color.White, fontSize = 16.sp)


        Spacer(modifier = Modifier.height(60.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
        colors = ButtonDefaults.buttonColors(Color.White),
        shape = RoundedCornerShape(10.dp),
        onClick = { if(role == Role.FARMER.name ) {
            navController.popBackStack(route = Screens.BUYER_LOGIN.name, inclusive = false)}
            else{
            navController.popBackStack(route = Screens.FARMER_LOGIN.name, inclusive = false)
            }}) {
        Spacer(modifier = Modifier.height(35.dp))
        Text(text = "Back to Login Screen", color =  Color(0xFF4CAD73), fontSize = 18.sp)
        Spacer(modifier = Modifier.height(35.dp))
    }

    }
    
    
}
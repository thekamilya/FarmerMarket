package com.example.farmermarket.presentation.screens.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.farmermarket.R
import com.example.farmermarket.Role
import com.example.farmermarket.Screens
import com.example.farmermarket.common.Constants.role

@Composable
fun StartScreen(navController: NavHostController){


    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {


        Image(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.group_796),
            contentDescription = null, // or provide a meaningful description
            contentScale = ContentScale.Crop

        )
        
        Spacer(modifier = Modifier.height(100.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
            colors = ButtonDefaults.buttonColors( Color(0xFF4CAD73)),
            shape = RoundedCornerShape(10.dp),
            onClick = { role = Role.FARMER.name ;  navController.navigate(Screens.BUYER_LOGIN.name) }) {
            Spacer(modifier = Modifier.height(35.dp))
            Text(text = "Continue as Buyer", color = Color.White, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(35.dp))
        }
        Spacer(modifier = Modifier.height(25.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
            colors = ButtonDefaults.buttonColors( Color(0xFF4CAD73)),
            shape = RoundedCornerShape(10.dp),
            onClick = { role = Role.BUYER.name ; navController.navigate(Screens.FARMER_LOGIN.name)  }) {
            Spacer(modifier = Modifier.height(35.dp))
            Text(text = "Continue as Farmer", color = Color.White, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(35.dp))
        }





        
    }


}













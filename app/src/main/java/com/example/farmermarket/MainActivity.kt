package com.example.farmermarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.farmermarket.ui.theme.GoogleBooksApiDirectTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoogleBooksApiDirectTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    BookListScreen()
//                    LazyColumn(){
//                        items(fruitNames){fruit ->
//                            Text(text = fruit)
//
//                        }
//                    }
                }
            }
        }
    }
}

@Composable
fun BookListScreen(viewModel: ViewModel = hiltViewModel()){


    LaunchedEffect(Unit) {
        viewModel.getBooks()
    }
    val bookList = viewModel.listState.value
    
    LazyColumn(){
        items(bookList){book->
            val description = book.volumeInfo.description ?: "No description available"
            Text(text = description)

            
        }
    }
    
    




}


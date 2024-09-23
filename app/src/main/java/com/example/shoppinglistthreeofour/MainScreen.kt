package com.example.shoppinglistthreeofour

import android.*
import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen() {

    var isAdding by remember {
        mutableStateOf(false)
    }

    var index by remember {
        mutableIntStateOf(1)
    }

    var listOfShoppingItems by remember {
        mutableStateOf(listOf<ShoppingItem>())
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isAdding) {
            AddDialog(id = index, onDismissRequest = {
                isAdding = false
            }, onCompleteDialog = {
                listOfShoppingItems += it
                index++
            })
        }
        Button(modifier = Modifier.padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
            elevation = ButtonDefaults.elevatedButtonElevation(15.dp),
            onClick = {
                isAdding = true
            }) {
            Text("Добавить")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(listOfShoppingItems) { element ->
                if (element.isEditing) {
                    EditShoppingItem(onDismissRequest = {},
                        shoppingItem = element,
                        onCompleteDialog = {},
                        onEditDone = { title, description, quantity ->
                            listOfShoppingItems =
                                listOfShoppingItems.map { it.copy(isEditing = false) }
                            val editedItem =
                                listOfShoppingItems.find { item -> item.id == element.id }
                            editedItem?.let {
                                it.title = title
                                it.description = description
                                it.quantity = quantity
                            }
                        })
                } else {
                    ShoppingItemCard(shoppingItem = element, onEditClicked = {
                        listOfShoppingItems =
                            listOfShoppingItems.map { it.copy(isEditing = it.id == element.id) }
                    }, onDeleteClicked = {
                        listOfShoppingItems -= it
                    })
                }
            }
        }
    }
}

@Composable
fun ShoppingItemCard(
    shoppingItem: ShoppingItem,
    onDeleteClicked: (ShoppingItem) -> Unit,
    onEditClicked: (ShoppingItem) -> Unit
) {
    Card(
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
        border = BorderStroke(2.dp, color = Color.DarkGray),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = shoppingItem.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = shoppingItem.description, fontSize = 16.sp, color = Color.Gray
                )
            }
            Column(
                modifier = Modifier.weight(0.3f)
            ) {
                Text(text = "Quantity:")
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = shoppingItem.quantity.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.End
                )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                IconButton(modifier = Modifier.width(20.dp), onClick = {
                    onEditClicked(shoppingItem)
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit, contentDescription = ""
                    )
                }
                IconButton(modifier = Modifier.width(20.dp), onClick = {
                    onDeleteClicked(shoppingItem)
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete, contentDescription = ""
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDialog(id: Int, onCompleteDialog: (ShoppingItem) -> Unit, onDismissRequest: () -> Unit) {

    var title by remember {
        mutableStateOf("")
    }

    var descritpion by remember {
        mutableStateOf("")
    }

    var quantity by remember {
        mutableIntStateOf(0)
    }

    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .background(
                    color = Color.White, shape = RoundedCornerShape(10.dp)
                ), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(4.dp))
            TextField(
                modifier = Modifier
                    .border(
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(2.dp, Color.LightGray)
                    )
                    .background(color = Color.White)
                    .padding(1.dp)
                    .clip(RoundedCornerShape(20.dp)),
                label = { Text("Name") },
                value = title,
                onValueChange = {
                    title = it
                })
            Spacer(Modifier.height(8.dp))
            TextField(
                modifier = Modifier
                    .border(
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(2.dp, Color.LightGray)
                    )
                    .background(color = Color.White)
                    .padding(1.dp)
                    .clip(RoundedCornerShape(20.dp)),
                label = { Text("Description") },
                value = descritpion,
                onValueChange = {
                    descritpion = it
                })
            Spacer(Modifier.height(8.dp))
            TextField(
                modifier = Modifier
                    .border(
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(2.dp, Color.LightGray)
                    )
                    .background(color = Color.White)
                    .padding(1.dp)
                    .clip(RoundedCornerShape(20.dp)),
                label = { Text("Quantity") },
                value = quantity.toString(),
                onValueChange = {
                    quantity = it.toIntOrNull() ?: 0
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.holo_red_dark)),
                    onClick = {
                        onDismissRequest()
                }) {
                    Text("Cancel")
                }
                Button(colors = ButtonDefaults.buttonColors(Color.Gray), onClick = {
                    onCompleteDialog(
                        ShoppingItem(
                            id = id,
                            title = title,
                            description = descritpion,
                            quantity = quantity
                        )
                    )
                    onDismissRequest()
                }) {
                    Text("Add")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditShoppingItem(
    shoppingItem: ShoppingItem,
    onEditDone: (String, String, Int) -> Unit,
    onDismissRequest: () -> Unit,
    onCompleteDialog: (ShoppingItem) -> Unit
) {

    var editedTitle by remember {
        mutableStateOf(shoppingItem.title)
    }

    var editedDescription by remember {
        mutableStateOf(shoppingItem.description)
    }

    var editedQuantity by remember {
        mutableIntStateOf(shoppingItem.quantity)
    }


    BasicAlertDialog(
        onDismissRequest = onDismissRequest, modifier = Modifier.background(
            color = Color.White, shape = RoundedCornerShape(10.dp)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .background(
                    color = Color.White, shape = RoundedCornerShape(10.dp)
                ), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(4.dp))
            TextField(
                modifier = Modifier
                    .border(
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(2.dp, Color.LightGray)
                    )
                    .background(color = Color.White)
                    .padding(1.dp)
                    .clip(RoundedCornerShape(20.dp)),
                label = { Text("Title") },
                value = editedTitle,
                onValueChange = { newTitle ->
                    editedTitle = newTitle
                })
            Spacer(Modifier.height(4.dp))
            TextField(
                modifier = Modifier
                    .border(
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(2.dp, Color.LightGray)
                    )
                    .background(color = Color.White)
                    .padding(1.dp)
                    .clip(RoundedCornerShape(20.dp)),
                singleLine = true,
                label = { Text("Description") },
                value = editedDescription,
                onValueChange = { newDescription ->
                    editedDescription = newDescription
                })
            Spacer(Modifier.height(4.dp))
            TextField(
                modifier = Modifier
                    .border(
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(2.dp, Color.LightGray)
                    )
                    .background(color = Color.White)
                    .padding(1.dp)
                    .clip(RoundedCornerShape(20.dp)),
                label = { Text("Quantity") },
                value = editedQuantity.toString(),
                onValueChange = { newQuantity ->
                    editedQuantity = newQuantity.toIntOrNull() ?: 0
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.holo_red_dark)),
                    onClick = {
                    onEditDone(
                        shoppingItem.title, shoppingItem.description, shoppingItem.quantity
                    )
                }) {
                    Text("Cancel")
                }
                Button(colors = ButtonDefaults.buttonColors(Color.DarkGray),
                    onClick = {
                    onEditDone(
                        editedTitle, editedDescription, editedQuantity
                    )
                }) {
                    Text("Confirm")
                }
            }
        }
    }
}

//    Card(
//        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
//        border = BorderStroke(2.dp, color = Color.DarkGray),
//        elevation = CardDefaults.elevatedCardElevation(15.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(8.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//                BasicTextField(
//                    value = editedTitle,
//                    onValueChange = {
//                        editedTitle = it
//                    },
//                )
//                Spacer(Modifier.height(2.dp))
//                BasicTextField(
//                    value = editedDescription,
//                    onValueChange = {
//                        editedDescription = it
//                    },
//                )
//            }
//            Column(
//                modifier = Modifier.weight(0.3f)
//            ) {
//                Text(text = "Quantity:")
//                BasicTextField(
//                    value = editedQuantity.toString(),
//                    onValueChange = {
//                        editedQuantity = it.toInt()
//                    },
//                )
//            }
//            IconButton(onClick = {
//                onEditDone(editedTitle, editedDescription, editedQuantity)
//            }) {
//                Icon(
//                    imageVector = Icons.Filled.Check, contentDescription = ""
//                )
//            }
//        }
//    }
//}


@Preview(showBackground = true)
@Composable
fun Preview() {
    MainScreen()
}


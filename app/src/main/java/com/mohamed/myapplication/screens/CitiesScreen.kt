package com.mohamed.myapplication.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohamed.domain.entity.CityEntity
import com.mohamed.myapplication.R
import com.mohamed.myapplication.screens.CitiesContract.CitySearchSideEffect.ShowError

@Composable
fun CitiesScreen(
    viewModel: CitySearchViewModel = hiltViewModel(), context: Context = LocalContext.current
) {
    val state by viewModel.uiState.collectAsState()
    val searchQuery = state.searchQuery
    val isLoading = state.isLoading
    val cities = state.cities
    val isEmpty = state.isEmpty
    val listSize = state.listSize

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                is ShowError -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEAEAEA))
        ) {

            Text("City Search", fontSize = 25.sp, modifier = Modifier.padding(16.dp))

            Text(
                if (isLoading || listSize < 0) "Loading"
                else "$listSize cities",
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )


            Box(
                modifier = Modifier.weight(1f)
            ) {
                when {
                    isLoading -> LoadingState()
                    isEmpty -> EmptyState()
                    else -> {
                        CityList(
                            cities = cities,
                            listSize = listSize,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
            ) {
                SearchBar(
                    query = searchQuery, onQueryChange = {
                        viewModel.onEvent(CitiesContract.CitySearchEvent.OnSearchQueryChanged(it))
                    }, modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun CityList(
    cities: List<CityEntity>, listSize: Int, modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val groupedCities =
        remember(cities) { cities.groupBy { it.displayName.first().uppercaseChar() } }

    Row(modifier = modifier) {

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            groupedCities.forEach { (letter, citiesInGroup) ->
                stickyHeader(key = letter) {
                    GroupHeader(letter = letter)
                }

                items(
                    citiesInGroup.size,
                    key = { index -> citiesInGroup[index].id }
                ) { index ->
                    CityItem(
                        city = citiesInGroup[index],
                        isEndOfList = index == listSize.minus(1),
                        onClick = { openInMaps(context, citiesInGroup[index]) })
                }
            }
        }
    }
}

fun openInMaps(context: Context, city: CityEntity) = context.startActivity(
    Intent(
        Intent.ACTION_VIEW,
        "geo:${city.latitude},${city.longitude}?q=${city.latitude},${city.longitude}(${city.displayName})".toUri()
    )
)


@Composable
fun TimeLine(modifier: Modifier = Modifier) = Box(
    modifier = modifier
        .width(1.dp)
        .background(Color.LightGray)
)

@Composable
fun CityItem(
    city: CityEntity, isEndOfList: Boolean, onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp)
    ) {
        Box {
            TimeLine(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .height(160.dp)
            )

            if (isEndOfList) Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(color = Color.Gray, shape = CircleShape)
                    .align(Alignment.BottomCenter)

            )
        }
        Spacer(modifier = Modifier.size(28.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .clickable { onClick() },
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .height(100.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(R.drawable.ic_placeholder),
                    contentDescription = "",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )


                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "${city.displayName},${city.country}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "${city.latitude}, ${city.longitude}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun LoadingState() = Box(
    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
) {
    CircularProgressIndicator(
        modifier = Modifier.size(48.dp), color = MaterialTheme.colorScheme.primary
    )
}


@Composable
fun GroupHeader(
    letter: Char, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .wrapContentSize()
            .padding(start = 8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color = Color.White, shape = CircleShape)
        ) {

            Text(
                text = letter.toString(),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }


        TimeLine(
            modifier = Modifier
                .height(12.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}


@Composable
fun SearchBar(
    query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier
) {
    var isFocused = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val animatedCornerRadius = animateDpAsState(
        targetValue = if (isFocused.value) 0.dp else 8.dp, animationSpec = tween(300)
    )

    val animatedPadding = animateDpAsState(
        targetValue = if (isFocused.value) 0.dp else 16.dp, animationSpec = tween(300)
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(animatedPadding.value),
        shape = RoundedCornerShape(animatedCornerRadius.value),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "search",
                tint = if (isFocused.value) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(12.dp))

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onFocusChanged { isFocused.value = it.isFocused },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                decorationBox = { innerTextField ->
                    Box {
                        if (query.isEmpty()) {
                            Text(
                                text = "Search cities...",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        innerTextField()
                    }
                })

            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") }, modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "clear",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
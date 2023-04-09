package com.example.superhero

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.superhero.model.Hero
import com.example.superhero.model.HeroesRepository.heroes
import com.example.superhero.ui.theme.SuperHeroTheme

@Composable
fun SuperHeroesApp() {
    Scaffold(
        topBar = { SuperHeroesTopAppBar() }
    ) {innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colors.background)
        ) {
            items(heroes) {
                SuperHeroesItem(hero = it)
            }
        }
    }
}

@Composable
fun SuperHeroesItem(hero: Hero, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = 6.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                SuperHeroesIcon(heroIcon = hero.imageRes)
                SuperHeroesInformation(heroName = hero.nameRes, description = hero.descriptionRes)
            }
        }
    }
}

@Composable
fun SuperHeroesIcon(@DrawableRes heroIcon: Int, modifier: Modifier = Modifier) {
    Image(
        modifier = modifier
            .size(64.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp)),
        painter = painterResource(id = heroIcon),
        contentDescription = null
    )
}

@Composable
fun SuperHeroesInformation(@StringRes heroName: Int, @StringRes description: Int, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = stringResource(id = heroName),
            style = MaterialTheme.typography.h2
        )
        Text(
            text = stringResource(id = description),
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun SuperHeroesTopAppBar(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.top_bar),
            style = MaterialTheme.typography.h1,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SuperHeroTheme {
        SuperHeroesApp()
    }
}
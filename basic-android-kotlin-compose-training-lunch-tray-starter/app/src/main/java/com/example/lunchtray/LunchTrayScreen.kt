/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lunchtray

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.datasource.DataSource
import com.example.lunchtray.model.MenuItem
import com.example.lunchtray.ui.*

// TODO: Screen enum
enum class LunchTrayScreen() {
    Start,
    Entree,
    SideDish,
    Accompaniment,
    Checkout
}

// TODO: AppBar
@Composable
fun LunchTrayAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    currentScreen: LunchTrayScreen
) {
    TopAppBar(
        title = {
            when (currentScreen) {
                LunchTrayScreen.Start -> Text(text = stringResource(id = R.string.start_order))
                LunchTrayScreen.Entree -> Text(text = stringResource(id = R.string.choose_entree))
                LunchTrayScreen.SideDish -> Text(text = stringResource(id = R.string.choose_side_dish))
                LunchTrayScreen.Accompaniment -> Text(text = stringResource(id = R.string.choose_accompaniment))
                LunchTrayScreen.Checkout -> Text(text = stringResource(id = R.string.order_checkout))
            } },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(
                            id = R.string.back_button
                        )
                    )
                }
            }
        }
    )
}

@Composable
fun LunchTrayApp(modifier: Modifier = Modifier) {
    // TODO: Create Controller and initialization
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Create ViewModel
    val viewModel: OrderViewModel = viewModel()
    val currentScreen =
        LunchTrayScreen.valueOf(backStackEntry?.destination?.route ?: LunchTrayScreen.Start.name)


    Scaffold(
        topBar = {
            // TODO: AppBar
            LunchTrayAppBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                currentScreen = currentScreen)
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        // TODO: Navigation host
        NavHost(
            navController = navController,
            startDestination = LunchTrayScreen.Start.name,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(LunchTrayScreen.Start.name) {
                StartOrderScreen(onStartOrderButtonClicked = {
                    navController.navigate(LunchTrayScreen.Entree.name)
                })
            }
            composable(LunchTrayScreen.Entree.name) {
                EntreeMenuScreen(
                    options = DataSource.entreeMenuItems,
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(
                            viewModel,
                            navController
                        ) },
                    onNextButtonClicked = { navController.navigate(LunchTrayScreen.SideDish.name) },
                    onSelectionChanged = { viewModel.updateEntree(it) }
                )
            }
            composable(LunchTrayScreen.SideDish.name) {
                SideDishMenuScreen(
                    options = DataSource.sideDishMenuItems,
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(
                            viewModel,
                            navController
                        )
                    },
                    onNextButtonClicked = { navController.navigate(LunchTrayScreen.Accompaniment.name) },
                    onSelectionChanged = { viewModel.updateSideDish(it) }
                )
            }
            composable(LunchTrayScreen.Accompaniment.name) {
                AccompanimentMenuScreen(
                    options = DataSource.accompanimentMenuItems,
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(
                            viewModel,
                            navController
                        )
                    },
                    onNextButtonClicked = { navController.navigate(LunchTrayScreen.Checkout.name) },
                    onSelectionChanged = { viewModel.updateAccompaniment(it) }
                )
            }
            composable(LunchTrayScreen.Checkout.name) {
                CheckoutScreen(
                    orderUiState = uiState,
                    onNextButtonClicked = { navController.navigate(LunchTrayScreen.Start.name) },
                    onCancelButtonClicked = { cancelOrderAndNavigateToStart(viewModel, navController) })
            }
        }
    }
}

private fun cancelOrderAndNavigateToStart(
    viewModel: OrderViewModel,
    navController: NavController
) {
    viewModel.resetOrder()
    navController.popBackStack(LunchTrayScreen.Start.name, inclusive = false)
}

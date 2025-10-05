package com.example.hobbyhub.mainui.onboardingscreen.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hobbyhub.core.navigations.Screen
import com.example.hobbyhub.domain.model.OnBoardingPage
import com.example.hobbyhub.domain.model.onBoardingPages // REMAINED for PageIndicator/PagerState initialization
import kotlinx.coroutines.launch
import androidx.hilt.navigation.compose.hiltViewModel // ADDED IMPORT
import com.example.hobbyhub.mainui.onboardingscreen.viewmodel.OnboardingViewModel // ADDED IMPORT


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: OnboardingViewModel = hiltViewModel() // INJECTED VIEWMODEL
) {
    // NOTE: pageCount relies on the size of the original list,
    // but the actual list pages are loaded from the ViewModel.
    val pages = viewModel.pages
    val pagerState = rememberPagerState(pageCount = { pages.size })

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { pageIndex -> // Changed iterator name to pageIndex for clarity
            OnboardingPageContent(page = pages[pageIndex]) // Used pages from ViewModel
        }

        OnboardingFooter(
            pagerState = pagerState,
            onNextClicked = {
                viewModel.onOnboardingComplete() // Delegate completion logic to ViewModel
                navController.popBackStack() // Remove onboarding from the back stack
                navController.navigate(Screen.LoginScreen.route)
            },
            onSkipClicked = {
                viewModel.onOnboardingComplete() // Delegate completion logic to ViewModel
                navController.popBackStack()
                navController.navigate(Screen.LoginScreen.route)
            }
        )
    }
}

@Composable
fun OnboardingPageContent(page: OnBoardingPage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = page.image),
            contentDescription = page.title,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(1f),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingFooter(
    pagerState: PagerState,
    onNextClicked: () -> Unit,
    onSkipClicked: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    // NOTE: onBoardingPages is still referenced here for size/last page calculation,
    // which is acceptable since it's a constant list from the domain layer.
    val isLastPage = pagerState.currentPage == onBoardingPages.size - 1

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PageIndicator(pageCount = onBoardingPages.size, currentPage = pagerState.currentPage)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onSkipClicked, enabled = !isLastPage) {
                Text(text = if (isLastPage) "" else "Skip", color = if (isLastPage) Color.Transparent else MaterialTheme.colorScheme.primary)
            }

            Button(onClick = {
                if (isLastPage) {
                    onNextClicked()
                } else {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }) {
                Text(text = if (isLastPage) "Get Started" else "Next")
            }
        }
    }
}

@Composable
fun PageIndicator(pageCount: Int, currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { iteration ->
            val color = if (currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
            Box(
                modifier = Modifier
                    .size(if (currentPage == iteration) 12.dp else 8.dp)
                    .clip(CircleShape)
                    .let {
                        // Applying background color using a let block
                        // for better readability
                        it.background(color)
                    }
            )
        }
    }
}
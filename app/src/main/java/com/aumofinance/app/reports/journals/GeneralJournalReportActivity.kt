package com.aumofinance.app.reports.journals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.aumofinance.app.ui.components.SnackbarMessageHost
import com.aumofinance.app.ui.theme.AumoTheme

class GeneralJournalReportActivity : ComponentActivity() {
    private val viewModel: JournalReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AumoTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    JournalReportScreen(
                        entries = viewModel.entries,
                        selectedPeriodName = viewModel.selectedPeriodName,
                        defaultPeriodLabel = "No period selected",
                    )
                    SnackbarMessageHost(
                        message = viewModel.snackbarMessage,
                        onConsumed = { viewModel.clearSnackbar() },
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadGeneral()
    }
}

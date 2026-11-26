package com.aumomobile.app.reports.ledgers.permanent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.aumomobile.app.reports.ledgers.LedgerScreen
import com.aumomobile.app.ui.theme.AumoTheme

class GeneralLedgerPermanentActivity : ComponentActivity() {
    private val viewModel: GeneralLedgerPermanentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AumoTheme {
                LedgerScreen(
                    report = viewModel.report,
                    isLoading = viewModel.isLoading,
                    errorMessage = viewModel.errorMessage,
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.load()
    }
}

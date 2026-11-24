package com.aumofinance.app.reports.journals

// General Journal: tampilan & host dipakai bersama lewat
// BaseJournalReportActivity; hanya endpoint yang berbeda.
class GeneralJournalReportActivity : BaseJournalReportActivity() {
    override fun loadJournal(viewModel: JournalReportViewModel) {
        viewModel.loadGeneral()
    }
}

package com.aumofinance.app.reports.journals

// Adjusting Journal: sama seperti General Journal, tapi backend sudah
// memfilter journalType "Adjusting" saja. Tampilan & host dipakai bersama
// lewat BaseJournalReportActivity; hanya endpoint yang berbeda.
class AdjustingJournalReportActivity : BaseJournalReportActivity() {
    override fun loadJournal(viewModel: JournalReportViewModel) {
        viewModel.loadAdjusting()
    }
}

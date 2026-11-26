package com.aumomobile.app.reports.journals

// Reversing Journal (Jurnal Pembalik): tampilan & host dipakai bersama lewat
// BaseJournalReportActivity; hanya endpoint yang berbeda.
class ReversingJournalReportActivity : BaseJournalReportActivity() {
    override fun loadJournal(viewModel: JournalReportViewModel) {
        viewModel.loadReversing()
    }
}

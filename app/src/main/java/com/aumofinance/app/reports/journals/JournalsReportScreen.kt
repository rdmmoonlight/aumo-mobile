package com.aumomobile.app.reports.journals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aumomobile.app.core.CurrencyFormatter
import com.aumomobile.app.ui.theme.AumoColors
import com.aumomobile.app.ui.theme.AumoDimens
import java.text.SimpleDateFormat
import java.util.Locale

// Lebar indentasi/tab nomor referensi baris kredit — dipakai juga untuk
// menggeser mundur (backspace) nominal debit sejauh jarak yang sama, supaya
// nominal debit & kredit sengaja tidak sejajar.
private const val CREDIT_INDENT = "        "

// Label periode bawaan, satu untuk General maupun Adjusting Journal.
private const val DEFAULT_PERIOD_LABEL = "Belum ada periode dipilih"

private val inputDateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
private val displayDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("in", "ID"))
private val displayTimestampFormat = SimpleDateFormat("dd/MM HH:mm", Locale("in", "ID"))

/**
 * Halaman General/Adjusting Journal (hanya tampilan baca; tanpa edit/hapus).
 * Dipakai bersama oleh GeneralJournalReportActivity dan
 * AdjustingJournalReportActivity.
 */
@Composable
fun JournalReportScreen(
    entries: List<JournalReportEntry>,
    selectedPeriodName: String?,
) {
    Scaffold(containerColor = AumoColors.Background) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(AumoDimens.SpacingLarge),
        ) {
            Text(
                text = "${selectedPeriodName ?: DEFAULT_PERIOD_LABEL} \u00B7 Nominal dalam Rupiah",
                color = AumoColors.TextMuted,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                modifier = Modifier.fillMaxWidth(),
            )

            val grouped =
                remember(entries) {
                    entries.groupBy { it.entryDate.substringBefore("T") }.toSortedMap()
                }

            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = PaddingValues(top = AumoDimens.SpacingSmall, bottom = AumoDimens.SpacingLarge),
            ) {
                grouped.forEach { (dateKey, entriesForDate) ->
                    item(key = "header-$dateKey") {
                        Text(
                            text = formatDateLabel(dateKey),
                            color = AumoColors.TextMuted,
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            modifier = Modifier.padding(top = AumoDimens.SpacingLarge, bottom = AumoDimens.SpacingSmall),
                        )
                    }
                    items(entriesForDate, key = { it.id }) { entry ->
                        EntryCard(entry = entry)
                    }
                }
            }
        }
    }
}

@Composable
private fun EntryCard(entry: JournalReportEntry) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = AumoDimens.SpacingSmall)
                .background(AumoColors.Surface, RoundedCornerShape(8.dp))
                .padding(AumoDimens.SpacingLarge),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = entry.transactionNumber,
                color = AumoColors.TextPrimary,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = formatTimestampLabel(entry),
                color = AumoColors.TextMuted,
                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                modifier = Modifier.padding(end = AumoDimens.SpacingSmall),
            )
        }

        Spacer(modifier = Modifier.height(AumoDimens.SpacingSmall))

        entry.lines.orEmpty().sortedBy { it.lineOrder }.forEach { line ->
            JournalLineRow(line)
        }
    }
}

// Nama akun + deskripsi (pemisah " - "); nominal tanpa "Rp" (mata uang
// sudah dinyatakan sekali di header halaman). Debit & kredit sama-sama
// rata kanan. Kredit mepet penuh ke tepi; nominal debit sengaja digeser
// mundur (backspace) sejauh lebar CREDIT_INDENT — dicapai dengan spasi
// kosong di akhir teks nominal debit.
@Composable
private fun JournalLineRow(line: JournalReportLine) {
    val label =
        buildString {
            append(line.referenceNumber).append(" - ").append(line.accountName)
            if (!line.lineDescription.isNullOrBlank()) {
                append(" - ").append(line.lineDescription)
            }
        }
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 1.dp)) {
        if (line.debit > 0) {
            Text(
                text = label,
                color = AumoColors.TextPrimary,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = CurrencyFormatter.formatBare(line.debit) + CREDIT_INDENT,
                color = AumoColors.TextPrimary,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
            )
        } else {
            // Kredit: indentasi satu tab (spasi) dari debit, nominal di
            // kolom kanan mepet penuh ke tepi.
            Text(
                text = "$CREDIT_INDENT$label",
                color = AumoColors.TextPrimary,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = CurrencyFormatter.formatBare(line.credit),
                color = AumoColors.TextPrimary,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
            )
        }
    }
}

private fun formatDateLabel(dateKey: String): String =
    try {
        displayDateFormat.format(inputDateTimeFormat.parse("${dateKey}T00:00:00")!!)
    } catch (e: Exception) {
        dateKey
    }

// Timestamp created/edited, ditaruh compact di sebelah kanan baris nomor
// transaksi (tepat di bawah tanggal entri). Hanya tampilkan "Diubah" jika
// entry pernah diedit (updatedAt beda dari createdAt).
private fun formatTimestampLabel(entry: JournalReportEntry): String {
    val createdLabel = formatTimestamp(entry.createdAt)
    val updated = entry.updatedAt
    return if (updated != null && updated != entry.createdAt) {
        "Dibuat $createdLabel \u00B7 Diubah ${formatTimestamp(updated)}"
    } else {
        "Dibuat $createdLabel"
    }
}

private fun formatTimestamp(raw: String): String =
    try {
        val normalized = raw.substringBefore(".").let { if (it.length == 10) "${it}T00:00:00" else it }
        displayTimestampFormat.format(inputDateTimeFormat.parse(normalized)!!)
    } catch (e: Exception) {
        raw
    }

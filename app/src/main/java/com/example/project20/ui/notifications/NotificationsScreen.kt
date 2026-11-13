package com.example.project20.ui.notifications

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.project20.R

private data class Notice(val title: String, val url: String)

@Composable
fun NotificationsScreen() {
    val uriHandler = LocalUriHandler.current
    val notices = listOf(
        Notice(stringResource(R.string.First), "https://www.eccprayagraj.org/uploads/notices/Notice%20regarding%20Ramnavmi%20Holiday-O4qX4Q.pdf"),
        Notice(stringResource(R.string.Second), "https://www.eccprayagraj.org/uploads/notices/M.A.%20-%20M.%20Sc%20Semester-I%20-%20III%20Scrutiny%20Result%202023-24-eqsmca.pdf"),
        Notice(stringResource(R.string.Third), "https://www.eccprayagraj.org/uploads/notices/Alumni%20Meet%20at%20Allahabad%20University-sKRz7h.pdf"),
        Notice(stringResource(R.string.Fourth), "https://www.eccprayagraj.org/uploads/notices/B._Ed._Semester-II_Exam._Schedule-1--FHD1YM.pdf"),
        Notice(stringResource(R.string.Fifth), "https://www.eccprayagraj.org/uploads/notices/Alumni%20Meet%20at%20Allahabad%20University-sKRz7h.pdf"),
        Notice(stringResource(R.string.Sixth), "https://www.eccprayagraj.org/uploads/notices/UG%20and%20PG%20Classes%20will%20Suspended%20after%204th%20Period%20on%2003.04.2024-KuRCHO.pdf"),
        Notice(stringResource(R.string.Seventh), "https://www.eccprayagraj.org/uploads/notices/IMG-20240320-WA0000-vuNtXR.pdf"),
        Notice(stringResource(R.string.Eighth), "https://www.eccprayagraj.org/uploads/notices/Notice%20regarding%20Holiday%20for%20Holi%20-%20Good%20Friday-xTaoTM.pdf"),
        Notice(stringResource(R.string.Ninth), "https://www.eccprayagraj.org/uploads/notices/Tutorial%20Examination%20Schedule%20for%20B.%20Com.%20Sem.-%20II%20-%20IV%20-Session%202023-24--rvjXrT.pdf"),
        Notice(stringResource(R.string.Tenth), "https://www.eccprayagraj.org/uploads/notices/ISO%20Certificates-aTkhs1.pdf"),
        Notice(stringResource(R.string.Eleventh), "https://www.eccprayagraj.org/uploads/notices/Mahashivratri%20Holiday%20Notice%20-08.03.2024--QbUOkD.pdf"),
        Notice(stringResource(R.string.Twelveth), "https://www.eccprayagraj.org/uploads/notices/SEC%20Sems-IV%20-Swapnil--i0Wf7g.pdf"),
        Notice(stringResource(R.string.Thirteenth), "https://www.eccprayagraj.org/uploads/notices/Notification%20regarding%20Instructions%20for%20Keep%20College%20Campus%20Clean%20-%20Green-u4sbwy.pdf"),
        Notice(stringResource(R.string.Fourteenth), "https://www.eccprayagraj.org/uploads/notices/ALL_Notice_scrutiny-ri2orD.pdf"),
        Notice(stringResource(R.string.Fifteenth), "https://www.eccprayagraj.org/uploads/notices/Teaching%20suspended%20on%20Sports%20Day%20-01.03.2024--HdnpLL.pdf"),
        Notice(stringResource(R.string.Sixteenth), "https://www.eccprayagraj.org/uploads/notices/Tutorial%20Test%20Schedule%20with%20Notice-y3Kcnv.pdf"),
        Notice(stringResource(R.string.Seventeenth), "https://www.eccprayagraj.org/uploads/notices/Annual%20Sports%20Competition-LGKD31.pdf")
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Notifications & Announcements",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(notices) { notice ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { uriHandler.openUri(notice.url) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = notice.title,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

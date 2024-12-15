package com.ismael.teams.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppTypography
import com.ismael.teams.R

@Composable
fun SideNavBarItems(
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet {
        Spacer(
            modifier = Modifier
                .height(40.dp)
        )
        NavigationDrawerItem(
            label = {
                UserNameAndCompany()
            },
            selected = false,
            onClick = {}
        )
    }

}

@Composable
fun UserNameAndCompany(
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
    ) {
        UserIcon(
            painter = painterResource(R.drawable.perfil),
            contentDescription = null,
            onclick = { },
            modifier = Modifier
                //.padding(start = 8.dp)
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
        ) {
            Text(
                text = "Ismael Nunes Campos - External",
                fontWeight = FontWeight.Bold,
                style = AppTypography.bodyLarge
            )
            Text(
                text = "NUNES EQUIPAMENTOS ELETRICOS LTDA",
                style = AppTypography.labelSmall,
                fontWeight = FontWeight.Thin
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SideNavBarItemsPreview() {
    SideNavBarItems()
}
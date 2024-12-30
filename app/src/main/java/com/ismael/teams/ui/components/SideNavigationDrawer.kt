package com.ismael.teams.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppTypography
import com.ismael.teams.R
import com.ismael.teams.ui.screens.chat.UserIcon
import com.ismael.teams.ui.screens.chat.UserIconWithStatus

@Composable
fun SideNavBarItems(
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier
                .height(40.dp)
        )
        NavigationDrawerItem(
            label = {
                UserDetails(
                    userName = "Ismael Nunes Campos - External",
                    secondaryText = "NUNES EQUIPAMENTOS ELETRICOS LTDA"
                )
            },
            selected = false,
            onClick = {}
        )
        NavigationDrawerItem(
            label = {
                SideBarNavigationItems(
                    icon = R.drawable.draft_orders_24px,
                    topLabel = R.string.statusMessage,
                    bottomLabel = R.string.showInChat,
                )
            },
            selected = false,
            onClick = {},
            modifier = Modifier
                .height(80.dp)
        )
        NavigationDrawerItem(
            label = {
                SideBarNavigationItems(
                    icon = R.drawable.notifications_24px,
                    topLabel = R.string.notifications,
                    bottomLabel = R.string.notifications_description,
                )
            },
            selected = false,
            onClick = {}
        )
        NavigationDrawerItem(
            label = {
                SideBarNavigationItems(
                    icon = R.drawable.settings_24px,
                    topLabel = R.string.settings,
                )
            },
            selected = false,
            onClick = {}
        )
        NavigationDrawerItem(
            label = {
                SideBarNavigationItems(
                    icon = R.drawable.add_24px,
                    topLabel = R.string.addAccount,
                )
            },
            selected = false,
            onClick = {}
        )
    }

}

@Composable
fun UserDetails(
    userName: String,
    secondaryText: String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
    ) {
        UserIconWithStatus(
            status = "available",
            modifier = Modifier
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
        ) {
            Text(
                text = userName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                style = AppTypography.bodyLarge
            )
            Text(
                text = secondaryText,
                style = AppTypography.labelSmall,
                fontWeight = FontWeight.Thin
            )
        }
    }
}

@Composable
fun SideBarNavigationItems(
    @DrawableRes icon : Int,
    @StringRes topLabel : Int,
    @StringRes bottomLabel : Int? = null,
    modifier: Modifier = Modifier
) {
   Row(
       verticalAlignment = Alignment.CenterVertically,
       modifier = modifier
   ) {
       Icon(
           painter = painterResource(icon),
           contentDescription = null,
           modifier = Modifier
               .size(28.dp)
       )
       Column(
           modifier = Modifier
               .padding(start = 8.dp)
       ) {
           Text(
               text = stringResource(topLabel),
               fontWeight = FontWeight.Bold,
               style = AppTypography.bodyLarge
           )
          if (bottomLabel != null) {
              Text(
                  text = stringResource(bottomLabel),
                  style = AppTypography.labelSmall,
                  fontWeight = FontWeight.Thin
              )
          }
       }
   }
}

@Composable
@Preview(showBackground = true)
fun SideNavBarItemsPreview() {
    SideBarNavigationItems(
        R.drawable.notifications_24px,
        topLabel = R.string.notifications,
        bottomLabel = R.string.notifications_description,
    )
}
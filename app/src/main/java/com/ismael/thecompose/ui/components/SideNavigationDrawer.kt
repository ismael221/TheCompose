package com.ismael.thecompose.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ismael.thecompose.ui.theme.AppTypography
import com.ismael.thecompose.R
import com.ismael.thecompose.data.model.NavigationRoutes
import com.ismael.thecompose.data.model.User
import com.ismael.thecompose.ui.screens.chat.UserIconWithStatus
import com.ismael.thecompose.ui.screens.user.UserUiState
import org.jivesoftware.smack.packet.Presence

//TODO pass the uistate in order to get the user activity
@Composable
fun SideNavBarItems(
    loggedUser: User,
    userUiState: UserUiState,
    onItemClick: (String) -> Unit = {},
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

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
                    userName = loggedUser.displayName,
                    secondaryText = "NUNES EQUIPAMENTOS ELETRICOS LTDA",
                    userStatus =  userUiState.mode.toString(),
                    userProfilePic = ImageBitmap.imageResource(R.drawable.yasmin),
                    modifier = Modifier
                )
            },
            selected = false,
            onClick = {},
            shape = ShapeDefaults.ExtraSmall
        )
        NavigationDrawerItem(
            label = {
                UserStatusItem(
                    icon = when (userUiState.mode){
                        Presence.Mode.available -> R.drawable.available
                        Presence.Mode.dnd -> R.drawable.dnd
                        Presence.Mode.away -> R.drawable.away
                        Presence.Mode.xa -> R.drawable.away
                        else -> {
                            R.drawable.offline
                        }
                    }
                        ,
                    description = when (userUiState.mode){
                        Presence.Mode.available -> R.string.available
                        Presence.Mode.dnd -> R.string.dnd
                        Presence.Mode.away -> R.string.away
                        Presence.Mode.xa -> R.string.brb
                        else -> {
                            R.string.offline
                        }
                    }
                )
            },
            selected = false,
            modifier = Modifier,
            onClick = { expanded = !expanded },
            shape = ShapeDefaults.ExtraSmall
        )
        if (expanded) {
            NavigationDrawerItem(
                label = {
                    UserStatusItem(
                        icon = R.drawable.available,
                        description = R.string.available
                    )
                },
                selected = false,
                modifier = Modifier
                    .padding(start = 16.dp),
                onClick = {
                    onItemClick("available")
                    expanded = false
                },
                shape = ShapeDefaults.ExtraSmall
            )
            NavigationDrawerItem(
                label = {
                    UserStatusItem(
                        icon = R.drawable.dnd,
                        description = R.string.dnd
                    )
                },
                selected = false,
                modifier = Modifier
                    .padding(start = 16.dp),
                onClick = {
                    onItemClick("dnd")
                    expanded = false
                },
                shape = ShapeDefaults.ExtraSmall

            )
            NavigationDrawerItem(
                label = {
                    UserStatusItem(
                        icon = R.drawable.away,
                        description = R.string.brb
                    )
                },
                selected = false,
                modifier = Modifier
                    .padding(start = 16.dp),
                onClick = {
                    onItemClick("brb")
                    expanded = false
                },
                shape = ShapeDefaults.ExtraSmall

            )
            NavigationDrawerItem(
                label = {
                    UserStatusItem(
                        icon = R.drawable.away,
                        description = R.string.away
                    )
                },
                selected = false,
                modifier = Modifier
                    .padding(start = 16.dp),
                onClick = {
                    onItemClick("away")
                    expanded = false
                },
                shape = ShapeDefaults.ExtraSmall

            )
            NavigationDrawerItem(
                label = {
                    UserStatusItem(
                        icon = R.drawable.offline,
                        description = R.string.offline
                    )
                },
                selected = false,
                modifier = Modifier
                    .padding(start = 16.dp),
                onClick = {
                    onItemClick("offline")
                    expanded = false
                },
                shape = ShapeDefaults.ExtraSmall

            )
            NavigationDrawerItem(
                label = {
                    UserStatusItem(
                        icon = R.drawable.restart_alt_24px,
                        description = R.string.reset
                    )
                },
                selected = false,
                modifier = Modifier
                    .padding(start = 16.dp),
                onClick = {},
                shape = ShapeDefaults.ExtraSmall

            )
        }
        NavigationDrawerItem(
            label = {
                SideBarNavigationItems(
                    icon = R.drawable.draft_orders_24px,
                    topLabel = userUiState.status.toString(),
                    bottomLabel = R.string.showInChat,
                )
            },
            selected = false,
            onClick = {
                navController.navigate(NavigationRoutes.STATUS)
            },
            modifier = Modifier
                .height(80.dp),
            shape = ShapeDefaults.ExtraSmall
        )
        NavigationDrawerItem(
            label = {
                SideBarNavigationItems(
                    icon = R.drawable.notifications_24px,
                    topLabel = stringResource(R.string.notifications),
                    bottomLabel = R.string.notifications_description,
                )
            },
            selected = false,
            onClick = {},
            shape = ShapeDefaults.ExtraSmall
        )
        NavigationDrawerItem(
            label = {
                SideBarNavigationItems(
                    icon = R.drawable.settings_24px,
                    topLabel = stringResource(R.string.settings),
                )
            },
            selected = false,
            onClick = {},
            shape = ShapeDefaults.ExtraSmall
        )
        NavigationDrawerItem(
            label = {
                SideBarNavigationItems(
                    icon = R.drawable.add_24px,
                    topLabel = stringResource(R.string.addAccount),
                )
            },
            selected = false,
            onClick = {},
            shape = ShapeDefaults.ExtraSmall
        )
    }

}

@Composable
fun UserDetails(
    userName: String,
    secondaryText: String? = null,
    userStatus: String,
    userProfilePic: ImageBitmap,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        UserIconWithStatus(
            status = userStatus,
            userProfile = userProfilePic,
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
            if (secondaryText != null) {
                Text(
                    text = secondaryText,
                    style = AppTypography.labelSmall,
                    fontWeight = FontWeight.Thin
                )
            }
        }
    }
}

@Composable
fun SideBarNavigationItems(
    @DrawableRes icon: Int,
    topLabel: String,
    @StringRes bottomLabel: Int? = null,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(16.dp)
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
                text = topLabel,
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
fun UserStatusItem(
    @DrawableRes icon: Int,
    @StringRes description: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = stringResource(description),
                modifier = Modifier
                    .size(26.dp)
            )
        }
        Spacer(
            modifier = Modifier
                .width(16.dp)
        )
        Text(
            text = stringResource(description),
            style = AppTypography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }

}

@Composable
@Preview(showBackground = true)
fun SideNavBarItemsPreview() {
    MaterialTheme(
        darkColorScheme()
    ) {
        //  UserStatusItem()
//        SideNavBarItems(
//            loggedUser = LocalLoggedAccounts.account
//        )
    }
}
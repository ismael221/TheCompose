package com.ismael.teams.ui.screens.user

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ismael.teams.data.remote.xmpp.XmppManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.packet.PresenceBuilder

class UserViewModel: ViewModel() {

    private val xmppManager = XmppManager

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState = _uiState.asStateFlow()


    fun updatePresence(presence: String) {
        println("Presença alterada: $presence")
        val newPresence = PresenceBuilder.buildPresence()
        when (presence) {
            "available" -> {
                _uiState.update {
                    it.copy(mode = Presence.Mode.available)
                }
                newPresence.setMode(Presence.Mode.available)
                xmppManager.setPresence(newPresence.build())
            }
            "dnd" -> {
                _uiState.update {
                    it.copy(mode = Presence.Mode.dnd)
                }
                newPresence.setMode(Presence.Mode.dnd)
                xmppManager.setPresence(newPresence.build())
            }
            "away" -> {
                _uiState.update {
                    it.copy(mode = Presence.Mode.away)
                }
                newPresence.setMode(Presence.Mode.away)
                xmppManager.setPresence(newPresence.build())
            }
            "brb" -> {
                _uiState.update {
                    it.copy(mode = Presence.Mode.xa)
                }
                newPresence.setMode(Presence.Mode.xa)
                xmppManager.setPresence(newPresence.build())
            }
            "offline" -> {
                _uiState.update {
                    it.copy(type = Presence.Type.unavailable.toString())
                }
                newPresence.ofType(Presence.Type.unavailable)
                xmppManager.setPresence(newPresence.build())
            }
        }
    }

    fun getPresence(jid: String) {
        val presence = xmppManager.getUserPresence(jid)
        Log.i("Roster", presence?.mode.toString())
        Log.i("Roster", presence?.status.toString())
        _uiState.update {
            it.copy(
                mode = presence?.mode,
                type = presence?.type.toString()
            )
        }
    }
}
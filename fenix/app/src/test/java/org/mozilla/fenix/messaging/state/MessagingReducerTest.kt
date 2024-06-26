/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.messaging.state

import io.mockk.mockk
import mozilla.components.service.nimbus.messaging.Message
import mozilla.components.service.nimbus.messaging.MessageData
import mozilla.components.service.nimbus.messaging.MessageSurfaceId
import mozilla.components.service.nimbus.messaging.StyleData
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mozilla.fenix.components.appstate.AppAction.MessagingAction.ConsumeMessageToShow
import org.mozilla.fenix.components.appstate.AppAction.MessagingAction.UpdateMessageToShow
import org.mozilla.fenix.components.appstate.AppAction.MessagingAction.UpdateMessages
import org.mozilla.fenix.components.appstate.AppState
import org.mozilla.fenix.components.appstate.AppStoreReducer
import org.mozilla.fenix.messaging.FenixMessageSurfaceId
import org.mozilla.fenix.messaging.MessagingState

class MessagingReducerTest {

    @Test
    fun `GIVEN a new value for messageToShow WHEN UpdateMessageToShow is called THEN update the current value`() {
        val initialState = AppState(
            messaging = MessagingState(
                messageToShow = mapOf(),
            ),
        )

        val m = createMessage("message1")

        var updatedState = MessagingReducer.reduce(
            initialState,
            UpdateMessageToShow(m),
        )

        assertNotNull(updatedState.messaging.messageToShow[m.surface])

        updatedState = AppStoreReducer.reduce(updatedState, ConsumeMessageToShow(m.surface))

        assertNull(updatedState.messaging.messageToShow[m.surface])
    }

    private fun createMessage(id: String, action: String = "action-1", surface: MessageSurfaceId = FenixMessageSurfaceId.HOMESCREEN): Message =
        Message(
            id = id,
            data = MessageData(surface = surface),
            action = action,
            style = StyleData(),
            triggerIfAll = listOf(),
            metadata = Message.Metadata(id = id),
        )

    @Test
    fun `GIVEN a new value for messages WHEN UpdateMessages is called THEN update the current value`() {
        val initialState = AppState(
            messaging = MessagingState(
                messages = emptyList(),
            ),
        )

        var updatedState = MessagingReducer.reduce(
            initialState,
            UpdateMessages(listOf(mockk())),
        )

        assertFalse(updatedState.messaging.messages.isEmpty())

        updatedState = AppStoreReducer.reduce(updatedState, UpdateMessages(emptyList()))

        assertTrue(updatedState.messaging.messages.isEmpty())
    }
}

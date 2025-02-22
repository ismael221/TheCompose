# TheCompose

TheCompose is a project inspired by Microsoft Teams and WhatsApp, developed using Jetpack Compose, Material3, and Kotlin. The goal is to create a team communication application with features such as sending messages, updating status, and more. The implementation of the XMPP protocol has been completed, enabling real-time messaging between users, along with additional functionalities like presence updates, file sharing, and chat states.

The idea behind TheCompose is to provide an alternative to messaging software owned by billion-dollar companies, whose data handling practices are often unclear. With TheCompose, users have full control over their data, as they can host their own server using solutions like Openfire or ejabberd.

Future updates will include advanced features such as message forwarding, replying to specific messages, location sharing, message reactions, and end-to-end encryption using OMEMO or OpenPGP.

<img alt="TheCompose Demo" height="600px" src="debug.gif"/>

## Features

- **Modern User Interface:** Built with Jetpack Compose and Material3 to deliver a smooth and responsive user experience.
- **Real-Time Messaging:** Send and receive messages in real-time using the XMPP protocol.
- **Presence and Status Updates:** Users can update their presence (e.g., "Online," "Busy") and status messages.
- **File Sharing:** Share files such as photos and documents directly within the app.
- **Chat States:** Real-time indicators like "Typing" (chatState) to enhance communication.
- **XMPP Integration:** Fully implemented XMPP protocol for real-time communication and advanced messaging features.
- **End-to-End Encryption:** Future updates will include OMEMO and OpenPGP encryption for secure communication.
- **Upcoming Features:**
    - **Message Forwarding:** Forward messages to other users or groups.
    - **Reply to Specific Messages:** Reply directly to a specific message in a conversation.
    - **Location Sharing:** Share your real-time location with other users.
    - **Message Reactions:** React to messages with emojis for quick and expressive feedback.

## Supported XEPs

TheCompose supports various XMPP Extension Protocols (XEPs) to enhance functionality:

- **XEP-0004:** Data Forms  
- **XEP-0085:** Chat State Notifications (e.g., "Typing...")  
- **XEP-0198:** Stream Management (Improved reliability and reconnection)  
- **XEP-0234:** File Transfer (For sharing files and documents)  
- **XEP-0280:** Message Carbons (Message synchronization across devices)  
- **XEP-0313:** Message Archive Management (MAM - message history storage)  
- **XEP-0333:** Message Reactions (Planned feature)  
- **XEP-0352:** Client State Indication (Power-efficient background operation)  
- **XEP-0384:** OMEMO Encryption (Planned feature for end-to-end encryption)  
- **XEP-0055:** Jabber Search

## Technologies

- **Kotlin:** A modern and efficient programming language for Android development.
- **Jetpack Compose:** A declarative UI framework for Android, focused on simplicity and performance.
- **Material3:** A modern visual style with components from the latest Material Design.
- **XMPP:** A messaging protocol for real-time communication (fully implemented).

## How to Run the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/ismael221/TheCompose.git


2. Open the project in Android Studio.

3. Connect a physical device or start an Android emulator.

4. Build and run the application on the device or emulato

## Contributions

Feel free to contribute to the project. For suggestions, improvements, or bug fixes, create an **issue** or submit a **pull request**.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

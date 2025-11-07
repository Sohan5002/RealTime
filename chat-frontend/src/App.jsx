// src/App.jsx
import React, { useState } from "react";
import { WebsocketProvider } from "./services/websocket";

// ✅ Correct imports (make sure these files exist)
import NavBar from "./features/chat/NavBar";
import ChatList from "./features/chat/Sidebar";     // replaces Sidebar
import Conversation from "./features/chat/ChatRoom"; // replaces ChatRoom

export default function App() {
  const [selectedConversation, setSelectedConversation] = useState(null);  // ...existing code...

  // ...existing code...

  return (
    <WebsocketProvider>
      <div className="h-screen flex flex-col">
        {/* Top Navigation */}
        <NavBar />

        {/* Main Layout: Sidebar + Chat Area */}
        <div className="flex flex-1">
          {/* Left Sidebar (Chat List) */}
          <ChatList onSelectConversation={setSelectedConversation} />

          {/* Right Chat Area */}
          <div className="flex-1 bg-gray-100">
            {selectedConversation ? (
              <Conversation
                conversationId={selectedConversation.id}
                currentUserId={1}
                onBack={() => setSelectedConversation(null)}
              />
            ) : (
              <div className="flex h-full items-center justify-center text-gray-400">
                Select a chat to start messaging 💬
              </div>
            )}
          </div>
        </div>
      </div>
    </WebsocketProvider>
  );
}

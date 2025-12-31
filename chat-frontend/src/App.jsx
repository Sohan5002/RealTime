import React, { useState, useEffect } from "react";
import { WebsocketProvider } from "./services/websocket";
import NavBar from "./features/chat/NavBar";
import ChatList from "./features/chat/Sidebar";
import Conversation from "./features/chat/ChatRoom";
import Login from "./features/chat/login";
import Register from "./features/chat/Register";
import api from "./services/api";

export default function App() {
  const [selectedConversation, setSelectedConversation] = useState(null);
  const [currentUser, setCurrentUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [showRegister, setShowRegister] = useState(false);

  useEffect(() => {
    // Check if user is logged in
    const token = localStorage.getItem('accessToken');
    const userId = localStorage.getItem('userId');
    
    if (token && userId) {
      setIsAuthenticated(true);
      setCurrentUser({ id: parseInt(userId) });
      
      // Fetch user details
      fetchUserDetails(parseInt(userId));
    }
  }, []);

  const fetchUserDetails = async (userId) => {
    try {
      const response = await api.get(`/api/users/${userId}`);
      setCurrentUser(response.data);
    } catch (error) {
      console.error('Error fetching user details:', error);
    }
  };

  const handleLogin = (userData) => {
    localStorage.setItem('accessToken', userData.accessToken);
    localStorage.setItem('refreshToken', userData.refreshToken);
    localStorage.setItem('userId', userData.userId);
    setIsAuthenticated(true);
    setCurrentUser({ id: userData.userId });
    setShowRegister(false);
  };

  const handleLogout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('userId');
    setIsAuthenticated(false);
    setCurrentUser(null);
    setSelectedConversation(null);
  };

  const handleSelectConversation = (conversation) => {
    setSelectedConversation(conversation);
  };

  // Show login/register forms if not authenticated
  if (!isAuthenticated) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-100">
        {showRegister ? (
          <Register
            onRegister={handleLogin}
            onSwitchToLogin={() => setShowRegister(false)}
          />
        ) : (
          <Login
            onLogin={handleLogin}
            onSwitchToRegister={() => setShowRegister(true)}
          />
        )}
      </div>
    );
  }

  return (
    <WebsocketProvider>
      <div className="h-screen flex flex-col bg-gray-50">
        {/* Top Navigation */}
        <NavBar onLogout={handleLogout} currentUser={currentUser} />

        {/* Main Layout: Sidebar + Chat Area */}
        <div className="flex flex-1 overflow-hidden">
          {/* Left Sidebar (Chat List) */}
          <ChatList 
            onSelectConversation={handleSelectConversation}
            currentUserId={currentUser?.id}
          />

          {/* Right Chat Area */}
          <div className="flex-1 flex flex-col bg-gray-100">
            {selectedConversation ? (
              <Conversation
                recipientId={selectedConversation.id}
                currentUserId={currentUser?.id}
                recipientName={selectedConversation.name || selectedConversation.username}
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

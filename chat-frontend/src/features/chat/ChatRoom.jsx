import React, { useEffect, useRef, useState } from "react";
import { useWS } from "../../services/websocket";
import api from "../../services/api";
import MessageInput from "./MessageInput";
import MessageList from "./MessageList";

export default function Conversation({ recipientId, currentUserId, recipientName }) {
  const ws = useWS();
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(true);
  const messagesEndRef = useRef(null);

  // Load existing messages from API
  useEffect(() => {
    if (!recipientId || !currentUserId) return;

    const loadMessages = async () => {
      setLoading(true);
      try {
        // Get messages between current user and recipient
        const response = await api.get(
          `/api/messages/between/${currentUserId}/${recipientId}`
        );
        const messageList = response.data.map(msg => ({
          id: msg.id,
          senderId: msg.senderId,
          recipientId: msg.recipientId,
          content: msg.content,
          createdAt: msg.createdAt,
          status: msg.status || 'sent',
        }));
        setMessages(messageList);
      } catch (err) {
        console.error("Failed to load messages:", err);
      } finally {
        setLoading(false);
      }
    };

    loadMessages();
  }, [recipientId, currentUserId]);

  // Subscribe to WebSocket for real-time messages
  useEffect(() => {
    if (!ws || !recipientId) return;

    const unsubscribe = ws.subscribe('message.receive', (payload) => {
      // Only add message if it's from this conversation
      if (payload.senderId === recipientId || payload.recipientId === recipientId) {
        setMessages((prev) => {
          // Avoid duplicates
          if (prev.some((m) => m.id === payload.id)) return prev;
          return [...prev, {
            id: payload.id,
            senderId: payload.senderId,
            recipientId: payload.recipientId,
            content: payload.content,
            createdAt: payload.timestamp || new Date().toISOString(),
            status: 'sent',
          }];
        });
      }
    });

    return unsubscribe;
  }, [ws, recipientId]);

  // Auto scroll to bottom
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  // Send message
  const handleSend = (text) => {
    if (!text.trim() || !recipientId || !currentUserId) return;

    const tempId = `temp-${Date.now()}`;
    
    // Add optimistic message
    const tempMessage = {
      tempId,
      senderId: currentUserId,
      recipientId: recipientId,
      content: text,
      createdAt: new Date().toISOString(),
      status: 'pending',
    };
    setMessages((prev) => [...prev, tempMessage]);

    // Send via WebSocket
    ws.send('message.send', {
      tempId,
      recipientId: recipientId,
      content: text,
      contentType: 'text',
    });
  };

  if (loading) {
    return (
      <div className="flex-1 flex items-center justify-center">
        <div className="text-gray-500">Loading messages...</div>
      </div>
    );
  }

  return (
    <div className="flex flex-col h-full bg-white">
      {/* Header */}
      <div className="px-4 py-3 border-b border-gray-200 bg-gray-50">
        <h2 className="text-lg font-semibold text-gray-800">
          {recipientName || `User ${recipientId}`}
        </h2>
      </div>

      {/* Message List */}
      <div className="flex-1 overflow-y-auto">
        <MessageList
          messages={messages}
          currentUserId={currentUserId}
        />
        <div ref={messagesEndRef} />
      </div>

      {/* Input */}
      <MessageInput onSend={handleSend} />
    </div>
  );
}

import React, { createContext, useContext, useEffect, useRef, useState } from "react";
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const WSContext = createContext(null);

const WS_URL = import.meta.env.VITE_WS_URL || 'http://localhost:8083/ws'; // ChatService WebSocket endpoint

export function WebsocketProvider({ children }) {
  const [connected, setConnected] = useState(false);
  const clientRef = useRef(null);
  const handlersRef = useRef({});

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      console.warn('No token found, WebSocket connection skipped');
      return;
    }

    // Create STOMP client
    const client = new Client({
      webSocketFactory: () => new SockJS(WS_URL),
      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onConnect: () => {
        console.log('WebSocket connected');
        setConnected(true);
        
        // Subscribe to message delivery topic
        client.subscribe('/topic/messages', (message) => {
          try {
            const data = JSON.parse(message.body);
            const recipientHandlers = handlersRef.current['message.receive'] || [];
            recipientHandlers.forEach(handler => handler(data));
          } catch (error) {
            console.error('Error parsing WebSocket message:', error);
          }
        });
      },
      onStompError: (frame) => {
        console.error('STOMP error:', frame);
        setConnected(false);
      },
      onWebSocketClose: () => {
        console.log('WebSocket closed');
        setConnected(false);
      },
      onDisconnect: () => {
        console.log('WebSocket disconnected');
        setConnected(false);
      },
    });

    clientRef.current = client;
    client.activate();

    return () => {
      if (clientRef.current) {
        clientRef.current.deactivate();
      }
    };
  }, []);

  const send = (type, payload) => {
    if (!clientRef.current || !connected) {
      console.warn('WebSocket not connected, message not sent');
      return;
    }

    if (type === 'message.send') {
      // Send message via STOMP
      clientRef.current.publish({
        destination: '/app/message.send',
        body: JSON.stringify({
          recipientId: payload.recipientId,
          content: payload.content,
          contentType: payload.contentType || 'text',
        }),
      });
      
      // Optimistically trigger confirmation handler
      const confirmHandlers = handlersRef.current['message.confirm'] || [];
      confirmHandlers.forEach(handler => handler({
        tempId: payload.tempId,
        ...payload,
        status: 'sent',
      }));
    } else if (type === 'typing') {
      // Optional: Send typing indicator
      clientRef.current.publish({
        destination: '/app/typing',
        body: JSON.stringify(payload),
      });
    }
  };

  const subscribe = (eventType, handler) => {
    if (!handlersRef.current[eventType]) {
      handlersRef.current[eventType] = [];
    }
    handlersRef.current[eventType].push(handler);

    // Return unsubscribe function
    return () => {
      handlersRef.current[eventType] = handlersRef.current[eventType].filter(
        h => h !== handler
      );
    };
  };

  const value = {
    send,
    subscribe,
    connected,
  };

  return React.createElement(WSContext.Provider, { value }, children);
}

export const useWS = () => {
  const ctx = useContext(WSContext);
  if (!ctx) {
    throw new Error('useWS must be used within WebsocketProvider');
  }
  return ctx;
};

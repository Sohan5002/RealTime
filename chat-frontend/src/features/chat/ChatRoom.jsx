import React, { useEffect, useState } from "react";
import MessageInput from "./MessageInput";
import MessageList from "./MessageList";
import { useWS } from "../../services/websocket";   

export default function ChatRoom({ convId }) {
  const [messages, setMessages] = useState([]);
  const ws = useWS();

  // Listen to message events
  useEffect(() => {
    if (!ws) return;

    const unsubConfirm = ws.subscribe("message.confirm", (payload) => {
      setMessages((prev) =>
        prev.map((m) =>
          m.tempId === payload.tempId ? { ...payload, status: "sent" } : m
        )
      );
    });

    const unsubReceive = ws.subscribe("message.receive", (payload) => {
      setMessages((prev) => {
        if (prev.some((m) => m.id === payload.id)) return prev;
        return [...prev, { ...payload, status: "sent" }];
      });
    });

    return () => {
      unsubConfirm();
      unsubReceive();
    };
  }, [ws]);

  const handleSend = (text) => {
    if (!ws) {
      console.warn("WebSocket not ready — cannot send message");
      return;
    }
    if (!convId) {
      console.warn("No active conversation selected — cannot send message");
      return;
    }

    const tempId = "t-" + Date.now();
    const tempMsg = {
      tempId,
      senderId: 1,
      content: text,
      createdAt: new Date().toISOString(),
      status: "pending",
    };
    setMessages((prev) => [...prev, tempMsg]);
    ws.send("message.send", { tempId, convId, content: text, senderId: 1 });
  };

  return (
    <div className="chat">
      <MessageList messages={messages} />
      <div className="input-bar">
        <MessageInput onSend={handleSend} />
      </div>
    </div>
  );
}

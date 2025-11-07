import React, { useEffect, useRef } from "react";

function MessageList({ messages }) {
  const ref = useRef();

  useEffect(() => {
    if (ref.current) ref.current.scrollTop = ref.current.scrollHeight;
  }, [messages]);

  return (
    <div ref={ref} className="message-list">
      {messages.map((m) => (
        <div
          key={m.id || m.tempId}
          className={`message ${m.senderId === 1 ? "me" : "other"}`}
        >
          {m.content}
          <div
            style={{
              fontSize: 12,
              color: "#666",
              marginTop: 4,
              textAlign: "right",
            }}
          >
            {m.status === "pending"
              ? "Sending..."
              : new Date(m.createdAt).toLocaleTimeString()}
          </div>
        </div>
      ))}
    </div>
  );
}

export default MessageList;

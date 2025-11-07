import React from "react";

export default function Sidebar({ onSelectConv, activeConv }) {
  const conversations = [
    { id: "general", name: "General Chat" },
    { id: "project", name: "Project Group" },
    { id: "support", name: "Support Team" }
  ];

  return (
    <div className="sidebar">
      <h3 style={{ marginTop: 0 }}>Chats</h3>
      {conversations.map((c) => (
        <div
          key={c.id}
          onClick={() => onSelectConv(c.id)}
          style={{
            padding: "10px",
            borderRadius: "8px",
            background: activeConv === c.id ? "#e6f0ff" : "transparent",
            cursor: "pointer",
            marginBottom: "6px"
          }}
        >
          {c.name}
        </div>
      ))}
    </div>
  );
}

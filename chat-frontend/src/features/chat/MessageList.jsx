import React from "react";

function MessageList({ messages, currentUserId }) {
  return (
    <div className="flex flex-col gap-3 p-4">
      {messages.length === 0 ? (
        <div className="text-center text-gray-400 py-8">
          No messages yet. Start the conversation!
        </div>
      ) : (
        messages.map((m) => {
          const isOwn = m.senderId === currentUserId;
          return (
            <div
              key={m.id || m.tempId}
              className={`flex ${isOwn ? "justify-end" : "justify-start"}`}
            >
              <div
                className={`max-w-[70%] px-4 py-2 rounded-lg ${
                  isOwn
                    ? "bg-blue-500 text-white rounded-br-none"
                    : "bg-gray-200 text-gray-800 rounded-bl-none"
                }`}
              >
                <p className="text-sm break-words">{m.content}</p>
                <div
                  className={`text-xs mt-1 ${
                    isOwn ? "text-blue-100" : "text-gray-500"
                  }`}
                >
                  {m.status === "pending" ? (
                    <span>Sending...</span>
                  ) : m.createdAt ? (
                    new Date(m.createdAt).toLocaleTimeString([], {
                      hour: "2-digit",
                      minute: "2-digit",
                    })
                  ) : (
                    ""
                  )}
                </div>
              </div>
            </div>
          );
        })
      )}
    </div>
  );
}

export default MessageList;

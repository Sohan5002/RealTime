import React, { useState } from "react";

const MessageInput = ({ onSend }) => {
  const [text, setText] = useState("");

  const handleSend = () => {
    if (!text.trim()) return;
    onSend(text.trim());
    setText("");
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  return (
    <div className="flex items-center gap-2 p-4 bg-white border-t border-gray-200">
      <textarea
        rows={1}
        value={text}
        onChange={(e) => setText(e.target.value)}
        onKeyDown={handleKeyDown}
        placeholder="Type a message..."
        className="flex-1 resize-none px-4 py-2 rounded-lg border border-gray-300 outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500 text-sm"
        style={{ minHeight: "40px", maxHeight: "120px" }}
      />
      <button
        onClick={handleSend}
        className="bg-blue-500 text-white px-6 py-2 rounded-lg hover:bg-blue-600 transition font-medium text-sm"
      >
        Send
      </button>
    </div>
  );
};

export default MessageInput;

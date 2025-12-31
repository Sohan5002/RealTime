import React, { useState, useEffect } from "react";
import api from "../../services/api";

export default function ChatList({ onSelectConversation, currentUserId }) {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [activeUserId, setActiveUserId] = useState(null);

  // Fetch all users (excluding current user)
  useEffect(() => {
    const fetchUsers = async () => {
      setLoading(true);
      setError(null);
      try {
        const response = await api.get("/api/users");
        // Filter out current user
        const otherUsers = response.data.filter(user => user.id !== currentUserId);
        setUsers(otherUsers);
      } catch (err) {
        console.error("Error fetching users:", err);
        setError("Failed to load users");
      } finally {
        setLoading(false);
      }
    };

    if (currentUserId) {
      fetchUsers();
    }
  }, [currentUserId]);

  const handleSelect = (user) => {
    setActiveUserId(user.id);
    if (onSelectConversation) {
      onSelectConversation({
        id: user.id,
        name: user.username || user.email,
        username: user.username,
      });
    }
  };

  return (
    <div className="h-full w-80 bg-white border-r border-gray-200 flex flex-col">
      {/* Header */}
      <div className="px-4 py-3 border-b border-gray-200 bg-gray-50">
        <h3 className="text-lg font-semibold text-gray-800">Chats</h3>
      </div>

      {/* Loader / Error */}
      {loading && (
        <div className="p-4 text-gray-500 text-sm">Loading users...</div>
      )}
      {error && (
        <div className="p-4 text-red-500 text-sm">{error}</div>
      )}

      {/* User List */}
      <div className="overflow-y-auto flex-1">
        {users.length === 0 && !loading ? (
          <p className="p-4 text-gray-400 text-sm text-center">
            No other users found.
          </p>
        ) : (
          users.map((user) => (
            <div
              key={user.id}
              onClick={() => handleSelect(user)}
              className={`p-3 cursor-pointer hover:bg-gray-100 flex items-center gap-3 border-b border-gray-100 transition ${
                activeUserId === user.id ? "bg-blue-50 border-l-4 border-l-blue-500" : ""
              }`}
            >
              {/* Avatar */}
              <div className="w-10 h-10 rounded-full bg-blue-500 flex items-center justify-center text-white font-semibold">
                {(user.username || user.email || 'U').charAt(0).toUpperCase()}
              </div>

              {/* User Info */}
              <div className="flex flex-col flex-1 min-w-0">
                <h4 className="font-medium text-sm text-gray-800 truncate">
                  {user.username || user.email || `User ${user.id}`}
                </h4>
                {user.email && user.username && (
                  <p className="text-xs text-gray-500 truncate">
                    {user.email}
                  </p>
                )}
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}

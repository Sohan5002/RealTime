import React, { useState } from "react";

const NavBar = ({ onLogout, currentUser }) => {
  return (
    <nav className="bg-white border-b border-gray-200 px-4 py-3 shadow-sm">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <h1 className="text-xl font-bold text-gray-800">ChitChat</h1>
        </div>
        
        <div className="flex items-center gap-4">
          {currentUser && (
            <span className="text-sm text-gray-600">
              User ID: {currentUser.id}
            </span>
          )}
          <button
            onClick={onLogout}
            className="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition text-sm font-medium"
          >
            Logout
          </button>
        </div>
      </div>
    </nav>
  );
};

export default NavBar;

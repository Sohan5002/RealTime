import React, { createContext, useContext, useEffect, useRef } from "react";

const WSContext = createContext(null);

export function WebsocketProvider({ children }) {
  const handlers = useRef({});

  // Mock WebSocket simulation
  useEffect(() => {
    console.log("Mock WebSocket connected");
  }, []);

  const send = (type, payload) => {
    if (type === "message.send") {
      const { tempId, convId, content, senderId } = payload;
      setTimeout(() => {
        const confirm = {
          type: "message.confirm",
          payload: {
            tempId,
            id: "m-" + Date.now(),
            convId,
            content,
            senderId,
            createdAt: new Date().toISOString(),
          },
        };
        (handlers.current["message.confirm"] || []).forEach((h) =>
          h(confirm.payload)
        );
        (handlers.current["message.receive"] || []).forEach((h) =>
          h(confirm.payload)
        );
      }, 800);
    }
  };

  const subscribe = (type, fn) => {
    handlers.current[type] = handlers.current[type] || [];
    handlers.current[type].push(fn);
    return () => {
      handlers.current[type] = handlers.current[type].filter((x) => x !== fn);
    };
  };

  // Use createElement to avoid JSX in a .js file
  return React.createElement(WSContext.Provider, { value: { send, subscribe } }, children);
}

export const useWS = () => {
  const ctx = useContext(WSContext);
  if (!ctx) {
    throw new Error("useWS must be used within a WebsocketProvider");
  }
  return ctx;
};

# Chat Frontend

React-based frontend for the Real-Time Chat Application.

## Setup

1. Install dependencies:
```bash
npm install
```

2. Configure environment variables:
   - Copy `.env.example` to `.env` (if it exists)
   - Or create `.env` with:
   ```
   VITE_API_BASE_URL=http://localhost:8082
   VITE_WS_URL=http://localhost:8083/ws
   ```

3. Start development server:
```bash
npm run dev
```

## Environment Variables

- `VITE_API_BASE_URL`: API Gateway URL for REST API calls (default: http://localhost:8082)
- `VITE_WS_URL`: WebSocket URL for real-time messaging (default: http://localhost:8083/ws)

## Features

- JWT-based authentication
- Real-time messaging via STOMP WebSocket
- User list and conversation management
- Responsive UI with Tailwind CSS

## Build

```bash
npm run build
```

## Project Structure

```
src/
  ├── features/
  │   └── chat/
  │       ├── ChatRoom.jsx      # Conversation view
  │       ├── login.jsx         # Login form
  │       ├── Register.jsx      # Registration form
  │       ├── MessageInput.jsx  # Message input component
  │       ├── MessageList.jsx   # Message list component
  │       ├── NavBar.jsx        # Navigation bar
  │       └── Sidebar.jsx       # User list sidebar
  ├── services/
  │   ├── api.js               # Axios API client
  │   └── websocket.js         # STOMP WebSocket client
  ├── App.jsx                  # Main app component
  └── main.jsx                 # Entry point
```

import React, { createContext, useContext, useEffect, useRef } from 'react'

const WSContext = createContext(null)

export function WebsocketProvider({ children }){
  const handlers = useRef({}) // eventType => [fn]
  const socketRef = useRef(null)

  // MOCK: simulate server responses
  useEffect(()=>{
    // in real: socketRef.current = new WebSocket("wss://....?token=xxx")
    // we'll simulate confirm after 800ms for message.send
    socketRef.current = {
      send: (payloadStr) => {
        const payload = JSON.parse(payloadStr)
        if(payload.type === 'message.send'){
          const { tempId, convId, content, senderId } = payload.payload
          setTimeout(()=>{
            // dispatch message.confirm
            const confirm = {
              type: 'message.confirm',
              payload: {
                tempId,
                id: 'm-' + Date.now(),
                convId,
                content,
                senderId,
                createdAt: new Date().toISOString()
              }
            }
            // call handlers
            (handlers.current['message.confirm']||[]).forEach(h=>h(confirm.payload))
            // also simulate deliver to other user
            (handlers.current['message.receive']||[]).forEach(h=>h(confirm.payload))
          }, 800)
        }
      }
    }
  },[])

  const send = (type, payload) => {
    const msg = JSON.stringify({type, payload})
    socketRef.current?.send(msg)
  }

  const subscribe = (type, fn) => {
    handlers.current[type] = handlers.current[type] || []
    handlers.current[type].push(fn)
    return () => {
      handlers.current[type] = handlers.current[type].filter(x=>x!==fn)
    }
  }

  return <WSContext.Provider value={{ send, subscribe }}>{children}</WSContext.Provider>
}

export const useWS = () => useContext(WSContext)

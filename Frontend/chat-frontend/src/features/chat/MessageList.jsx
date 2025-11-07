import React, { useRef, useEffect } from 'react'
import './message.css'

export default function MessageList({ messages=[] }){
  const ref = useRef()
  useEffect(()=> {
    // auto-scroll to bottom on new msg
    if(ref.current) ref.current.scrollTop = ref.current.scrollHeight
  }, [messages.length])
  return (
    <div ref={ref} className="message-list" role="log" aria-live="polite">
      {messages.map(m => (
        <div key={m.id || m.tempId} className={`message ${m.senderId===1 ? 'me' : 'other'}`}>
          <div>{m.content}</div>
          <div style={{fontSize:12,color:'#666',marginTop:6}}>
            {m.status === 'pending' ? 'Sending...' : new Date(m.createdAt).toLocaleTimeString()}
          </div>
        </div>
      ))}
    </div>
  )
}

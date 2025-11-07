import React, { useEffect, useState } from 'react'
import MessageList from './MessageList'
import MessageInput from './MessageInput'
import { useWS } from '../../services/websocket'

export default function ChatRoom({ convId }){
  const [messages, setMessages] = useState([])
  const ws = useWS()

  useEffect(()=> {
    if(!ws) return
    // subscribe confirms
    const unsubConfirm = ws.subscribe('message.confirm', (payload)=>{
      setMessages(prev => prev.map(m => m.tempId === payload.tempId ? {...payload, status:'sent'} : m))
    })
    const unsubReceive = ws.subscribe('message.receive', (payload)=>{
      // if message from other user, append
      setMessages(prev => {
        // prevent duplicates
        if(prev.some(m => m.id === payload.id)) return prev
        return [...prev, {...payload, status:'sent'}]
      })
    })
    return ()=> { unsubConfirm(); unsubReceive() }
  }, [ws])

  // load initial (mock)
  useEffect(()=>{
    setMessages([]) // clear when conv changes
    if(!convId) return
    setTimeout(()=> setMessages([
      {id:'m1', senderId:2, content:'Hi!', createdAt:new Date().toISOString(), status:'sent'},
      {id:'m2', senderId:1, content:'Hello, ready to test?', createdAt:new Date().toISOString(), status:'sent'}
    ]),200)
  },[convId])

  const handleSend = (content) => {
    const tempId = 't-'+Date.now()
    const tempMsg = { tempId, senderId:1, content, createdAt:new Date().toISOString(), status:'pending' }
    setMessages(prev=>[...prev, tempMsg])
    ws.send('message.send', { tempId, convId, content, senderId:1 })
  }

  if(!convId) return <div className="chat"><div style={{padding:20}}>Select conversation</div></div>

  return (
    <div className="chat">
      <MessageList messages={messages} />
      <div className="input-bar">
        <MessageInput onSend={handleSend} />
      </div>
    </div>
  )
}

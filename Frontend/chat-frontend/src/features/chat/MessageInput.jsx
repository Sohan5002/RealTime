import React, { useState } from 'react'

export default function MessageInput({ onSend }){
  const [text, setText] = useState('')
  const send = () => {
    if(!text.trim()) return
    onSend(text.trim())
    setText('')
  }
  const onKeyDown = (e) => {
    if(e.key === 'Enter' && !e.shiftKey){
      e.preventDefault(); send()
    }
  }
  return (
    <>
      <textarea rows={1} value={text} onKeyDown={onKeyDown} onChange={e=>setText(e.target.value)} placeholder="Type a message..." />
      <button className="send-btn" onClick={send}>Send</button>
    </>
  )
}

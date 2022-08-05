import './App.css';
import Desk from './model/Desk'
import Auth from './auth/Auth'
import axios from 'axios';
import {AuthContext} from './context/authContext'
import { useEffect, useState } from 'react';
import {over} from 'stompjs'
import SockJS from 'sockjs-client';
import getGame from './service/GameService'
import { getUser } from './service/UserService';
import GameUserData from './elements/GameUserData'
import Actions from './actions/Actions'

function App() {
  const [isAuth, setIsAuth] = useState(localStorage.getItem("isAuth") === "true")
  const [accessHeader, setAccessHeader] = useState(localStorage.getItem("access_header"))
  const [refreshHeader, setRefreshHeader] = useState(localStorage.getItem("refresh_header"))
  const [username, setUsername] = useState(localStorage.getItem("username"))
  const [showLogin, setShowLogin] = useState(false)
  const [showRegister, setShowRegister] = useState(false)
  localStorage.setItem("server-url", process.env.REACT_APP_BACKEND_URL)
  console.log(process.env.REACT_APP_BACKEND_URL)
  const [user, setUser] = useState({})
  let stompClient = null
  useEffect(() => {
    if(isAuth){
      getUser().then(
        res => {
          setUser(res.data)
          if(!stompClient){
          let Sock = new SockJS(localStorage.getItem('server-url') + '/stomp-endpoint')
          stompClient = over(Sock)
          const headers = {
            "Authorization": localStorage.getItem("access_header")

          }
          stompClient.connect({headers: headers}, (frame) => {
            stompClient.subscribe('/usrs/' + res.data.username, onMsg, onErr)
          }, () => console.log('error on stomp connect'))
        }
        }
      )
    }
  }, [])

  function onMsg(usr){
    console.log('stomp msg')
    setUser(JSON.parse(usr.body))
  }
  function onErr(error){
    console.log(error)
  }
  return (
    <AuthContext.Provider value={{
      isAuth, setIsAuth, 
      accessHeader, setAccessHeader,
      refreshHeader, setRefreshHeader,
      username, setUsername
    }}>
    <div className="header">
       <Auth showLogin={showLogin} setShowLogin={setShowLogin} showRegister={showRegister} setShowRegister={setShowRegister} user={user} setUser={setUser}/>        
    </div>
    <div className="container">
      <div className="chess">
        <GameUserData user={user} isWhite={false}/>
        <Desk user={user} setUser={setUser}/>
        <GameUserData user={user} isWhite={true}/>
      </div>
      <Actions showLogin={showLogin} setShowLogin={setShowLogin} showRegister={showRegister} setShowRegister={setShowRegister} user={user} setUser={setUser}/>
    </div>    
    </AuthContext.Provider>
    );

}


export default App;

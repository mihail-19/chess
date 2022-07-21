import logo from './logo.svg';
import './App.css';
import Field from './model/Field'
import Desk from './model/Desk'
import Auth from './service/Auth'
import axios from 'axios';
import {AuthContext} from './context/authContext'
import { useState } from 'react';
import Login from './auth/Login';
import Register from './auth/Register';
function App() {
  const [isAuth, setIsAuth] = useState(localStorage.getItem("isAuth") == "true")
  const [accessHeader, setAccessHeader] = useState(localStorage.getItem("access_header"))
  const [refreshHeader, setRefreshHeader] = useState(localStorage.getItem("refresh_header"))
  const [username, setUsername] = useState(localStorage.getItem("username"))
  const [showLogin, setShowLogin] = useState(false)
  const [showRegister, setShowRegister] = useState(false)
  localStorage.setItem("server-url", "http://178.151.21.70")
  return (
    <AuthContext.Provider value={{
      isAuth, setIsAuth, 
      accessHeader, setAccessHeader,
      refreshHeader, setRefreshHeader,
      username, setUsername
    }}>
    <div class="header">
       <Auth showLogin={showLogin} setShowLogin={setShowLogin} showRegister={showRegister} setShowRegister={setShowRegister}/>        
    </div>
    <div className="container">
      <div className="chess">
        <Desk />
      </div>
      <div className="actions">
        <Login showLogin={showLogin} setShowLogin={setShowLogin}/>
        <Register showRegister={showRegister} setShowRegister={setShowRegister}/>
      </div>
    </div>    
    </AuthContext.Provider>
    );
}

async function storage(){
  console.log('storage')
  const response = await axios.get("https://jsonplaceholder.typicode.com/posts")
  console.log(response.data)
}
export default App;

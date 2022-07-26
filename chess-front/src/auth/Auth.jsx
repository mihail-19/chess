import axios from "axios"
import { useContext } from "react"
import './Auth.css'
import { AuthContext } from "../context/authContext"


const Auth  = ({showLogin, setShowLogin, showRegister, setShowRegister,user, setUser}) =>{
    const {isAuth, setIsAuth, 
        accessHeader, setAccessHeader,
        refreshHeader, setRefreshHeader,
        username, setUsername} = useContext(AuthContext)
    if(isAuth && user){
        return (
            <div className="auth">
                <div className="auth__username">{user.username}</div>
                <button className="auth__button auth__logout-button" onClick={() => logout()}>logout</button>
            </div>
            
        )
    } else {
        return (
            <div className="auth">
                <button className="auth__button auth__login-button" onClick={() => login()}>Sign in</button>
                <button className="auth__button auth__register-button" onClick={() => register()}>Register</button>
            </div>
        )
    }

    async function getUsers(){
        const headers = {
            "Authorization": accessHeader
        }
        const res = await axios.get(localStorage.getItem("server-url") + "/users", {headers: headers})
        
        console.log(res.data)
    }
    function login(){
        if(showLogin){
            setShowLogin(false)
        } else {
            setShowLogin(true)
        }
        setShowRegister(false)
    }
    function register(){
        if(showRegister){
            setShowRegister(false)
        } else {
            setShowRegister(true)
        }
        setShowLogin(false)
        
    }
    function logout(){
        setIsAuth(false);
        setAccessHeader('')
        setRefreshHeader('')
        setUsername('')
        localStorage.clear()
        setUser(null)
    }

}

export default Auth
import axios from "axios"
import { useContext } from "react"
import { AuthContext } from "../context/authContext"


const Auth  = ({showLogin, setShowLogin, showRegister, setShowRegister}) =>{
    const AUTH_PATH = "http://178.151.21.70/chess/login";
    const REGISTER_PATH = "http://178.151.21.70/chess/users/add"
    const {isAuth, setIsAuth, 
        accessHeader, setAccessHeader,
        refreshHeader, setRefreshHeader,
        username, setUsername} = useContext(AuthContext)
    if(isAuth){
        return (
            <div className="auth">
                <div className="username">{username}</div>
                <button onClick={() => logout()}>logout</button>
                <button onClick={() => getUsers()}>get users</button>
            </div>
            
        )
    } else {
        return (
            <div className="auth">
                <button onClick={() => login()}>Sign in</button>
                <button onClick={() => register()}>Register</button>
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
        setShowLogin(true)
        setShowRegister(false)
    }
    function register(){
        setShowLogin(false)
        setShowRegister(true)
    }
    function logout(){
        setIsAuth(false);
        setAccessHeader('')
        setRefreshHeader('')
        setUsername('')
        localStorage.clear()
    }

}

export default Auth
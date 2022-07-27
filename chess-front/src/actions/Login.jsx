import axios from "axios"
import { useState, useContext } from "react"
import './Actions.css'
import {AuthContext} from '../context/authContext'
import { getUser } from "../service/UserService"
const Login = ({showLogin, setShowLogin, setUser}) => {
    const {isAuth, setIsAuth, 
        accessHeader, setAccessHeader,
        refreshHeader, setRefreshHeader,
        username, setUsername} = useContext(AuthContext)
    const [name, setName] = useState('')
    const [password, setPassword] = useState('')
    if(showLogin){
        return (
            <div className="actions__form">
                <div className="actions__form-line">
                    <label >Username</label> <input type="text" value={name} onChange={e => setName(e.target.value)}></input>
                </div>
                <div className="actions__form-line">
                    <label>Password</label> <input type="text" value={password} onChange={e => setPassword(e.target.value)}></input>
                </div>
                <div className="actions__form-line">
                    <button className="actions__button" onClick={() => loginFunc(name, password)}>Login</button>
                </div>
            </div>
        )
    }

    async function loginFunc(name, password){
        console.log('login: ' + name + ' ' + password)
        const url = localStorage.getItem("server-url") + "/login"
        const data = new FormData()
        data.append('username', name)
        data.append('password', password)
        let res = await axios.post(url, data)
        console.log(res.data.access_token)
        const access_header = "Bearer " + res.data.access_token
        const refresh_header = "Bearer " + res.data.refresh_token
        setAccessHeader(access_header)
        setRefreshHeader(refresh_header)
        setIsAuth(true)
        setUsername(name)
        localStorage.setItem("username", name)
        localStorage.setItem("access_header", access_header)
        localStorage.setItem("refresh_header", refresh_header)
        localStorage.setItem("isAuth", "true")
        getUser().then(res => {
            setUser(res.data)
            setShowLogin(false)
        })
    }
}

export default Login
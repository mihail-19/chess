import axios from "axios"
import { useState, useContext } from "react"
import './Actions.css'
import {AuthContext} from '../context/authContext'
import { registerUser, getUser } from "../service/UserService"
import { useEffect } from "react"
const Register = ({showRegister, setShowRegister, user, setUser}) => {
    const {isAuth, setIsAuth, 
        accessHeader, setAccessHeader,
        refreshHeader, setRefreshHeader,
        username, setUsername} = useContext(AuthContext)
    const [name, setName] = useState('')
    const [password, setPassword] = useState('')
    if(showRegister && !isAuth){
        return (
            <div className="actions__form">
                <div className="actions__form-line">
                 <label>Username</label> <input type="text" value={name} onChange={e => setName(e.target.value)}></input>
                </div>
                <div className="actions__form-line">
                 <label>Password</label> <input type="text" value={password} onChange={e => setPassword(e.target.value)}></input>
                </div>
                <div className="actions__form-line">
                 <button className="actions__button" onClick={() => registerFunc(name, password)}>Register</button>
                </div>
            </div>
        )
    }

    async function registerFunc(name, password){
        registerUser(name, password)
            .then(res =>  loginFunc(name, password)) 
            .catch(err => console.log(err))       
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
        getUser()
            .then(res => setUser(res.data))
    }
}

export default Register
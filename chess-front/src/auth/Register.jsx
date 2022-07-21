import axios from "axios"
import { useState, useContext } from "react"
import {AuthContext} from '../context/authContext'
const Register = ({showRegister, setShowRegister}) => {
    const {isAuth, setIsAuth, 
        accessHeader, setAccessHeader,
        refreshHeader, setRefreshHeader,
        username, setUsername} = useContext(AuthContext)
    const [name, setName] = useState('')
    const [password, setPassword] = useState('')
    if(showRegister){
        return (
            <div className="auth__form">
                Username <input type="text" value={name} onChange={e => setName(e.target.value)}></input>
                Password <input type="text" value={password} onChange={e => setPassword(e.target.value)}></input>
                <button onClick={() => registerFunc(name, password)}></button>
            </div>
        )
    }

    async function registerFunc(name, password){
        const url = localStorage.getItem("server-url") + "/users/add"
        const data = {
            username: name,
            password: password
        }
        let res = await axios.post(url, data)
        loginFunc(name, password)        
    }

    async function loginFunc(name, password){
        console.log('login: ' + name + ' ' + password)
        const url = localStorage.getItem("server-url") + "/login"
        const data = new FormData()
        data.append('username', name)
        data.append('password', password)
        let res = await axios.post(url, data)
        console.log(res.data.access_token)
        setAccessHeader("Bearer " + res.data.access_token)
        setRefreshHeader("Bearer " + res.data.refresh_token)
        setIsAuth(true)
        setUsername(name)

    }
}

export default Register
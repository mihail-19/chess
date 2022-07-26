import axios from 'axios'
 function getUser(){
    console.log('getting user')
    const headers = {
        "Authorization": localStorage.getItem("access_header")
    }
    let url = localStorage.getItem("server-url") + "/users/current"
    const res = axios.get(url, {headers: headers})
    console.log(res.data)
    return res
}

function registerUser(name, password){
    const url = localStorage.getItem("server-url") + "/users/add"
    const data = {
        username: name,
        password: password
    }
    let res = axios.post(url, data)
    return res
}
export {getUser, registerUser}
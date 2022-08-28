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
function searchUser(namePart){
    const url  = localStorage.getItem("server-url") + "/users/search"
    const headers = {
        "Authorization": localStorage.getItem("access_header")
    }
    const formData = new FormData()
    formData.append('namePart', namePart)
    let res = axios.post(url, formData, {headers: headers})
    return res
}
export {getUser, registerUser, searchUser}
import axios from 'axios'
async function getGame(){
    console.log('getting game for user')
    const headers = {
        "Authorization": localStorage.getItem("access_header")
    }
    let url = localStorage.getItem("server-url") + "/actions/game/for-user"
    const res = await axios.get(url, {headers: headers})
    
    console.log(res.data)
}

function gameMove(from, to){
    console.log('move')
    const headers = {
        "Authorization": localStorage.getItem("access_header")
    }
    const url = localStorage.getItem("server-url") + "/actions/game/move"
    let move = {
        from: from,
        to: to
    }
    const res = axios.post(url, move, {headers: headers})
    
    return res    
}

function gameStart(){
    console.log('game start')
    const headers = {
        "Authorization": localStorage.getItem("access_header")
    }
    const url = localStorage.getItem("server-url") + "/actions/game/start"
    const res = axios.get(url, {headers: headers})
    
    return res    
}

function gameStop(){
    console.log('game stop')
    const headers = {
        "Authorization": localStorage.getItem("access_header")
    }
    const url = localStorage.getItem("server-url") + "/actions/game/stop"
    const res = axios.get(url, {headers: headers})
    
    return res    
}
function offerGameStop(isDraw){
    const headers = {
        "Authorization": localStorage.getItem("access_header")
    }
    const fd = new FormData();
    fd.append('isDraw', isDraw)
    const url = localStorage.getItem("server-url") + "/actions/game/stop-offer"
    const res = axios.post(url, fd, {headers: headers})
    
    return res
}

function acceptGameStop(){
    const headers = {
        "Authorization": localStorage.getItem("access_header")
    }
    const url = localStorage.getItem("server-url") + "/actions/game/accept-stop"
    const res = axios.get(url, {headers: headers})
    
    return res
}
export {getGame, gameMove, gameStart, gameStop, offerGameStop, acceptGameStop}
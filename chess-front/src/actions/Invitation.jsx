import axios from "axios"
import { useEffect } from "react"
import { useState, useContext } from "react"
import './Actions.css'
import { AuthContext } from "../context/authContext"

const Invitation = ({user}) => {
    const {isAuth, setIsAuth, 
        accessHeader, setAccessHeader,
        refreshHeader, setRefreshHeader,
        username, setUsername} = useContext(AuthContext)
        const [name, setName] = useState('')
        const [invs, setInvs] = useState([])
        useEffect(() => {
            console.log(user)
            if(isAuth && user && user.invitations)
                setInvs(user.invitations)
            else {
                setInvs([])
            }
        }, [user])
        if(isAuth && user && (!user.game || user.game.isFinished)){
            return (
                <div className="invitations">
                    <div className="actions__form">
                        <div className="actions__form-line">
                            <label>Invitation:</label> 
                            <input type="text" value={name} onChange={e => setName(e.target.value)}></input>
                        </div>
                        <div className="actions__form-line">
                            <button className="actions__button" onClick={() => sendInvitation(name)}>Send</button>
                        </div>
                    </div>
                    {receivedInitations()}
                    {sendedInvitations()}
                </div>
            )
        }

        function receivedInitations(){
            const invsFiltered = invs.filter(i => i.recepient.username === user.username)
            if(invsFiltered && invsFiltered.length > 0){
                return (
                    <div className="invitations__list">
                     <h3>Invitations received</h3>
                        {invsFiltered.map(i =>
                         <div> {i.sender.username}
                            <button className="actions__button" onClick={() => denyInvitation(i)}>deny</button>
                            <button className="actions__button" onClick={() => acceptInvitation(i)}>accept</button>
                        </div>)}
                    </div>
                )
            }
        }

        function sendedInvitations(){
            const invsFiltered = invs.filter(i => i.sender.username === user.username)
            if(invsFiltered && invsFiltered.length > 0){
                return (
                    <div className="invitations__list">
                        <h3>Invitations sent</h3>
                        {invsFiltered.map(i =>
                             <div>{i.recepient.username}
                            </div>)}
                    </div>
                )
            }
        }

        async function sendInvitation(username){
            const headers = {
                "Authorization": accessHeader
            }
            let data = new FormData()
            data.append("recepient", name)
            const res = await axios.post(localStorage.getItem("server-url") + "/actions/invitation/send", data, {headers: headers})
        }
        async function getInvitations(){
            const headers = {
                "Authorization": accessHeader
            }
            const res = await axios.get(localStorage.getItem("server-url") + "/actions/invitation/for-user", {headers: headers})
            setInvs( res.data )
            console.log(res.data)   
        }
        async function acceptInvitation(inv){
            const headers = {
                "Authorization": accessHeader
            }
            let url = localStorage.getItem("server-url") + "/actions/invitation/" + inv.id + "/accept"
            const res = await axios.post(url, {}, {headers: headers})
            console.log(res.data)
        }
        async function denyInvitation(inv){
            const headers = {
                "Authorization": accessHeader
            }
            console.log(inv.id == 1)
            console.log(headers)
            let url = localStorage.getItem("server-url") + "/actions/invitation/" + inv.id + "/deny"
            const res = await axios.post(url, {}, {headers: headers})
            console.log(res.data)
        }
}

export default Invitation
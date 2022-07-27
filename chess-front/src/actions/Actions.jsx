import './Actions.css'
import Login from './Login';
import Register from './Register';
import Invitation from './Invitation'
import GameActions from './GameActions';


const Actions = ({showLogin, setShowLogin, showRegister, setShowRegister, user, setUser}) => {
    return (
        <div className="actions">
            <Login showLogin={showLogin} setShowLogin={setShowLogin} setUser={setUser}/>
            <Register showRegister={showRegister} setShowRegister={setShowRegister} user={user} setUser={setUser}/>
            <Invitation user={user}/>
            <GameActions user={user} setUser={setUser}/>
        </div>
    )
}
export default Actions
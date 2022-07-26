import { gameStart, gameStop } from "../service/GameService"
import { getUser } from "../service/UserService"

const GameActions = ({user, setUser}) =>{
    if(user && user.game ){
        if(!user.game.isStarted && user.username === user.game.creator.username){
            return <button onClick={() => start()}>start</button>
        } else if(user.game.isStarted && !user.game.isFinished){
            return <button onClick={() => stop()}>stop</button>
        }
    }

    function start(){
        console.log('starting game')
        gameStart()
            .then(g => {
                getUser()
                    .then(res => setUser(res.data))
            })
    }

    function stop(){
        console.log('stopping game')
        gameStop().then(undefined)
    }
}

export default GameActions
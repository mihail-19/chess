import './Actions.css'
import { gameStart, offerGameStop, acceptGameStop } from "../service/GameService"
import { getUser } from "../service/UserService"

const GameActions = ({user, setUser}) =>{
    
    return (
        <div className='game-actions'>
            {getStartButton()}
        </div>

    )

    function getStartButton(){
        if(user && user.game ){
            if(!user.game.isStarted && user.username === user.game.creator.username){
                return <button className="actions__button" onClick={() => start()}>start</button>
            } else if(user.game.isStarted && !user.game.isFinished){

                return (
                    <div className='actions__game-finish'>
                        <button className="actions__button actions__button_game-finish" onClick={() => offerDraw()}>offer draw</button>
                        <button className="actions__button actions__button_game-finish" onClick={() => giveUp()}>give up</button>
                        {acceptStopOffer()}
                    </div>
                )
            }
        }
    }
    function acceptStopOffer(){
        const finProps = user.game.gameFinishProposition
        if(finProps && finProps.senderUsername !== user.username){
            return (
                <div className='actions__game-finish'>
                    <div className='actions__game-finish_text'>User {finProps.senderUsername} offers a draw</div>
                    <button className="actions__button  actions__button_game-finish" onClick={() => acceptDraw()}>accept</button>
                    <button className="actions__button  actions__button_game-finish" onClick={() => denyDraw()}>deny</button>
                </div>
            )
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

    function offerDraw(){
        offerGameStop(true).then(undefined)
    }

    function giveUp(){
        offerGameStop(false).then(undefined)
    }
    function acceptDraw(){
        acceptGameStop().then(undefined)
    }
    function denyDraw(){
        
    }
}

export default GameActions
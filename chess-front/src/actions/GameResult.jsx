import './Actions.css'
const GameResult = ({user}) => {
    if(user && user.game && user.game.isFinished){
        return(
            <div className="actions__game-result">
                <h3>Game is finished</h3>
                {results()}
            </div>
        )
    }

    function results(){
        if(user.game.winner === 'draw'){
            return (
                <h4>DRAW</h4>
            )
        } else {
            let winner = getUserByColor(user.game.winner)
            let loserColor = 'white'
            if(user.game.winner === 'white'){
                loserColor = 'black'
            }
            let loser = getUserByColor(loserColor)
            return (
                <div>
                    <h4>Winner ({user.game.winner}): {winner}</h4>
                    <h4>Loser ({loserColor}): {loser}</h4>
                </div>
            )
        }
    }

    function getUserByColor(userColor){
        if(user.game.creatorColor === userColor){
            return user.game.creator.username
        } else {
            return user.game.opponent.username
        }
    }
}
export default GameResult
import './GameUserData.css'
const GameUserData = ({user, isWhite}) => {
    if(user && user.game){
        const isWhiteCreator = user.game.creatorColor === 'white'
        let player
        if(isWhite){
            if(isWhiteCreator){
                player = user.game.creator
            } else {
                player = user.game.opponent
            }
        } else {
            if(isWhiteCreator){
                player = user.game.opponent
            } else {
                player = user.game.creator
            }
        }
        
        return (
            <div className="game-user-data">
                <div className="game-user-data__name">
                    {player.username} {isPlayerMove(user, player)}
                </div>
            </div>
        )
    }
    function isPlayerMove(user, player){
        const isPlayerMove = user.game.moverUsername === player.username
        if(isPlayerMove){
            return (
                <span>(move)</span>
            )
        }
    }
}
export default GameUserData
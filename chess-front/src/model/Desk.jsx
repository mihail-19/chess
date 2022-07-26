import React, { useState, useContext } from 'react'
import Row from './Row'
import '../App.css'
import availableMoves from './availableMoves'
import { isLabelWithInternallyDisabledControl } from '@testing-library/user-event/dist/utils'
import { useEffect } from 'react'
import {AuthContext} from '../context/authContext'
import defaultFigures from './defaultFigures'
import {gameMove } from '../service/GameService'
import { getUser } from '../service/UserService'
const Desk = ({user, setUser}) =>{
    //Getting game from server if connected, using useContext to get auth 
    const {isAuth, setIsAuth, 
        accessHeader, setAccessHeader,
        refreshHeader, setRefreshHeader,
        username, setUsername} = useContext(AuthContext)
    
    
    const [game, setGame] = useState({})

    const [figures, setFigures] = useState(defaultFigures())
    // Real figures - uncomment to use
    useEffect(() => {
        if(user && user.game && user.game.isStarted){
            setFigures(user.game.desk.figures.map(f => {
               return  {
                 color: f.color,
                isAlive: f.isAlive,
                columnId: f.field.columnId,
                rowId: f.field.rowId,
                type: f.type
            }}))
        } else {
            console.log('useEffect: set default figures')
            setFigures(defaultFigures())
        }
    }, [user])
    

    const [chosenFigure, setChosenFigure] = useState(undefined)
    const [moves, setMoves] = useState(undefined)

    function chosenFigureState(field){
        let figure = getFigureInField(field)
        if(figure){
            setChosenFigure(figure)
            let m = availableMoves(figure, figures)
            setMoves(m)
        } else {
            //if empty field
            setChosenFigure(undefined)
            setMoves(undefined)
        }
    }

    function getFigureInField(field){
        let figureInField = figures.filter(f => f.columnId === field.columnId && f.rowId === field.rowId)
        if(figureInField && figureInField.length > 0){
            return figureInField[0]
        } else {
            return undefined
        }
    }
    function unchosenState(){
    }

    function moveFigure(field){
        //if real game is on
        if(user && user.game){
            if(user.game.isStarted && !user.game.isFinished){
                //chek if it is current player move
                
                if(!isPlayerMove(user)){
                    console.log('could not move by player')
                    return undefined
                } else {
                    console.log('valid move')
                }
            } else {
                //if game created but is not going (not started or finished)
                return undefined
            }
        }

        //click on chosen figure - resest moveset
        if(chosenFigure && chosenFigure.columnId === field.columnId && chosenFigure.rowId === field.rowId){
            setChosenFigure(undefined)
            setMoves(undefined)
            return undefined
        }

        if(isValidMove(field)){
            //perform move is move is valid
            if(user && user.game && user.game.isStarted && !user.gameIsFinished){
                //move to server
                performMoveToServer(field)
            } else {
                //local game move
                performMove(field)
            }
        } else {
            //if move is not valid logic for chosen figure chane
            chosenFigureState(field)
        }
    }

    function isPlayerMove(usr){
        if(usr.game.moverUsername !== usr.username){
            return false
        } else {
            return true
        }
    }
    function isValidMove(field){
        //not valid move if no figure is chosen or no possible moves found
        if(!chosenFigure || !moves){
            return false
        }
        //true if moves contains move to given field
        return moves.filter(m => m.columnId === field.columnId && m.rowId === field.rowId).length === 1
    }
    function performMoveToServer(field){
        const from = {
            rowId: chosenFigure.rowId,
            columnId: chosenFigure.columnId
        }
        const to = {
            rowId: field.rowId,
            columnId: field.columnId
        }
        gameMove(from, to)
            .then(gm => {
                getUser().then(res => setUser(res.data))
                setMoves([])
            })
        
        //delete this on websocket update
        
    }
    function performMove(field){
        console.log(`move to field ${field.columnId}${field.rowId}`)
        let figureToMove = figures.filter(f => f.columnId === chosenFigure.columnId && f.rowId === chosenFigure.rowId)[0]
        //move itself
        let modified = figures.filter(f => !(f.columnId === chosenFigure.columnId && f.rowId === chosenFigure.rowId))
        modified = modified.filter(m => !(m.columnId === field.columnId && m.rowId === field.rowId))
        figureToMove.rowId = field.rowId
        figureToMove.columnId = field.columnId
        //eat figure
        
        modified.push(figureToMove)
        setFigures(modified)
        setChosenFigure(undefined)
        setMoves(undefined)

    }
    const [rows, setRows] = useState([
        {id: 1},
        {id: 2},
        {id: 3},
        {id: 4},
        {id: 5},
        {id: 6},
        {id: 7},
        {id: 8}
    ])
    return (
        <div className="desk" >
            {rows.map(row => <Row id={row.id} chooseFunc={chosenFigureState} unchooseFunc={unchosenState} move={moveFigure} figures={figures} moves={moves}/>)}
        </div>
    );
}


export default Desk;
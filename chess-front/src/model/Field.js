import { useState } from 'react'
import './model.css'
import Figure from './Figure'

const Field = ({id, rowId, isWhite, chooseFunc, unchooseFunc, move, figures, moves}) =>{
    let cName = isWhite ? 'field_white' : 'field_black'
    let arrVal = figures.filter(f => f.columnId === id && f.rowId === rowId)
    let figure = undefined
    if(arrVal && arrVal.length > 0){
        figure = arrVal[0]
    }
    let moveHintClass = 'move-hint_none'
    if(moves && moves.filter(m => m.columnId === id && m.rowId === rowId).length === 1){
        moveHintClass = 'move-hint_block'
    }
    if(figure){
        return (
            <div className={`field ${cName}`} onClick={() => move({columnId: id, rowId: rowId})}>
                <div className={moveHintClass}></div>
                <Figure columnId={figure.columnId} rowId={figure.rowId} type={figure.type} color={figure.color}/> 
             </div>
        )
    } else {
       return ( 
            <div className={`field ${cName}`} onClick={() => move({columnId: id, rowId: rowId})}>
                <div className={moveHintClass}></div>
            <Figure columnId={id} rowId={rowId} type={''} color={''}/> 
        </div>)
    }
}
function initialFigureType(columnId, rowId){
    let type = '';
    if(rowId === 2 || rowId === 7){
        type = 'pawn'
    }
    if(rowId === 1 || rowId === 8){
        if(columnId === 'a' || columnId === 'h'){
            type = 'rook'
        }
        if(columnId === 'b' || columnId === 'g'){
            type = 'knight'
        }
        if(columnId === 'c' || columnId === 'f'){
            type = 'bishop'
        }
        if(columnId === 'd'){
            type = 'queen'
        }
        if(columnId === 'e'){
            type = 'king'
        }
    }
    return type;
}
function figureColor(rowId){
    if(rowId < 3){
        return 'white'
    } else if(rowId > 6){
        return 'black'
    } else {
        return ''
    }
}
export default Field;
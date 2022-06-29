import React, { useState } from 'react'
import Row from './Row'
import '../App.css'
import availableMoves from './availableMoves'
import { isLabelWithInternallyDisabledControl } from '@testing-library/user-event/dist/utils'
const Desk = () =>{
    console.log('render desk')
    
    // Real figures - uncomment to use
    const [figures, setFigures] = useState(defaultFigures())

    //test figures
    //const [figures, setFigures] = useState(testFigures())
    
    const [chosenFigure, setChosenFigure] = useState(undefined)
    const [moves, setMoves] = useState(undefined)

    function chosenFigureState(field){
       // console.log(`choosing figure for field ${field.columnId}${field.rowId}`)
        let figure = getFigureInField(field)
        if(figure){
            setChosenFigure(figure)
            let m = availableMoves(figure, figures)
      //      console.log(`moves for figure `)
       //     console.log(figure)
      //      console.log(m)
            setMoves(m)
        } else {
            //if empty field
            setChosenFigure(undefined)
            setMoves(undefined)
        }
    }

    function getFigureInField(field){
        let figureInField = figures.filter(f => f.columnId === field.columnId && f.rowId === field.rowId)
        //todo player restrictions
        if(figureInField && figureInField.length > 0){
            return figureInField[0]
        } else {
            return undefined
        }
    }
    function unchosenState(){
    }

    function moveFigure(field){
        if(chosenFigure && chosenFigure.columnId === field.columnId && chosenFigure.rowId === field.rowId){
            console.log('self')
            setChosenFigure(undefined)
            setMoves(undefined)
            return undefined
        }
        if(chosenFigure && moves && moves.filter(m => m.columnId === field.columnId && m.rowId === field.rowId).length === 1){
            console.log(`move to field ${field.columnId}${field.rowId}`)
            let figureToMove = figures.filter(f => f.columnId === chosenFigure.columnId && f.rowId === chosenFigure.rowId)[0]
            //state - valid move, invalid move - depends on figure type
            
            //if invalid - same color and choose again, or reset choosing figure
            
            //if valid - just move or take enemy's figure

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
        } else {
            chosenFigureState(field)
        }
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
function testFigures(){
    let figures = []
    figures.push({
        columnId: 'd',
        rowId: 5,
        type: 'king',
        color: 'white',
        isAlive: true
    })
    figures.push({
        columnId: 'd',
        rowId: 2,
        type: 'rook',
        color: 'black',
        isAlive: true

    })
    figures.push({
        columnId: 'f',
        rowId: 5,
        type: 'rook',
        color: 'white',
        isAlive: true
    })
    return figures
}
function defaultFigures(){
    let figures = [];
    let colId = 'a'
    for(let i = 0; i<8;i++){
        figures.push({
            columnId: colId,
            rowId: 2,
            type: 'pawn',
            color: 'white',
            isAlive: true 
        })
        figures.push({
            columnId: colId,
            rowId: 7,
            type: 'pawn',
            color: 'black',
            isAlive: true
        })
        colId = String.fromCodePoint(colId.charCodeAt(0) + 1)
    }
    figures.push({columnId: 'a', rowId: 1, type: 'rook', color: 'white', isAlive: true})
    figures.push({columnId: 'h', rowId: 1, type: 'rook', color: 'white', isAlive: true})
    figures.push({columnId: 'a', rowId: 8, type: 'rook', color: 'black', isAlive: true})
    figures.push({columnId: 'h', rowId: 8, type: 'rook', color: 'black', isAlive: true})
    figures.push({columnId: 'b', rowId: 1, type: 'knight', color: 'white', isAlive: true})
    figures.push({columnId: 'g', rowId: 1, type: 'knight', color: 'white', isAlive: true})
    figures.push({columnId: 'b', rowId: 8, type: 'knight', color: 'black', isAlive: true})
    figures.push({columnId: 'g', rowId: 8, type: 'knight', color: 'black', isAlive: true})
    figures.push({columnId: 'c', rowId: 1, type: 'bishop', color: 'white', isAlive: true})
    figures.push({columnId: 'f', rowId: 1, type: 'bishop', color: 'white', isAlive: true})
    figures.push({columnId: 'c', rowId: 8, type: 'bishop', color: 'black', isAlive: true})
    figures.push({columnId: 'f', rowId: 8, type: 'bishop', color: 'black', isAlive: true})
    figures.push({columnId: 'd', rowId: 1, type: 'queen', color: 'white', isAlive: true})
    figures.push({columnId: 'd', rowId: 8, type: 'queen', color: 'black', isAlive: true})
    figures.push({columnId: 'e', rowId: 1, type: 'king', color: 'white', isAlive: true})
    figures.push({columnId: 'e', rowId: 8, type: 'king', color: 'black', isAlive: true})

    
    return figures
}

export default Desk;
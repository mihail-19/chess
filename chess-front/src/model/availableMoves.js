
//returns an array of available moves for given figure. Move is 
//available to empty field or enemy's figure depending on figure type
function availableMoves(figure, figures){
    if(figure.type === 'pawn'){
        return pawnMoves(figure, figures)
    } else if(figure.type === 'knight'){
        return knightMoves(figure, figures)
    } else if(figure.type === 'bishop'){
        return bishopMoves(figure, figures)
    } else if(figure.type === 'rook'){
        return rookMoves(figure, figures)
    } else if(figure.type === 'queen'){
        return queenMoves(figure, figures)
    } else if(figure.type === 'king'){
        return kingMoves(figure, figures)
    }
}
function pawnMoves(figure, figures){
    let moves = []
    let {columnId, rowId} = figure
    if(figure.color === 'white'){
        //forward
        if(figures.filter(f => f.rowId === rowId + 1 && f.columnId === columnId).length === 0){
           moves.push({columnId: columnId, rowId: rowId + 1})
           if(figure.rowId === 2 && figures.filter(f => f.rowId === rowId + 2 && f.columnId === columnId).length === 0){
                moves.push({columnId: columnId, rowId: rowId + 2})
           }
        }
        //take figure
        let leftFigure = figureAt(figures, shift(figure.columnId, -1), figure.rowId + 1)
        if(leftFigure && leftFigure.color !== figure.color){
            moves.push({columnId: leftFigure.columnId, rowId: leftFigure.rowId})
        }
        let rightFigure = figureAt(figures, shift(figure.columnId, 1),figure.rowId + 1)
        if(rightFigure && rightFigure.color !== figure.color){
            moves.push({columnId: rightFigure.columnId, rowId: rightFigure.rowId})
        }
        
    } else {
        if(figures.filter(f => f.rowId === rowId - 1 && f.columnId === columnId).length === 0){
            moves.push({columnId: columnId, rowId: rowId - 1})
            if(figure.rowId === 7 && figures.filter(f => f.rowId === rowId - 2 && f.columnId === columnId).length === 0){
                 moves.push({columnId: columnId, rowId: rowId - 2})
            }
         }
          //take figure
        let leftFigure = figureAt(figures, shift(figure.columnId, -1), figure.rowId - 1)
        if(leftFigure && leftFigure.color !== figure.color){
            moves.push({columnId: leftFigure.columnId, rowId: leftFigure.rowId})
        }
        let rightFigure = figureAt(figures, shift(figure.columnId, 1), figure.rowId - 1)
        if(rightFigure && rightFigure.color !== figure.color){
            moves.push({columnId: rightFigure.columnId, rowId: rightFigure.rowId})
        }
    }
    return moves
    
}
function knightMoves(figure, figures){
    const moves = []
    //high left
    let columnId = shift(figure.columnId, -1)
    let rowId = figure.rowId + 2
    otherFigureOnWay(figures, figure.color, columnId, rowId, moves)

    //high right
    columnId = shift(figure.columnId, 1)
    otherFigureOnWay(figures, figure.color, columnId, rowId, moves)

    //low left
    columnId = shift(figure.columnId, -1)
    rowId = figure.rowId - 2
    otherFigureOnWay(figures, figure.color, columnId, rowId, moves)

    //low right
    columnId = shift(figure.columnId, 1)
    otherFigureOnWay(figures, figure.color, columnId, rowId, moves)

    //middle left top
    columnId = shift(figure.columnId, -2)
    rowId = figure.rowId + 1
    otherFigureOnWay(figures, figure.color, columnId, rowId, moves)

    //middle left bottom
    rowId = figure.rowId - 1
    otherFigureOnWay(figures, figure.color, columnId, rowId, moves)

    //middle right top
    columnId = shift(figure.columnId, 2)
    rowId = figure.rowId + 1
    otherFigureOnWay(figures, figure.color, columnId, rowId, moves)

    //middle right bottom
    rowId = figure.rowId - 1
    otherFigureOnWay(figures, figure.color, columnId, rowId, moves)
    return moves
}

function bishopMoves(figure, figures){
    let moves = []
    let columnId = shift(figure.columnId, -1)
    let rowId = figure.rowId - 1

    //left bottom
    let isFinished = false;
    while(!isFinished){
        isFinished = otherFigureOnWay(figures, figure.color, columnId, rowId, moves)
        columnId = shift(columnId, -1)
        rowId = rowId - 1
    }

    //right bottom
    isFinished = false
    columnId = shift(figure.columnId, 1)
    rowId = figure.rowId - 1
    while(!isFinished){
        isFinished = otherFigureOnWay(figures, figure.color, columnId, rowId, moves)
        columnId = shift(columnId, 1)
        rowId = rowId - 1
    }

    //left top
    isFinished = false
    columnId = shift(figure.columnId, -1)
    rowId = figure.rowId + 1
    while(!isFinished){
        isFinished = otherFigureOnWay(figures, figure.color, columnId, rowId, moves)
        columnId = shift(columnId, -1)
        rowId = rowId + 1
    }

    //right top
    isFinished = false
    columnId = shift(figure.columnId, 1)
    rowId = figure.rowId + 1
    while(!isFinished){
        isFinished = otherFigureOnWay(figures, figure.color, columnId, rowId, moves)
        columnId = shift(columnId, 1)
        rowId = rowId + 1
    }

    return moves
}

function rookMoves(figure, figures){
    let moves = []
    let columnId = figure.columnId
    let rowId = figure.rowId - 1
    //down
    while(rowId >= 1){
        if(otherFigureOnWay(figures, figure.color, columnId, rowId, moves)){
            break;
        }
        rowId--;
    }
    //up
    rowId = figure.rowId + 1
    while(rowId <= 8){
        if(otherFigureOnWay(figures, figure.color, columnId, rowId, moves)){
            break;
        }
        rowId++;
    }
    rowId = figure.rowId
    columnId = shift(figure.columnId, -1)
    //left
    while(columnId >= 'a'){
        if(otherFigureOnWay(figures, figure.color, columnId, rowId, moves)){
            break;
        }
        columnId = shift(columnId, -1)
    }
    columnId = shift(figure.columnId, 1)
    //right
     while(columnId <='h'){
        if(otherFigureOnWay(figures, figure.color, columnId, rowId, moves)){
            break;
        }
         columnId = shift(columnId, 1)
     }
    return moves
}
function otherFigureOnWay(figures, color, columnId, rowId, moves){
    console.log('func')
    if(columnId < 'a' || columnId > 'h' || rowId < 1 || rowId > 8){
        return true
    }
    let figure = figureAt(figures, columnId, rowId)
    console.log(figure)
    if(!figure){
        moves.push({columnId, rowId})
        return false
    } else {
        if(figure.color !== color){
            moves.push({columnId, rowId})
        }
        return true
    }
}
function queenMoves(figure, figures){
    let moves = rookMoves(figure, figures)
    return  moves.concat(bishopMoves(figure, figures))
}

function kingMoves(figure, figures){
    let moves = []
    otherFigureOnWay(figures, figure.color, figure.columnId, figure.rowId + 1, moves)
    otherFigureOnWay(figures, figure.color, figure.columnId, figure.rowId - 1, moves)

     otherFigureOnWay(figures, figure.color, shift(figure.columnId, 1), figure.rowId, moves)
     otherFigureOnWay(figures, figure.color, shift(figure.columnId, -1), figure.rowId, moves)

    otherFigureOnWay(figures, figure.color, shift(figure.columnId, 1), figure.rowId + 1, moves)
    otherFigureOnWay(figures, figure.color, shift(figure.columnId, -1), figure.rowId + 1, moves)
    otherFigureOnWay(figures, figure.color, shift(figure.columnId, 1), figure.rowId - 1, moves)
    otherFigureOnWay(figures, figure.color, shift(figure.columnId, -1), figure.rowId - 1, moves)

    return moves
}
function figureAt(figures, columnId, rowId){
    let fArr = figures.filter(f => f.columnId === columnId && f.rowId === rowId)
    if(fArr.length > 0){
        return fArr[0]
    } else {
        return undefined
    }
}
function shift(column, shift){
    let colNumShiftedValue = column.charCodeAt(0) + shift
    return String.fromCharCode(colNumShiftedValue)
}
export default availableMoves;
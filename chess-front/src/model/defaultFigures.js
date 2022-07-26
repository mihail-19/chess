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

export default defaultFigures
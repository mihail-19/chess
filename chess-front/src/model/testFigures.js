
//Util function to create test figures
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

export default testFigures
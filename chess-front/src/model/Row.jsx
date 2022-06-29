import React, { useState } from 'react'
import Field from './Field'
import '../App.css'

const Row = ({id, chooseFunc, unchooseFunc, move, figures, moves}) => {
    const fieldsArr = [
        {id: 'a', rowId: id, isWhite: id%2 == 0},
        {id: 'b', rowId: id, isWhite: id%2 != 0},
        {id: 'c', rowId: id, isWhite: id%2 == 0},
        {id: 'd', rowId: id, isWhite: id%2 != 0},
        {id: 'e', rowId: id, isWhite: id%2 == 0},
        {id: 'f', rowId: id, isWhite: id%2 != 0},
        {id: 'g', rowId: id, isWhite: id%2 == 0},
        {id: 'h', rowId: id, isWhite: id%2 != 0}
    ]
    const [row, setRows] = useState(fieldsArr)
    return (
        <div className="desk__row">
            {row.map(field => 
                <Field id={field.id} rowId={field.rowId} isWhite={field.isWhite}  key={field.id} chooseFunc={chooseFunc} unchooseFunc={unchooseFunc} move={move} figures={figures} moves={moves}/>
            )}
        </div>
      );

}

export default Row;
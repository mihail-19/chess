import React from 'react'
import '../App.css'

const Figure = ({columnId, rowId, type, color}) => {
    if(type && type.length > 0){
        let imgSrc = `./desk/${type}-${color.charAt(0)}.png`
        return (
            <div className="desk__figure" >
                <img src={imgSrc} alt={type}></img>
            </div>
        );
    } else {
        return (
            <div className="desk__figure" >
            </div>
        )
    }
}

export default Figure;
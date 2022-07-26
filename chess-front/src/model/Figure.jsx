import React from 'react'
import '../App.css'

const Figure = ({columnId, rowId, type, color}) => {
    if(type && type.length > 0){
        let imgSrc = `${process.env.PUBLIC_URL}/desk/${type}-${color.charAt(0)}.png`
        //let imgSrc = "/favico.ico"
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
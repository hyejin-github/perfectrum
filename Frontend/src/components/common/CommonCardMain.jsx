import React from "react";
import './CommonCard_module.css'

const CommonCardMain = (props) => {
  return(
    <div style={{ width : "auto" }} className="card">
      <img src={props.img} style={{width:"355px", height:"417px"}}/>
      <div className="card-body">
        <h2>{props.title}</h2>
        <p style={{fontSize: "36px", fontWeight : "bold"}}>{props.description}</p>
        <h5>{props.author}</h5>
      </div>
    </div>
  )
}

export default CommonCardMain;
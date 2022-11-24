import React from "react";
import './CommonCard_module.css'

const CommonCard = (props) => {
  return(
    <div style={{ width : "15rem" }} className="card">
      <img src={props.img} />
      <div className="card-body">
        <h2>{props.title}</h2>
        <p>{props.description}</p>
        <h5>{props.author}</h5>
      </div>
    </div>
  )
}

export default CommonCard;
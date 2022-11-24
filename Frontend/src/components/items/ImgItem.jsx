import React from 'react';
import styled from "styled-components";
import './ImgItem.css';
import { Button } from "@material-ui/core"
import history from "../../utils/history";
import { NavLink } from "react-router-dom";

const DivCenter = styled.div`
    text-align: center;
`

const Img = styled.img`
width:100%;
height:850px;
`



const ImgItem = (props) => {
    const imgSrc = props.item.src;
    const idxSrc = props.item.idx;

    let maintheme = "";
    let subtheme = "";
    // let vis = false;

    if(idxSrc === 0){
        maintheme = "PERSONAL PERFUME";
        subtheme = "당신의 가치를 빛내줄 퍼스널 향수 추천";
        // vis = true;
    }else if(idxSrc === 1) {
        maintheme = "RECOMMEND FOR 'YOU'"
        subtheme = "당신을 위한 맞춤 향수 추천"
    }else{
        maintheme = "BE ELEGANCE"
        subtheme ="당신의 품격을 더해줄 향수"
    }
    
    return(
    <DivCenter>
            <div className="banner">
                <Img src={imgSrc} alt=""/>  
                <div className="banner-txt">
                    <p className="maintheme">{maintheme}</p>
                    <p className="subtheme">{subtheme}</p>
                    <br/>
                </div>
            </div>
    </DivCenter>
    );
};

export default ImgItem;
import React from 'react';
import {Container, Box} from "@mui/material";
import "./PollResult_module.css"
import {useEffect} from "react";
import {Button} from "@mui/material";
import kakaoShare from "../../assets/images/btn-att-kak.png"
import {Link, useNavigate} from "react-router-dom";
import {useRecoilState} from "recoil";
import {surveyPerfume} from "../../atom";

const Card = (props) => {
    return(
        <div className="card">
            <img src={props.img} />
            <div className="card-body">
                <h2>{props.title}</h2>
                <p>{props.description}</p>
                <h5>{props.author}</h5>
            </div>
        </div>
    )
}


const PollResult = () => {
    const navigate = useNavigate()
    const [resultPerfume, setResultPerfume2] = useRecoilState(surveyPerfume)

    useEffect(() => {
        const script = document.createElement('script');
        script.src = "https://developers.kakao.com/sdk/js/kakao.js";
        script.async = true;
        document.body.appendChild(script)
    }, []);

    const shareToKakao = () => {
        if(window.Kakao){
            const kakao = window.Kakao
            if(!kakao.isInitialized()){
                kakao.init("a6bb9cbde484dc4c12e60c1748f41b94")
            }

            kakao.Link.sendDefault({
                objectType : "feed",
                content : {
                    title : `나와 어울리는 향수는 ${resultPerfume.perfume_name}! ✨`,
                    description : `나와 어울리는 향수를 찾아보고 싶다면?`,
                    imageUrl : `${resultPerfume.perfume_img}`,
                    link : {
                        mobileWebUrl : "http://j7c105.p.ssafy.io/personal",
                        webUrl : "http://j7c105.p.ssafy.io/personal"
                    }
                }
            })
        }
    }


    return (
        <>
            <div className="header">
                <h1>🎉 당신과 어울리는 향수입니다. 🎉</h1>
                <h3>-Recommended by <span className="text-rainbow">Perfectrum</span>-</h3>
            </div>

            <div onClick={() => navigate(`/detail/${resultPerfume.idx}`) } className='cards'>
                <Card
                    img={resultPerfume.perfume_img}
                    title={resultPerfume.perfume_name}
                    author={resultPerfume.brand_name}
                    description={resultPerfume.description}
                />
            </div>
            {/* eslint-disable-next-line jsx-a11y/click-events-have-key-events,jsx-a11y/no-noninteractive-element-interactions */}
            <Box sx={{display : "flex", justifyContent : "center" }}>
                {/* eslint-disable-next-line jsx-a11y/click-events-have-key-events,jsx-a11y/no-noninteractive-element-interactions */}
                <img src={kakaoShare} style={{ objectFit : "cover", width:"20%", height: "10%", marginBottom : "20px" }} onClick={() => {
                    shareToKakao()
                }}/>
            </Box>

        </>
    )
};

export default PollResult;

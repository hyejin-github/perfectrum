import React, {useState} from 'react';

import styled from 'styled-components';

import {Box} from "@mui/material";
import history from '../utils/history';
import {surveyResult} from "../apis/perfumeAPI";
import {surveyPerfume} from "../atom";
import {useRecoilState} from "recoil";
import {useNavigate} from "react-router-dom";

const Content = styled.div`
    font-family: SUIT Variable;
    color : white;
    text-shadow: 5px 5px 5px black;
    font-weight: bold;
    text-align: center;
    padding: 100px 0px 0px 0px;
    //margin: 15px 15px 0px 0px;
    font-size: 64px;
    //position: absolute;
`;



const SubContent = styled.p`
    font-family: SUIT Variable;
    font-weight: bold;
    //color : white;
    color : white;
    text-shadow: 5px 5px 5px black;
    text-align: center;
    margin: 15px 15px 0px 0px;
    font-size: 56px;
`;

const StyledButton = styled.button`
    width:360px;
    height:72px;
    margin-top: 15%;
    margin-right: 20px;
    color: white;
    background-color: black;
    border-radius: 8px;
    padding: 3px 45px 3px 45px;
    font-size: 32px;
    font-family: SUIT Variable;
    font-weight: bold;
`


const Question = (props) => {
        const {id, getId} = props; // 비구조화 할당 문법
        const [imgUrl, setImgUrl] = useState('https://image.kkday.com/v2/image/get/s1.kkday.com/product_121586/20211022011216_g2YFw/jpg')
        const [response, setResponse] = useState("")
        const [survey, setSurvey] = useState([])
        const [resultPerfume, setResultPerfume] = useRecoilState(surveyPerfume)
        const navigator = useNavigate()

        const BackgroundWrap = styled.div`
          opacity: 0.9;
          background-image: url(${imgUrl});
          background-size: cover;
          width: 100vw;
          height: 100vh;
          padding: 0px;
      `;

        const questions = [
            {question: '', ans: '', img: "https://www.brides.com/thmb/7Upc0zW3X-H8qpusm73nD-E7iRY=/780x0/filters:no_upscale():max_bytes(150000):strip_icc():gifv():format(webp)/__opt__aboutcom__coeus__resources__content_migration__brides__proteus__5b845ee6d83eb614e2366a65__11-3d487af5ba534727a565f6b6d3d5e503.jpeg"},
            {question: 'Q1. 🌻 선호하는 계절을 알려주세요.🍁', ans: ['봄', '여름', '가을', '겨울'], img: "https://p4.wallpaperbetter.com/wallpaper/661/716/948/four-seasons-1920x1076-nature-seasons-hd-art-wallpaper-preview.jpg"},
            {question: 'Q2. 성별을 알려주세요.', ans: ['남자', '상관없음', '여자'], img: "https://www.dictionary.com/e/wp-content/uploads/2019/02/1000x700-gender-vs-sex.jpg"},
            {question: 'Q3. 향이 오래 갔으면 좋겠나요?', ans: ['네', '아니오'], img: "https://www.10wallpaper.com/wallpaper/1366x768/1308/hourglass-High_quality_wallpapers_1366x768.jpg"},
            {question: 'Q4. 향의 느낌을 선택해주세요', ans: ['강렬함', '부드러운'], img: "https://image.fmkorea.com/files/attach/new/20200421/5665468/31180/2877051892/99b983892094b5c6d2fc3736e15da7d1.jpg"},
            {question: 'Q5. 다음중 좋아하는 향을 선택해주세요', ans: ['매운 향', '톡쏘는 향','야성적인 향','인공적인 향'], img: "https://www.osmoz.com/Public/Files/article/best_spicy_perfumes_48c81ae21d.jpg"},
            {question: 'Q5. 다음중 좋아하는 향을 선택해주세요', ans: ['꽃 향기', '풀 향기','과일 향','달콤한 향'], img: "https://blog.kakaocdn.net/dn/VKPa4/btqZ3fjNBuu/ZdyWw9jODbhI2043L5Ime0/img.jpg"}

        ];
        const onClick = (ans) => {

            console.log(questions[id].ans.length);
            console.log(questions[id].ans[0]);
            console.log(ans)
            setSurvey([ans,...survey])
            console.log(survey)
            
            if(id === 5 || id === 6){
                history.push('/');
            }else if(id === 4 && ans === "부드러운"){
                console.log("강렬해")
                getId(id+2);
                setImgUrl((questions[id+2].img))
            }
            else{
                getId(id + 1);
                setImgUrl(questions[id+1].img)
            }
            
        };
        console.log(survey)
        if(survey.length === 6){
            surveyResult(survey[4],survey[3],survey[2],survey[0])
                .then((res) => {res.json().then((res) => {
                    console.log(res)
                    setResultPerfume({
                        idx : res.perfume.idx,
                        perfume_name : res.perfume.perfumeName,
                        brand_name : res.perfume.brandName,
                        perfume_img : res.perfume.perfumeImg,
                        description : res.perfume.description
                    })
                    navigator('/pollresult')
                })})
        }
        

        const list = []

        for(let i = 0; i < questions[id].ans.length; i++){
            const a = questions[id].ans;

            list.push(<StyledButton key={a[i]} onClick ={() => {
                    // event.preventDefault();
                    onClick(a[i]);
            }}>{a[i]}</StyledButton>)
        }

        let content = null;

        if(id === 0){
            content = '나만의 향수를 추천해드립니다.';
            list.push(<StyledButton key='start' onClick={onClick}>시작하기</StyledButton>)
        }

        console.log(list);
        return (
            <Box>
                {/*<img style={{ width : '100%' }} src="https://static.overlay-tech.com/assets/38a95fc7-fb7d-4c9a-8f8b-acde86a3f47f.svg" />*/}
                <BackgroundWrap>
                    <Content>{content}</Content>
                    <SubContent>{questions[id].question}</SubContent>
                     {list}
                </BackgroundWrap>
            </Box>
        );
    };

export default Question;
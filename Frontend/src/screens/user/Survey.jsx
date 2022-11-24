import { React, useState } from "react";
import uuid from 'react-uuid';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { surveyState, userState } from "../../atom";
import "./Survey.scss";
import SurveyItem from "../../components/user/SurveyItem"





function Survey() {

  const navigate = useNavigate();
  const userLoginState = useRecoilValue(userState);
  const survey = useRecoilValue(surveyState);
  const setSurvey = useSetRecoilState(surveyState);
  const list = Object.values(survey.data).map((item)=>(<SurveyItem key={uuid()} item={item}/>))

  const completeSurvey = () => {

    const LUT = {
      "유니섹스" : "Unisex",
      "남성" : "Men", 
      "여성" : "Women", 
      "봄" : "spring",
      "여름" : "summer", 
      "가을" : "fall", 
      "겨울" : "winter", 
      "톡쏘는 향" : 1, 
      "꽃 향기" : 2, 
      "풀 향기" :3,  
      "과일 향" : 4,  
      "매운 향" : 5, 
      "야성적인 향" : 6, 
      "인공적인 향" : 7, 
      "달콤한 향" : 8, 
    }

    // 리코일에서 정보 받아다가 백엔드 요청
    axios({
      method: "post",
      url : "http://j7c105.p.ssafy.io:8083/user/",
      headers : {
        Authorization : userLoginState.sToken
      },
      data : {
          "gender" : LUT[`${survey.data[1].sentence[0]}`],
          "seasons" : LUT[`${survey.data[3].sentence[0]}`],
          "accordClass" : LUT[`${survey.data[5].sentence[0]}`]
      }
    })
    .then(res => console.log(res))
    .catch(err => console.log(err))

    navigate("/", { replace: true });
    
  }

  return (
    <div className="container flex">
          <div className="surveytitle_main notoBold fs-20"> 향수 봇과 이야기 하면서 선택해 보세요!</div>
          <div className="surveytitle_window">
          {list.slice(0, survey.ptr + 1)}
          </div>

          {survey.ptr >= 7 && <button type="button" className="surveytitle_btn notoBold fs-20" onClick={completeSurvey}>정보 입력 완료</button>}
    </div>
  )
};

export default Survey;
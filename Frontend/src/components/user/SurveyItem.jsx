import React from "react";
import uuid from 'react-uuid';
import bot from "@images/icon/bot.png";
import dummyProfile from "@images/icon/dummyIcon.png";
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { surveyState } from "../../atom";
import "./SurveyItem.scss";


function SurveyItem (props) {
  const survey = useRecoilValue(surveyState);
  const setSurvey = useSetRecoilState(surveyState);

  const surveyClick = (choice) => {
    const copy = JSON.parse(JSON.stringify(survey));
    copy.ptr = survey.ptr + 2;
    copy.data[survey.ptr].sentence = [choice]
    setSurvey(copy)
  }
  const lst = props.item.sentence.map((item) => (
  <div>
      <button className="notoBold fs-14 serveycontainer_user_btn" type="button" key={uuid()} onClick={() => {surveyClick(item)}}> {item}</button>
  </div>
  ))

  return (
    <div className="serveycontainer">
      <div className="serveycontainer_bot">
        {(props.item.step % 2 === 1) && <div className="notoBold fs-18"> <img className="serveycontainer_bot_img" src={bot} alt="" />{props.item.sentence}</div>}
      </div>
      <div className="serveycontainer_user">
        {(props.item.step % 2 === 0) && lst}
      </div>
    </div>
    
  );
}
export default SurveyItem;
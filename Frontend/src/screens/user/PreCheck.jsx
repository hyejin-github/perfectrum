import { React } from "react";
import { useNavigate } from 'react-router-dom';
import check from "@images/icon/check.png";
import "./PreCheck.scss";






function PreCheck() {
    const navigate = useNavigate();
    const goService = () => {
        navigate("/", { replace: true });
    }
    const goSurvey = () => {
        navigate("/survey", { replace: true });
    }
  return (
    <div className="container flex justify-center">
        <div className="PreCheck_window">
            <div className="PreCheck_box">
                <img className="PreCheck_img" src={check} alt="" />
            </div>
            <div className="PreCheck_box notoBold fs-28">
                회원가입 성공!
            </div>
            <div>
                <button className="PreCheck_btn notoBold fs-14" type="button" onClick={goSurvey}>
                    추가 정보 입력하기
                </button>
            </div>
            <div>
                <button className="PreCheck_btn notoBold fs-14" type="button" onClick={goService}>
                    서비스 이용하러 가기
                </button>
            </div>
        </div>
    </div>
  )
};

export default PreCheck;
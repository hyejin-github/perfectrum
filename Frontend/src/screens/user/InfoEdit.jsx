import { React, useState, useRef }  from "react";

import axios from "axios";

import uuid from "react-uuid"
import dummyProfile from "@images/icon/dummyIcon.png";
import "./InfoEdit.scss";
// import SelectItem from "@components/user/SelectItem";
import Select from "@components/user/SelectItem";
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { userProfileState, userState } from "../../atom";
import { storage } from "../../firebase";
import { useNavigate } from "react-router-dom";

const sex = [
  { value: "Unisex", name: "성별 무관" },
  { value: "Men", name: "남성" },
  { value: "Women", name: "여성" }
];
const season = [
  { value: "spring", name: "봄" },
  { value: "summer", name: "여름" },
  { value: "fall", name: "가을" },
  { value: "winter", name: "겨울" }
];
const likeScent = [
  { value: "citrus", name: "톡쏘는 향" },
  { value: "floral", name: "꽃 향기" },
  { value: "herbal", name: "풀 향기" },
  { value: "fruity", name: "과일 향" },
  { value: "spicy", name: "매운 향" },
  { value: "animalic", name: "야성적인 향" },
  { value: "synthetic", name: "인공적인 향" },
  { value: "sweet", name: "달콤한 향" }
];
const LUT = {
  "citrus" : 1,
  "floral" : 2,
  "herbal" : 3,
  "fruity" : 4,
  "spicy" : 5,
  "animalic" : 6,
  "synthetic" : 7,
  "sweet" : 8,
};

function InfoEdit() {
  const navigate = useNavigate();
  const userProfile = useRecoilValue(userProfileState);
  const setUserProfile = useSetRecoilState(userProfileState);
  const userLoginState = useRecoilValue(userState);
  const [isChecked, setIsChecked] = useState(0);
  const [image, setImage] = useState({
    image_file: "",
    preview_URL: `${userProfile[0].profileImg}`,
  });
  const [imageUrl, setImageUrl] = useState("");
  // let inputRef;
  const inputRef = useRef();
  const nicknameRef = useRef();
  const genderRef = useRef();
  const seasonsRef = useRef();
  const accordClassRef = useRef();


  const completeEdit = () => {
    if (isChecked) {
      const accord = accordClassRef.current.value;
      const copy = JSON.parse(JSON.stringify(userProfile));
      copy[0].nickname = nicknameRef.current.value;
      copy[0].gender = genderRef.current.value;
      copy[0].seasons = seasonsRef.current.value;
      copy[0].accordClass = LUT[accord];
      setUserProfile(copy)
      // console.log("어코드 디버깅", accord);
      // console.log("클래스 디버깅", LUT[accord]);
      axios({
        method: "put",
        url : "http://j7c105.p.ssafy.io:8083/user/",
        headers : {
          Authorization : userLoginState.sToken
        },
        data : {
          "nickname" : nicknameRef.current.value,
          "profileImg" : userProfile[0].profileImg,
          "gender" : genderRef.current.value,
          "seasons" : seasonsRef.current.value,
          "accordClass" : LUT[accord]
        }
      })
      .then(res => console.log("엑시오스", res))
      .catch(err => console.log(err))
      window.location.replace(`/userreview/${nicknameRef.current.value}`);
      // navigate(`/userreview/${nicknameRef.current.value}`)
    } else {
      alert("닉네임 중복 체크를 해주세요")
    }
  };


  const editImg = (e) => {
    e.preventDefault();
    inputRef.current.click();
  };


  const handleChangeFile = (e) => {
    e.preventDefault()
    console.log(e.target.files[0])
    if(e.target.files[0]){
        URL.revokeObjectURL(image.preview_URL);
        const preview_URL = URL.createObjectURL(e.target.files[0])
        console.log(preview_URL)
        setImage(() => (
            {
              image_file: e.target.files[0],
              preview_URL: preview_URL
            }
        ))
      console.log(image)
      const storageRef = storage.ref("userProfile/test/")
      const imageRef = storageRef.child(e.target.files[0].name)
      const upLoadTask = imageRef.put(e.target.files[0])
      upLoadTask.on(
          "state_changed",
          (snapshot) => {
            console.log("snapshot", snapshot);
            const percent = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
            console.log(percent + "% done");
          },
          (error) => {
            console.log("err", error);
          },
          () => {
            upLoadTask.snapshot.ref.getDownloadURL().then((downloadURL) => {
              console.log("File available at", downloadURL);
              setImageUrl(downloadURL);
              console.log(downloadURL)
              const copy = JSON.parse(JSON.stringify(userProfile));
              copy[0].profileImg = downloadURL;
              setUserProfile(copy)
            });
          }
      )
    }
  };

  const checkNickname = () => {
    axios({
      method: 'get',
      url: `http://j7c105.p.ssafy.io:8083/user/check/${nicknameRef.current.value}`,
    })
    .then(res => {
       if (res.data.message === "FAIL") {
        alert("닉네임이 중복되었습니다.")
       } else if (res.data.message === "SUCCESS") {
        setIsChecked(1);
        alert("닉네임이 중복되지 않았습니다. 계속 진행하세요.")
       }
    })
    .catch(err => {alert("관리자에게 문의 하세요")});
  };

  return (
    <div className="container flex justify-center">
      <div id="infoedit" className="infoedit">
        <div id="infoedit1" className="infoedit1 flex justify-center">
          <div className="infoedit1_title notoBold fs-28">개인정보 수정</div>
          { !userProfile[0]?.profileImg && <img
            src={dummyProfile}
            alt="Profile_Image"
            className="infoedit1_profileimg"
          />}
          { !!userProfile[0]?.profileImg && <img
            src={userProfile[0].profileImg}
            alt="Profile_Image"
            className="infoedit1_profileimg"
          />}
          <input type="file" className="infoedit1_imginput" name="imgFile" id="imgFile" ref={inputRef} onChange={handleChangeFile}/>
          <button className="infoedit_top_btn notoBold fs-15" type="button" onClick={editImg} >
            프로필 이미지 변경
          </button>
          <div className="divide" />
        </div>
        <div id="infoedit2" className="infoedit2">
          <div className="infoedit2_title notoBold fs-15">닉네임 변경</div>
          <input
            type="text" 
            className="infoedit3_input notoMid fs-14"
            placeholder="2~8자리의 문자로 입력해주세요"
            ref={nicknameRef}
          />
          <button className="infoedit2_btn notoBold fs-15" type="button" onClick={checkNickname}>중복 체크</button>
        </div>
        <div id="infoedit3" className="infoedit3">
          <div className="infoedit3_title notoBold fs-15">성별</div>

          <Select className="infoedit3_input notoMid fs-14" ref={genderRef}>
            <option value="default" disabled>성별을 선택해주세요</option>
            {sex.map((item) => (
              <option key={uuid()} value={item.value}>
                {item.name}
              </option>
            ))}
          </Select>
        </div>
        <div id="infoedit4" className="infoedit4">
          <div className="infoedit4_title notoBold fs-15">선호하는 계절</div>
          <Select className="infoedit4_input notoMid fs-14" ref={seasonsRef}>
            <option value="default" disabled>좋아하는 계절을 선택해주세요</option>
            {season.map((item) => (
              <option key={uuid()} value={item.value}>
                {item.name}
              </option>
            ))}
          </Select>
        </div>
        <div id="infoedit5" className="infoedit5">
          <div className="infoedit5_title notoBold fs-15">선호하는 향기</div>
          <Select className="infoedit5_input notoMid fs-14" ref={accordClassRef}>
            <option value="default" disabled>좋아하는 향을 선택해주세요</option>
            {likeScent.map((item) => (
              <option key={uuid()} value={item.value}>
                {item.name}
              </option>
            ))}
          </Select>
        </div>
        <div id="infoedit6" className="infoedit6 flex justify-center">
          <button
            className="infoedit6_btn notoBold fs-18"
            type="button"
            onClick={completeEdit}
          >
            수정 완료
          </button>
          {/* <div className="infoedit6_drop notnoMid fs-12">
            <Link to="/drop">탈퇴하기</Link>
          </div> */}
        </div>
      </div>
    </div>
  );
}
export default InfoEdit;

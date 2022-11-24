import React, { useEffect, useState } from "react";
import styled from 'styled-components';
import { Link } from "react-router-dom";
import dummyImg from "@images/icon/perfumeImg.svg";
import CarouselSlider from 'react-carousel-slider';
import "./TasteAnalysis.scss";
import "./Pie Chart";
import PieChart from "./Pie Chart";
import { fetchAccordClassListUser, fetchHaveListUser, fetchWishListUser, fetchHaveDelete, fetchWishDelete, fetchWishToHave, fetchRecomSVD} from "../../apis/perfumeAPI";
import { userProfileState, userState } from "../../atom";
import { useRecoilState } from "recoil";
import { Button, IconButton } from '@mui/material';
import { Delete, Send } from "@material-ui/icons";
import history from "../../utils/history";
import SendIcon from '@mui/icons-material/Send';

const DivCenter = styled.div`
margin: 30px 0px 30px 0px;
text-align: center;
`;

const DivGap = styled.div`
margin: 30px 0px 30px -30px;
text-align: center;
`
const SpanCenter = styled.span`
text-align: center;
`
const DivInline = styled.div`
display: inline  ;
`;

const sliderBoxStyle = {
  height: "400px",
  width: "80%",
  background: "transparent",
  border: "1px solid #e1e4e8"
};

const itemsStyle = {
  width: "200px",
  height: "240px",
  padding: "5px",
  background: "transparent",
  border: "1px solid #e1e4e8",
  borderRadius: "2px",
  };
  

const buttonSetting = {
  placeOn: "middle-inside",
  style: {
      left: {
      color: "#cccccc",
      background: "transparent",
      border: "1px solid #e1e4e8",
      borderRadius: "50%"
      },
      right: {
      color: "#cccccc",
      background: "transparent",
      border: "1px solid #e1e4e8",
      borderRadius: "50%"
      }
  }
  };

function TasteAnalysis() {

  // 현재 로그인 유저 정보
  const [user, setUser] = useRecoilState(userState);
  const userProfile = useRecoilState(userProfileState);
  //위시리스트 가져오기
  const [wishList, setWishList] = useState([]);
  //보유리스트 가져오기
  const [haveList, setHaveList] = useState([]);
  //향 순위 분석 가져오기
  const [accordClassList, setAccordClassList ] = useState([]);
  const [recomSVDList, setRecomSVDList ] = useState([]);
  const wishData = wishList;
  let wishBody;
  const haveData = haveList;
  let haveBody;
  const accordClassData = accordClassList;
  const recomSVDData = recomSVDList;
  const nickname = userProfile[0][0]?.nickname;

  let pieBody;

  if(accordClassData.length===0){
    pieBody = <DivCenter><h1 className="tasteAnalysis_emptyList_title fs-36">
    데이터가 없어요...
  </h1></DivCenter>
  }else{
    let sum = 0;

    for(let i = 0; i < accordClassData?.length; i++){
      accordClassData[i].y = accordClassData[i].accordClassCount;
      delete accordClassData[i].accordClassIdx;
      delete accordClassData[i].accordClassCount;
      sum += accordClassData[i].y;
    }
    for(let i = 0; i < accordClassData?.length; i++){
      accordClassData[i].y = Math.round(((accordClassData[i].y/sum)*100 + Number.EPSILON) * 100) / 100;
    }
    console.log("돌아감?")
    pieBody = <PieChart data={accordClassData}/>
  }

  
  // console.log("위시");
  // console.log(wishData);
  // console.log("해브");
  // console.log(haveData);
  // console.log(accordClassData);
  // console.log(userProfile[0][0].nickname);
  // console.log(recomSVDData);
  // 클릭시 삭

  function handleWishDelete(idx, e){
    e.preventDefault();
    console.log(idx);
    fetchWishDelete(user.sToken, idx)
    window.location.replace("/tasteanalysis")
  }  
  
  function handleHaveDelete(idx, e){
    e.preventDefault();
    console.log(idx);
    fetchHaveDelete(user.sToken, idx)
    window.location.replace("/tasteanalysis")
  }  

  function handleWishToHave(idx, e){
    e.preventDefault();
    console.log(idx);
    fetchWishToHave(user.sToken, idx)
    window.location.replace("/tasteanalysis")
  }

  const customSlideWishCpnts = 
  wishData?.map((item) => (<>
  <Link to={"/detail/" + item.perfumeIdx} key={item.perfumeIdx}>
      <img src = {item.perfumeImg}/>
    </Link>
    <br></br>
    <SpanCenter>
    <Button variant="contained" style={{backgroundColor : "black", color:"white", fontFamily : "NotoSansRegular", marginRight : "2px"}} size="small" onClick={(e) => {handleWishToHave(item.idx, e)}}>
      {/*<SendIcon/>*/}
      보유리스트로</Button>
    <Button variant="outlined" style={{background:"#ff1744", color:"white", fontFamily : "NotoSansRegular"}} size="small" onClick={(e) => {handleWishDelete(item.idx, e)}} startIcon={<Delete />}>
      삭제
      </Button>
    </SpanCenter>
  </>
  ));


  const customSlideHaveCpnts = 
    haveData?.map((item) => (<>
    <Link to={"/detail/" + item.perfumeIdx} key={item.perfumeIdx}>
        <img src = {item.perfumeImg}/>
      </Link>
      <br></br>
      <SpanCenter>
      <Button variant="outlined" style={{background:"#ff1744",  color:"white", fontFamily : "NotoSansRegular"}} size="small" onClick={(e) => {handleHaveDelete(item.idx, e)}} startIcon={<Delete />}>
        삭제
        </Button>
      </SpanCenter>
      
    </>
    ));

    const customSlideRecomSVDCpnts = 
    recomSVDData.map((item) => (
    <Link to={"/detail/" + item.idx} key={item.idx}>
        <img src = {item.perfume_img}/>
    </Link>
    ));


    useEffect(() => {
      fetchHaveListUser(user.sToken)
      .then((res) => {res.json().then((res) => {
        setHaveList(res.haveList)
      })})
    },[]);

    useEffect(() => {
      fetchWishListUser(user.sToken)
      .then((res) => {res.json().then((res) => {
        setWishList(res.wishList)
      })})
    },[]);

    useEffect(() => {
      fetchAccordClassListUser(user.sToken)
      .then((res) => {res.json().then((res) => {
        setAccordClassList(res.accordClassList)
      })})
    });

    useEffect(() => {
      fetchRecomSVD(userProfile[0][0]?.idx)
      .then((res) => {res.json().then((res) => {
        setRecomSVDList(res);
      })})
    },[]);

    if(wishData?.length===0){
      wishBody = <><h1 className="tasteAnalysis_emptyList_title fs-36">
      위시리스트가 비었어요!
    </h1>
    <div className="tasteAnalysis_emptyList_btns flex">
      <Link to="/perfumesearch">
      <button
        type="button"
        className="tasteAnalysis_emptyList_btns_search notoBold fs-20"
      >
        향수 찾아보기
      </button>
      </Link>
    </div></>
    }else{
      wishBody = 
      <CarouselSlider
        slideCpnts={customSlideWishCpnts}
        manner={{ circular: true }}
        dotsSetting={{ style: {
          dotSize: "5px",
          currDotColor: "rgba(0, 0, 0, 0.5)",
          marginTop: "2px"
      }}}
        sliderBoxStyle={sliderBoxStyle}
        buttonSetting={buttonSetting}
        itemsStyle={itemsStyle}        
      ></CarouselSlider>
    }

    if(haveData?.length===0){
      haveBody = <><h1 className="tasteAnalysis_emptyList_title fs-36">
      보유한 향수가 없어요...
    </h1></>
    }else{
      haveBody = 
      <CarouselSlider
        slideCpnts={customSlideHaveCpnts}
        manner={{ circular: true }}
        dotsSetting={{ style: {
          dotSize: "5px",
          currDotColor: "rgba(0, 0, 0, 0.5)",
          marginTop: "2px"
      }}}
        sliderBoxStyle={sliderBoxStyle}
        buttonSetting={buttonSetting}
        itemsStyle={itemsStyle}        
      ></CarouselSlider>
    }




    let recomBody;
    if(recomSVDData?.length===0){
      recomBody = <h1 className="tasteAnalysis_emptyList_title fs-36">
      데이터가 없어요...
    </h1>
    }else{
      recomBody =
      <CarouselSlider
        slideCpnts={customSlideRecomSVDCpnts}
        manner={{ circular: true }}
        dotsSetting={{ style: {
          dotSize: "5px",
          currDotColor: "rgba(0, 0, 0, 0.5)",
          marginTop: "2px"
      }}}
        sliderBoxStyle={sliderBoxStyle}
        buttonSetting={buttonSetting}
        itemsStyle={itemsStyle}        
      ></CarouselSlider>

    }

      return (<>
        <DivCenter className="container flex">
          <DivCenter id="tasteAnalysis" className="tasteAnalysis flex">
            <DivCenter className="tasteAnalysis_wishList flex">
              <DivCenter className="tasteAnalysis_wishList_title notoBold fs-36">
                위시리스트
              </DivCenter>
              {wishBody}
            </DivCenter>
            <DivCenter className="tasteAnalysis_wishList flex">
              <DivCenter className="tasteAnalysis_wishList_title notoBold fs-36">
                보유리스트
              </DivCenter>
              {haveBody}
            </DivCenter>
            <DivCenter className="tasteAnalysis_wishList_title notoBold fs-36">
                {nickname} 님의 향 선호 분포
            </DivCenter>
          </DivCenter>
        </DivCenter>
        {pieBody}
        <DivCenter className="container flex">
          <DivCenter id="tasteAnalysis" className="tasteAnalysis flex">
          <DivCenter className="tasteAnalysis_wishList_title notoBold fs-36">
                {nickname} 님과 같은 취향의 향수
            </DivCenter>
            {recomBody}
          </DivCenter>
        </DivCenter>
        </>
        );
}
export default TasteAnalysis;

import React from "react";
import dummyImg from "@images/icon/perfumeImg.svg";
import Button from '@mui/material/Button';
import { useState } from "react";
import "./PerfumeSearch.scss";
import SearchIcon from '@mui/icons-material/Search';
import { fetchSearchPerfume } from "../../apis/perfumeAPI";
import InfiniteScroll from 'react-infinite-scroll-component'
import LinearProgress from '@mui/material/LinearProgress';
import Box from '@mui/material/Box';
import Typography from "@mui/material/Typography";
import CircularProgress from '@mui/material/CircularProgress';
import { useNavigate } from "react-router-dom";
import CommonCard from "../../components/common/CommonCard";


function PerfumeSearch() {
  const [isActive, setIsActive] = useState([true, true, true, true])
  const [isDurationActive, setIsDurationActive] = useState([true, true, true, true, true, true])
  const [isAccordClassActive, setIsAccordClassActive] = useState([true, true, true, true, true, true, true, true, true])
  let payload = {
    gender : [],
    duration : [],
    accordClass : [],
    lastIdx : null,
    pageSize : 3
  }
  const durtaionList = ["전체","매우 강함","강함","적당함","약함","매우 약함"]
  const accordClassList = ["전체","매운 향","톡쏘는 향","야성적인 향","인공적인 향","꽃 향기","풀 향기","과일 향","달콤한 향"]
  const [searchResult, setSearchResult] = useState({
    isSearched : true,
    searchList : []
  })
  const [morePost, setMorePost] = useState(false)
  const navigate = useNavigate();

  console.log(isAccordClassActive)
  const handleClickGender = (index) => {
    const temp = [...isActive]
    if(index === 0){
      if(isActive[0] === true){
        for(let i=0; i<isActive.length; i++){
          temp[i] = false
          setIsActive(temp)
        }
      }else{
        for(let i=0; i<isActive.length; i++){
          temp[i] = true
          setIsActive(temp)
        }
      }
    }else{
      temp[index] = !temp[index]
      setIsActive(temp)
    }
    for(let i=0; i<isActive.length; i++){
      if(temp[i] === true){
        temp[0] = true
        setIsActive(temp)
      }
    }
    console.log(temp)
  }
  const handleClickDuration = (index) => {
    const temp2 = [...isDurationActive]
    if(index === 0){
      if(isDurationActive[0] === true){
        for(let i=0; i<isDurationActive.length; i++){
          temp2[i] = false
          setIsDurationActive(temp2)
      }
      }else{
        for(let i=0; i<isDurationActive.length; i++){
          temp2[i] = true
          setIsDurationActive(temp2)
        }
      }
    }else{
      temp2[index] = !temp2[index]
      setIsDurationActive(temp2)
    }
    for(let i=0; i<isDurationActive.length; i++){
      if(temp2[i] === true){
        temp2[0] = true
        setIsDurationActive(temp2)
      }
    }
  }
  const handleClickAccordClass = (index) => {
    const temp3 = [...isAccordClassActive]
    console.log(index)
    if(index === 0){
      if(isAccordClassActive[0] === true){
        for(let i=0; i<isAccordClassActive.length; i++){
          temp3[i] = false
          setIsAccordClassActive(temp3)
        }
      }else{
        for(let i=0; i<isAccordClassActive.length; i++){
          temp3[i] = true
          setIsAccordClassActive(temp3)
        }
      }
    }else{
      temp3[index] = !temp3[index]
      setIsAccordClassActive(temp3)
    }
    for(let i=0; i<isAccordClassActive.length; i++){
      if(temp3[i] === true){
        temp3[0] = true
        setIsAccordClassActive(temp3)
      }
    }
  }

  const genderList = [
    "전체",
    "남자",
    "중성",
    "여자"
  ]
  const durationList1 = [
    "전체",
    "매우 강함",
    "강함",
  ]

  const durationList2 = [
    "적당함",
    "약함",
    "매우 약함"
  ]
  const accordClassList1 = [
    "전체",
    "매운 향",
    "톡쏘는 향",
    "야성적인 향",
  ]

  const accordClassList2 = [
    "인공적인 향",
    "꽃 향기",
    "풀 향기",
    "과일 향",
    "달콤한 향",
  ]

  const searchPerfume = () => {
    for(let i=1; i<isActive.length; i++){
      if(isActive[i] === false){
        payload.gender.push(genderList[i])
      }
    }
    for(let j=1; j<isDurationActive.length; j++){
      if(isDurationActive[j] === false){
        payload.duration.push(durtaionList[j])
      }
    }
    for(let k=0; k<isAccordClassActive.length; k++){
      if(isAccordClassActive[k] === false){
        payload.accordClass.push(accordClassList[k])
      }
    }
    console.log(payload)
    fetchSearchPerfume(payload)
      .then((res) => {res.json().then((res) => {
        console.log(res)
        if(res.isSearched){
          setSearchResult({isSearched : res.isSearched,searchList :res.searchList})
          setMorePost(res.hasNext)
        }else{
          setSearchResult({isSearched : res.isSearched,searchList :res.searchList})
          setMorePost(false)
        }
      })})
  }
  const searchNextPerfume = () => {
    for(let i=1; i<isActive.length; i++){
      if(isActive[i] === false){
        payload.gender.push(genderList[i])
      }
    }
    for(let j=1; j<isDurationActive.length; j++){
      if(isDurationActive[j] === false){
        payload.duration.push(durtaionList[j])
      }
    }
    for(let k=0; k<isAccordClassActive.length; k++){
      if(isAccordClassActive[k] === false){
        payload.accordClass.push(accordClassList[k])
      }
    }
    payload.lastIdx = searchResult.searchList[searchResult.searchList.length - 1].idx
    console.log(payload)
    if(morePost){
      fetchSearchPerfume(payload)
        .then((res) => {res.json().then((res) => {
          console.log(res)
          if(res.isSearched){
            setSearchResult({isSearched: res.isSearched, searchList : searchResult.searchList.concat(res.searchList)})
            setMorePost(res.hasNext)
          }else if(!res.isSearched && res.searchList.length >= 1){
            // setSearchResult({isSearched: res.isSearched, searchList: res.searchList})
            setMorePost(false)
          }else{
            setSearchResult({isSearched: res.isSearched, searchList: res.searchList})
            setMorePost(false)
          }
        })})
    }
  }

  return (
    <div className="container flex">
      <div  id="perfumeSearch" className="perfumeSearch flex">
        <div style={{ fontFamily : "SUIT Variable", fontWeight : "bold" }}  className="perfumeSearch_title flex notoBold fs-36">
          맞춤 향수 검색
        </div>
        <div  className="perfumeSearch_gender flex">
          <div style={{ fontFamily : "SUIT Variable", fontWeight : "bold"}} className="perfumeSearch_gender_title notoBold fs-32">
            Gender
          </div>
          <div className="perfumeSearch_gender_btns flex">
            {genderList.map((gender,index) => (
              <Button
                key={index}
                style ={{
                  backgroundColor : isActive[index] ?  'gainsboro' : '#bbdefb',
                  color : 'black',
                  fontFamily : "SUIT Variable"
                }}
                variant="contained"
                onClick={() => {
                  handleClickGender(index)
                }}
              >
                {gender}
              </Button>
            ))

            }
          </div>
        </div>
        <div className="divide" />
        <div className="perfumeSearch_duration flex">
          <div style={{ fontFamily : "SUIT Variable", fontWeight : "bold" }} className="perfumeSearch_duration_title fs-32">
            Duration
          </div>
          <div className="perfumeSearch_duration_btns1 flex">
            {
              durationList1.map((duration,index) => (
                <Button
                  key={index}
                  style={{
                    backgroundColor : isDurationActive[index] ?  'gainsboro' : '#bbdefb',
                    color : 'black',
                    fontSize : 18,
                    fontFamily : "SUIT Variable"
                  }}
                  variant="contained"
                  onClick={() => {
                    handleClickDuration(index)
                  }}
                >
                  {duration}
                </Button>
              ))
            }
          </div>
          <div className="perfumeSearch_duration_btns2 flex">
            {
              durationList2.map((duration,index) => (
                <Button
                  key={index+3}
                  style={{
                    backgroundColor : isDurationActive[index+3] ?  'gainsboro' : '#bbdefb',
                    color : 'black',
                    fontFamily : "SUIT Variable"
                  }}
                  variant="contained"
                  onClick={() => {
                    handleClickDuration(index+3)
                  }}
                >
                  {duration}
                </Button>
              ))
            }
          </div>
        </div>
        <div className="divide" />
        <div className="perfumeSearch_duration flex">
          <div style={{ fontFamily : "SUIT Variable", fontWeight : "bold" }} className="perfumeSearch_duration_title notoBold fs-32">
            AccordClass
          </div>
          <div className="perfumeSearch_duration_btns1 flex">
            {
              accordClassList1.map((accordClass, index) => (
                <Button
                  key = {index}
                  style = {{
                    backgroundColor : isAccordClassActive[index] ? 'gainsboro' : '#bbdefb',
                    color : 'black',
                    fontFamily : "SUIT Variable"
                  }}
                  onClick={() => {
                    handleClickAccordClass(index)
                  }}
                >
                  {accordClass}
                </Button>
              ))
            }
          </div>
          <div className="perfumeSearch_duration_btns2 flex">
            {
              accordClassList2.map((accordClass, index) => (
                <Button
                  key={index+4}
                  style = {{
                    backgroundColor : isAccordClassActive[index+4] ? 'gainsboro' : '#bbdefb',
                    color : 'black',
                    fontFamily : "SUIT Variable",
                  }}
                  onClick={() => {
                    handleClickAccordClass(index+4)
                  }}
                >
                  {accordClass}
                </Button>
              ))
            }
          </div>
        </div>
        <div className="divide" />
          <Button onClick={() => {
            searchPerfume()
          }}
                  style={{ backgroundColor : "black" }}
                  sx={{ fontFamily : "NotoSansBold",
                    borderWidth : "2px",
                    marginBottom : "15px" }}
                  size="large"
                  variant="contained"
                  startIcon={<SearchIcon/>}>
            향수 검색
          </Button>
      </div>
      <div id="perfumeResult" className="perfumeResult flex">
        <InfiniteScroll
          id="perfumeResult" className="perfumeResult flex"
          dataLength={searchResult.searchList.length}
          next={() => {
            searchNextPerfume()
          }}
          hasMore={morePost}
          loader={
            <Box sx={{ width : "100%" ,marginBottom : "5px"}}>
              <LinearProgress/>
            </Box>
          }
        >
          { searchResult.isSearched ? (
              searchResult.searchList.map((result, index) => (
                // <div
                //   className="perfumeResult_img"
                //   key={index}
                //   onClick={() => {
                //   navigate(`/detail/${result.idx}`)
                // }}
                // >
                //   <img src={result.perfumeImg} alt="더미이미지" />
                // </div>
                <div
                  key={index}
                  onClick={() => {
                    navigate(`/detail/${result.idx}`)
                  }}
                >
                  <CommonCard
                    img={result.perfumeImg}
                    title={result.perfumeName}
                    author={result.brandName}
                    description={result.scent}
                  />
                </div>
              ))
          )  :  (
            <>
              <Box sx={{ width : "100%", display : "flex", justifyContent : "center", alignItems : "center" }} >
                <Typography style={{ fontFamily : "NotoSansBold" }} mt={3} variant="h4" component="h3">
                  검색결과가 없어요😥 이런 향수는 어떠세요??
                </Typography>
              </Box>
                {searchResult.searchList.map((result, index) => (
                // <div
                //   className="perfumeResult_img"
                //   key={index}
                // >
                //   <img src={result.perfumeImg} alt="더미이미지" />
                // </div>
                  <div
                    key={index}
                    onClick={() => {
                      navigate(`/detail/${result.idx}`)
                    }}
                  >
                    <CommonCard
                      img={result.perfumeImg}
                      title={result.perfumeName}
                      author={result.brandName}
                      description={result.scent}
                    />
                  </div>
            ))}
            </>
          )}
        </InfiniteScroll>
        {/*<div className="perfumeResult_img">*/}
        {/*  <img src={dummyImg} alt="더미이미지" />*/}
        {/*</div>*/}
        {/*<div className="perfumeResult_img">*/}
        {/*  <img src={dummyImg} alt="더미이미지" />*/}
        {/*</div>*/}
        {/*<div className="perfumeResult_img">*/}
        {/*  <img src={dummyImg} alt="더미이미지" />*/}
        {/*</div>*/}
        {/*<div className="perfumeResult_img">*/}
        {/*  <img src={dummyImg} alt="더미이미지" />*/}
        {/*</div>*/}
        {/*<div className="perfumeResult_img">*/}
        {/*  <img src={dummyImg} alt="더미이미지" />*/}
        {/*</div>*/}
        {/*<div className="perfumeResult_img">*/}
        {/*  <img src={dummyImg} alt="더미이미지" />*/}
        {/*</div>*/}
      </div>
    </div>
  );
}
export default PerfumeSearch;

import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { Link } from "react-router-dom";
import Carousel from 'react-material-ui-carousel';
import CarouselSlider from 'react-carousel-slider';
import ImgItem from '../components/items/ImgItem';
import { fetchMainPerfume } from "../apis/perfumeAPI";
import { fetchMainPerfumeUser } from "../apis/perfumeAPI";
import { useRecoilState } from "recoil";
import { userState } from "../atom";
import CommonCardMain from "../components/common/CommonCardMain";

    const ContentWrapper = styled.div`
        display: block;
        height: auto;
        margin-top: 200px;
        margin-bottom: 200px;
    `
    const DivCenter = styled.div`
        margin: 30px 0px 30px 0px;
        text-align: center;
        height: auto;
    `;

    const SpanCenter = styled.span`
        font-family: SUIT Variable;
    display: inline-block;
    width:90%;
    text-align: center;
        margin-top: 5px;
        font-size: 24px;    
        height: auto;
    `

    const SubContent = styled.p`
        font-weight: bold;
    font-family: SUIT Variable;
    text-align: center;
    margin: 15px 15px 0px 0px;
    font-size: 60px;
    height: auto;
`;

function Home() {

    const Wrapper = styled.div`
        display: block;
        height: auto;
    `

    // 오늘의 향수 추천
    const [todayPerfume, setTodayPerfume] = useState([{perfumeImg:"https://fimgs.net/mdimg/perfume/375x500.19642.jpg"},{perfumeImg:"https://fimgs.net/mdimg/perfume/375x500.19642.jpg"},{perfumeImg:"https://fimgs.net/mdimg/perfume/375x500.19642.jpg"}, {perfumeImg:"https://fimgs.net/mdimg/perfume/375x500.19642.jpg"},{perfumeImg:"https://fimgs.net/mdimg/perfume/375x500.19642.jpg"},{perfumeImg:"https://fimgs.net/mdimg/perfume/375x500.19642.jpg"}]);
    // 베스트 향수 추천
    const [bestPerfume, setBestPerfume] = useState([{perfumeImg:"https://fimgs.net/mdimg/perfume/375x500.19642.jpg"},{perfumeImg:"https://fimgs.net/mdimg/perfume/375x500.19642.jpg"},{perfumeImg:"https://fimgs.net/mdimg/perfume/375x500.19642.jpg"}, {perfumeImg:"https://fimgs.net/mdimg/perfume/375x500.19642.jpg"},{perfumeImg:"https://fimgs.net/mdimg/perfume/375x500.19642.jpg"},{perfumeImg:"https://fimgs.net/mdimg/perfume/375x500.19642.jpg"}]);
    // 현재 로그인 유저 정보
    const [user, setUser] = useRecoilState(userState);
    // 상단 캐러셀 인덱스
    const [cindex, setCindex] = useState(0);
    //상단 캐러셀 아이템
    const items = [
        {
            idx : 0,
            name: "first",
            src: require("../assets/images/carousel/10.jpg"),
        },
        {
            idx: 1,
            name: "second",
            src: require("../assets/images/carousel/22.jpg"),
        },
        {
            idx: 2,
            name: "third",
            src: require("../assets/images/carousel/21.jpg"),
        },
    ]

    //향수추천 향분석 향수찾기
    const links = ["personal", "tasteanalysis", "perfumesearch" ];
    // BEST 향수 추천 리스트 나중에 리팩토링



    console.log(bestPerfume);
    console.log(todayPerfume);
    
    const bestData = ([
        {
            index: 0,
            imgSrc: `${bestPerfume[0].perfumeImg}`,
            idx: `${bestPerfume[0].idx}`
        },
        {
            index: 1,
            imgSrc: `${bestPerfume[1].perfumeImg}`,
            idx: `${bestPerfume[1].idx}`
        },
        {
            index: 2,
            imgSrc: `${bestPerfume[2].perfumeImg}`,
            idx: `${bestPerfume[2].idx}`
        },
        {
            index: 3,
            imgSrc: `${bestPerfume[3].perfumeImg}`,
            idx: `${bestPerfume[3].idx}`
        },
        {
            index: 4,
            imgSrc: `${bestPerfume[4].perfumeImg}`,
            idx: `${bestPerfume[4].idx}`
        },
        {
            index: 5,
            imgSrc: `${bestPerfume[5].perfumeImg}`,
            idx: `${bestPerfume[5].idx}`
        },
        
    ])
    // 오늘의 향수 추천 리스트 나중에 리팩토링
    const todayData = ([
        {
            index: 0,
            imgSrc: `${todayPerfume[0].perfumeImg}`,
            idx: `${todayPerfume[0].idx}`
        },
        {
            index: 1,
            imgSrc: `${todayPerfume[1].perfumeImg}`,
            idx: `${todayPerfume[1].idx}`
        },
        {
            index: 2,
            imgSrc: `${todayPerfume[2].perfumeImg}`,
            idx: `${todayPerfume[2].idx}`
        },
        {
            index: 3,
            imgSrc: `${todayPerfume[3].perfumeImg}`,
            idx: `${todayPerfume[3].idx}`
        },
        {
            index: 4,
            imgSrc: `${todayPerfume[4].perfumeImg}`,
            idx: `${todayPerfume[4].idx}`
        },
        {
            index: 5,
            imgSrc: `${todayPerfume[5].perfumeImg}`,
            idx: `${todayPerfume[5].idx}`
        },
        
    ])
    // 받아온 데이터로 캐러셀 동적 생성
    const customSlideCpnts =
        todayData.map((item) => (
            <Link to={"/detail/" + item.idx} key={item.idx}>
                <CommonCardMain img={item.imgSrc} 
                description={todayPerfume[item.index].perfumeName}></CommonCardMain>
                <SpanCenter>{todayPerfume[item.index].scent}</SpanCenter>
                
            </Link>
        ));
    const customSlideCpnts2 = 
        bestData.map((item) => (
            <Link to={"/detail/" + item.idx} key={item.idx}>
            <CommonCardMain img={item.imgSrc} 
            description={bestPerfume[item.index].perfumeName}></CommonCardMain>
            <SpanCenter>{bestPerfume[item.index].scent}</SpanCenter>
            
        </Link>
    ));

    // 로그인 여부에 따른 api 호출
    useEffect(() => {
        if(user.isLogin){
            fetchMainPerfumeUser(user.sToken)
            .then((res) => {res.json().then((res) => {
                setBestPerfume(res.BestPerfumeList)
                setTodayPerfume(res.todayPerfumeList)
            })})
        }else{
            fetchMainPerfume()
            .then((res) => {res.json().then((res) => {
                setBestPerfume(res.BestPerfumeList)
                setTodayPerfume(res.todayPerfumeList)
            })})
        }
        },[]);

    const sliderBoxStyle = {
        height: "750px",
        width: "80%",
        background: "transparent",
        border: "1px solid #e1e4e8",
    };
    
    const itemsStyle = {
        width: "400px",
        height: "580px",
        padding: "2px",
        background: "transparent",
        border: "1px solid #e1e4e8",
        borderRadius: "2px",
        };
        

    const buttonSetting = {
        placeOn: "middle-inside",
        style: {
            left: {
            color: "#929393",
            background: "transparent",
            border: "1px solid #e1e4e8",
            borderRadius: "50%"
            },
            right: {
            color: "#929393",
            background: "transparent",
            border: "1px solid #e1e4e8",
            borderRadius: "50%"
            }
        }
        };
    
        return (
            <><Wrapper>
                <Carousel
                    index={cindex}
                    interval={4000}
                    autoPlay
                    animation="fade"
                    indicators={false}
                    stopAutoPlayOnHover
                    swipe
                    className="main-carousel"
                >
                    {items.map((item, i) => (
                        (!((user.isLogin === null)&&(i===1))) ? 
                        <Link to={"/" + links[i] } key={item.idx}>
                        <ImgItem key={i} item={item} />
                        </Link>
                        :
                        <ImgItem key={i} item={item} />
                    ))}
                </Carousel>
            </Wrapper>
            <Wrapper>
                <ContentWrapper>
                    <DivCenter>
                    <SubContent>오늘의 향수</SubContent></DivCenter>
                    <CarouselSlider 
                        slideCpnts={customSlideCpnts}
                        manner={{ circular: true }}
                        dotsSetting={{ style: {
                            dotSize: "10px",
                            currDotColor: "rgba(0, 0, 0, 0.5)",
                            marginTop: "30px"
                        }}}
                        sliderBoxStyle={sliderBoxStyle}
                        buttonSetting={buttonSetting}
                        itemsStyle={itemsStyle}></CarouselSlider>        
                </ContentWrapper>
                <ContentWrapper>
                <DivCenter>
                    <SubContent>BEST 향수</SubContent></DivCenter>
                <CarouselSlider 
                        slideCpnts={customSlideCpnts2}
                        manner={{ circular: true }}
                        dotsSetting={{ style: {
                            dotSize: "10px",
                            currDotColor: "rgba(0, 0, 0, 0.5)",
                            marginTop: "30px"
                        }}}
                        sliderBoxStyle={sliderBoxStyle}
                        buttonSetting={buttonSetting}
                        itemsStyle={itemsStyle}
                    ></CarouselSlider>   
                </ContentWrapper>
            </Wrapper></>
        )
}

export default Home;
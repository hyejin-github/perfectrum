import React, { useState, useEffect, useRef } from "react";
import uuid from "react-uuid";
import { useNavigate, useParams } from "react-router-dom";
import { useRecoilValue } from 'recoil';
import { userProfileState, userState } from "../../atom";
// import dummyImg from "@images/icon/perfumeImg.svg";
import favorite from "@images/icon/favorite_black(2).svg";
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import dummyProfile from "@images/icon/dummyIcon.png";
// import InfoReview from "@components/user/InfoReview";
import PerfumeReview from "@components/user/PerfumeReview";
import { getDetail } from "../../apis/perfume";
import { storage } from "../../firebase";
import "./PerfumeDetail.scss";
import axios from "axios";
import { fetchRecommendCos } from "../../apis/perfumeAPI";
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import AddAPhotoIcon from '@mui/icons-material/AddAPhoto';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Rating from '@mui/material/Rating';
import Slider from 'react-slick'
import "slick-carousel/slick/slick.css"
import "slick-carousel/slick/slick-theme.css"
import { wishPerfume, havePerfume } from "../../apis/perfumeAPI";
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import FavoriteIcon from '@mui/icons-material/Favorite';
import ShoppingCartOutlinedIcon from '@mui/icons-material/ShoppingCartOutlined';


const CommonCard = (props) => {
  return(
    <div style={{ width : "15em", display : "flex" }} className="card">
      <img style={{ width : "50%", height: "50%" }} src={props.img} />
      <div className="card-body">
        <h2>{props.title}</h2>
        <p>{props.description}</p>
        <h5>{props.author}</h5>
      </div>
    </div>
  )
}


function PerfumeDetail() {
  const [position, setPosition] = useState(0)
  const [total, setTotal] = useState(null);
  const [long, setLong] = useState(null);
  const [sil, setSil] = useState(null);
  const user = useRecoilValue(userState);
  const userProfile = useRecoilValue(userProfileState);
  const [image, setImage] = useState({
    image_file: "",
    preview_URL: "",
  });
  // console.log(image);
  const [imageUrl, setImageUrl] = useState("");
  const navigate = useNavigate();
  const reviewRef = useRef();
  const inputRef = useRef();
  const { id } = useParams();
  const move = () => {
    navigate(`/detail/${id}`);
    window.location.reload();
  };
  const [flip, setFlip] = useState("평점순");
  const [perfumeDetail, setPerfumeDetail] = useState({});
  const [getReviewList, setGetReviewList] = useState([]);
  const [recommendPerfume, setRecommendPerfume] = useState([]);
  const [isClicked, setIsClicked] = useState(null)
  console.log(isClicked)
  const detail = () => {
    if(user.sToken){
      axios({
        method: "post",
        url: `http://j7c105.p.ssafy.io:8083/detail/${id}`,
        headers : {
          "Authorization" : `${user.sToken}`
        },
        data: {
          "type": flip,
          "lastIdx": null,
          "lastTatalScore": null,
          "lastLikeCount": null,
          "pageSize": 4
        }
      })
        .then((res) => {
          console.log(res);
          setPerfumeDetail(res.data.perfume);
          setGetReviewList(res.data.reviewList);
          setIsClicked(res.data.isClicked)
        })
        .catch((err) => console.log(err))
    }else{
      axios({
        method: "post",
        url: `http://j7c105.p.ssafy.io:8083/detail/${id}`,
        data: {
          "type": flip,
          "lastIdx": null,
          "lastTatalScore": null,
          "lastLikeCount": null,
          "pageSize": 4
        }
      })
      .then((res) => {
        console.log(res);
        setPerfumeDetail(res.data.perfume);
        setGetReviewList(res.data.reviewList);
        setIsClicked(res.data.isClicked)
      })
      .catch((err) => console.log(err))
    }
  };

  // function onScroll(){
  //   setPosition(window.scrollY)
  // }
  // useEffect(() => {
  //   window.addEventListener("scroll", onScroll);
  //   return () => {
  //     window.removeEventListener("scroll", onScroll)
  //   }
  // }, [])

  useEffect(() => {
    fetchRecommendCos(id)
      .then((res) => {res.json().then((res) => {
        setRecommendPerfume(res)
        console.log(res)
      })})
  },[])
  // console.log(recommendPerfume)


  // console.log(perfumeDetail);
  // console.log(getReviewList);
  useEffect(() => {
    detail();
  }, [flip]);
  const perfumeReviewList = getReviewList?.map((item) => (<PerfumeReview key={uuid()} item={item}/>))
  const newclick = () => {
    setFlip("최신순");
  }
  const scoreclick = () => {
    setFlip("평점순");
  }

  const settings = {
    dots: true,
    infinite: true,
    slidesToShow: 3,
    slidesToScroll: 1,
    autoplay: true,
    speed: 2000,
    autoplaySpeed: 2000,
    cssEase: "linear"
  };

  const uploadImg = (e) => {
    e.preventDefault();
    inputRef.current.click();
  };

  const handleUpload = (e) => {
    e.preventDefault();
    // console.log(e.target.files[0]);
    // console.log(perfumeDetail.perfumeImg);
    if (e.target.files[0]) {
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
      const storageRef = storage.ref("detail/test/")
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
            console.log(downloadURL);
            // const copy = JSON.parse(JSON.stringify(userProfile));
            // copy[0].profileImg = downloadURL;
            // setUserProfile(copy)
          });
        }
      )
    }
  };
  const uploadReview = () => {
    if (window.confirm("리뷰를 등록하시겠습니까?")) {
      if (imageUrl === "") {
        axios({
          method: "post",
          url: `http://j7c105.p.ssafy.io:8083/detail/${id}/review`,
          headers: {
            Authorization: user.sToken
          },
          data: {
            "reviewImg": perfumeDetail.perfumeImg,
            "totalScore": total,
            "longevity": long,
            "sillageScore": sil,
            "content" : reviewRef.current.value
          }
        })
        .then(res => console.log("엑시오스", res))
        .catch(err => console.log(err))
      } else {
        axios({
          method: "post",
          url: `http://j7c105.p.ssafy.io:8083/detail/${id}/review`,
          headers: {
            Authorization: user.sToken
          },
          data: {
            "reviewImg": imageUrl,
            "totalScore": total,
            "longevity": long,
            "sillageScore": sil,
            "content" : reviewRef.current.value
          }
        })
        .then(res => console.log("엑시오스", res))
        .catch(err => console.log(err))
      }
      move();
    }
  };



  return (
    <div className="container flex justify-center">
      <div id="perfumeDetail" className="perfumeDetail">
        <div id="perfumeDetail1" className="perfumeDetail1 flex">
          <div className="perfumeDetail1_img">
            <img src={perfumeDetail.perfumeImg} alt="Perfume_Img" />
          </div>
          <div className="perfumeDetail1_contents flex justify-center align-center kyobo fs-22">
            <p>{perfumeDetail.description}</p>
          </div>
        </div>
        <div id="perfumeDetail2" className="perfumeDetail2 flex">
          <div className="perfumeDetail2_title flex">
            <div className="perfumeDetail2_title_name fs-24">
              {perfumeDetail.perfumeName}
            </div>
            <div className="perfumeDetail2_title_count flex">
              <div className="perfumeDetail2_title_count_like flex">
                <button className="perfumeDetail2_title_count_like_img"
                        type="button"
                        onClick={() => {
                          if(isClicked === "have"){
                            alert("이미 보유한 향수입니다.")
                          }else{
                            wishPerfume(user.sToken, id)
                              .then((res) => {res.json().then((res) => {
                                console.log(res)
                                window.location.reload();
                              })})
                          }
                        }}
                >
                  { isClicked === "wish" ? (
                    // <img style={{ color : "red" }} src={favorite} alt="favorite_Img" />
                    <FavoriteIcon  sx={{ fontSize : 35 }} style={{ color : "red" }} />
                  ) : (
                    <FavoriteBorderIcon sx={{ fontSize : 35 }} />
                  )}
                </button>
                <div
                  className="perfumeDetail2_title_count_like_number roBold fs-24"
                >
                  {perfumeDetail.wishCount}
                </div>
              </div>
              <div className="perfumeDetail2_title_count_have flex">
                <button className="perfumeDetail2_title_count_have_img"
                        type="button"
                        onClick={() => {
                          havePerfume(user.sToken, id)
                            .then((res) => {res.json().then((res) => {
                              console.log(res)
                              window.location.reload();
                            })})
                        }}
                >
                  { isClicked === "have" ? (
                    <ShoppingCartIcon sx={{ fontSize: 36, color: "red"}}/>
                  ) : (
                    <ShoppingCartOutlinedIcon sx={{ fontSize: 36, color: "black"}}/>
                  )}
                </button>
                <div className="perfumeDetail2_title_count_have_number roBold fs-24">
                  {perfumeDetail.haveCount}
                </div>
              </div>
            </div>
          </div>
          <div className="perfumeDetail2_info flex align-center fs-24">
            <div className="perfumeDetail2_info_season">
              {perfumeDetail.seasons}
            </div>
            <div className="perfumeDetail2_info_time">
              {perfumeDetail.timezone}
            </div>
          </div>
        </div>
        <div className="divide1" />
        <div>
          <Typography style={{ fontFamily : 'SUIT Variable', fontWeight : "bold", textAlign : 'center', margin : "10px" }} component="div" variant="h4">
            이 향수를 PICK한 사용자들은 이런 향수를 좋아해요😍
          </Typography>
          <Slider
            {...settings}
          >
            { recommendPerfume?.map((perfume, index) => (
              <div
                onClick={() => {
                  navigate(`/detail/${perfume.idx}`)
                  window.location.reload();
                }}
                key={perfume.idx}
              >
                <CommonCard
                  img={perfume.perfume_img}
                  title={perfume.perfume_name}
                  description={perfume.scent}
                  author={perfume.brand_name}
                />
              </div>
            )) }
          </Slider>
        </div>
        <div className="divide1"/>
        {user?.isLogin && (
          <div id="perfumeDetail3" className="perfumeDetail3 flex align-center">
            <input type="file" accept=".jpg, .jpeg, .png, .JPG, .JPEG, .PNG" className="perfumeDetail3_imginput" ref={inputRef} onChange={handleUpload}/>
            <button type="button" className="perfumeDetail3_img flex" onClick={uploadImg}>
              {!imageUrl && 
                <AddAPhotoIcon sx={{ fontSize: 80, color: "pink" }} />
              }
              {imageUrl &&
                <img alt="리뷰이미지" src={imageUrl} />
              }
              {/* <div className="perfumeDetail3_profile_img">
                {userProfile?.profileImg && (
                  <img src={userProfile.profileImg} alt="프로필이미지" />
                  )}
                {!userProfile?.profileImg && (
                  <img src={dummyProfile} alt="프로필이미지" />
                )}
              </div> */}
            </button>
            <div className="perfumeDetail3_rating flex kyobo fs-16">
              <div className="perfumeDetail3_rating_total flex">
                <div className="perfumeDetail3_rating_total_sub">
                  종합
                </div>
                <Rating
                  name="simple-controlled"
                  value={total}
                  onChange={(event, newValue) => {
                    setTotal(newValue);
                  }}
                  sx = {{ fontSize: 20}}
                />
              </div>
              <div className="perfumeDetail3_rating_long flex">
                <div className="perfumeDetail3_rating_long_sub">
                  지속
                </div>
                <Rating
                  name="simple-controlled"
                  value={long}
                  onChange={(event, newValue) => {
                    setLong(newValue);
                  }}
                  sx = {{ fontSize: 20}}
                />
              </div>
              <div className="perfumeDetail3_rating_sil flex">
                <div className="perfumeDetail3_rating_sil_sub">
                  잔향
                </div>
                <Rating
                  name="simple-controlled"
                  value={sil}
                  onChange={(event, newValue) => {
                    setSil(newValue);
                  }}
                  sx = {{ fontSize: 20}}
                />
              </div>
            </div>
            <div className="perfumeDetail3_input flex align-center">
              {/* <input className="perfumeDetail3_input_input" type="input" /> */}
              <textarea type="textarea" className="perfumeDetail3_input_text notoReg fs-18" ref={reviewRef} />
              <button
                className="perfumeDetail3_input_btn notoThin fs-18"
                type="button"
                onClick={uploadReview}
              >
                입력
              </button>
            </div>
          </div>
        )}
        <div className="perfumeDetail4 flex">
          {flip === "평점순" && (
            <div className="perfumeDetail4_sort flex">
              <button className="perfumeDetail4_sort_best notoBold fs-30" type="button" onClick={scoreclick}>평점순</button>
              <button className="perfumeDetail4_sort_new notoReg fs-30" type="button" onClick={newclick}>최신순</button>
            </div>
            )}
          {flip === "최신순" && (
            <div className="perfumeDetail4_sort flex">
              <button className="perfumeDetail4_sort_best notoReg fs-30" type="button" onClick={scoreclick}>평점순</button>
              <button className="perfumeDetail4_sort_new notoBold fs-30" type="button" onClick={newclick}>최신순</button>
            </div>
          )}
        </div>
        <div className="divide2" />
        <div className="perfumeDetail5 flex justify-center">
          {perfumeReviewList}
        </div>
      </div>
    </div>
  );
}
export default PerfumeDetail;

import { React, useEffect, useState }  from "react";
// import { Link } from "react-router-dom";
import uuid from "react-uuid";
import axios from "axios";
import dummyProfile from "@images/icon/dummyIcon.png";
import gear from "@images/icon/gear.png";
import InfoReview from "@components/user/InfoReview";
// import { useInView } from "react-intersection-observer";
import "./UserReview.scss";
import { useNavigate, useParams } from "react-router-dom";
import { useRecoilValue } from 'recoil';
import { userProfileState } from "../../atom";

function UserReview() {
  // 방문자 유저 정보 
  const [guestUserProfile, setGuestUserProfile] = useState({});
  const [reviewList, setReviewList] =  useState([]);
  const [guestReviewInfo, setGuestReviewInfo] = useState({});
  const [flip, setFlip] = useState("평점순");
  // 무한스크롤
  // const [ref, inView] = useInView();
  // const [page, setPage] = useState(0);
  // const [loading, setLoading] = useState(false);

  const navigate = useNavigate();
  const params = useParams();
  // console.log("파라미터", params.nickname);
  // console.log("로그인 정보", userProfile[0].nickname)

  const userProfile = useRecoilValue(userProfileState);
  // console.log("리코일 프로필",userProfile)
  // console.log("리코일 이미지",userProfile[0]?.profileImg)
  

  const getGuestUserProfile = () => {
    axios({
      method: "post",
      url : `http://j7c105.p.ssafy.io:8083/user/reviews/${params?.nickname}`,
      // headers : {
      // },
      data : {
        "type" : flip,
        "lastIdx" : null,
        "lastScore" : null,
        "pageSize" : 8
      }
    })
    .then((res) => {
      // console.log("api리스폰스", res.data);
      const reviewInfo = {
        "totalReviews" : res.data.totalReviews,
        "avgReviews" : res.data.avgReviews,
        "hasNext" : res.data.hasNext,
      }
      setGuestReviewInfo(reviewInfo);
      setGuestUserProfile(res.data.userInfo);
      setReviewList(res.data.myReviewList);
    })
    .catch((err) => console.log(err))    
  }

  const editProfile = () => {
    // 라우터 이동
    navigate("/infoedit");
  }


  useEffect(() => {
    getGuestUserProfile();
  }, [flip]);

  // 무한스크롤 
  // useEffect(() => {
  //   if (page > 0) {
  //     // getGuestUserProfile();

  //   }
  // }, [page]);

  // useEffect(() => {
  //   if (inView && !loading) {
  //     setLoading(true);
  //     setPage(page + 1);
  //   }
  // }, [inView, loading]);

  const infoReviewList = reviewList?.map((item)=>(<InfoReview key={uuid()} item={item}/>))
  console.log(infoReviewList)

  const newclick = () => {
    setFlip("최신순");
  }
  const scoreclick = () => {
    setFlip("평점순");
  }

  return (
    <div className="container flex justify-center">
      <div id="userReview" className="userReview">
        <div id="userReview1" className="userReview1 flex justify-center">
        { !guestUserProfile?.profileImg && <img
            src={dummyProfile}
            alt="Profile_Image"
            className="userReview1_profileimg"
          />}
          { !!guestUserProfile?.profileImg && <img
            src={guestUserProfile?.profileImg}
            alt="Profile_Image"
            className="userReview1_profileimg"
          />}
          {/* 수정 버튼 위치 */}
          {guestUserProfile?.userId === userProfile[0]?.userId && <button type="button" className="userReview1_editbutton" onClick={editProfile}>
            <img src={gear} className="userReview1_gear" alt="" />
          </button>}
          <div className="userReview1_nickname notoBold fs-24">
            {guestUserProfile?.nickname}
          </div>
        </div>
        <div id="userReview2" className="userReview2 flex">
          <div className="userReview2_title notoBold fs-24">작성한 리뷰</div>
          <div className="divide1" />
          <div className="userReview2_contents flex">
            <div className="userReview2_contents_count flex notoBold fs-20">
              <div className="userReview2_contents_count_title">
                전체
              </div>
              <div className="userReview2_contents_count_num">
                {guestReviewInfo.totalReviews ? guestReviewInfo.totalReviews : 0}
              </div>
            </div>
            <div className="userReview2_contents_rating flex notoMid fs-20">
              <div className="userReview2_contents_rating_title">
                평균
              </div>
              <div className="userReview2_contents_rating_num">
                {guestReviewInfo.avgReviews ? Math.round(guestReviewInfo.avgReviews * 100) / 100 : 0}
              </div>
            </div>
            <button className="userReview2_contents_new flex notoBold fs-20" type="button" onClick={newclick}>
              최신순
            </button>
            <button className="userReview2_contents_rate flex notoBold fs-20" type="button" onClick={scoreclick}>
              평점순
            </button>
          </div>
          <div className="divide2" />
        </div>
        <div className="userReview3">
            {infoReviewList}
          {/* <div className="observer" ref={ref}>무한스크롤 옵져버</div> */}
        </div>
      </div>
    </div>
  );
}
export default UserReview;

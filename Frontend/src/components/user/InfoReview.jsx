import React from "react";
import perfumeImg from "@images/icon/perfumeImg.svg";
import ystar from "@images/icon/ystar.png";
import long from "@images/icon/long.png";
import sillage from "@images/icon/sillage.png";
import "./InfoReview.scss";

function InfoReview (props) {

  const reviewItem = props.item;

  const starList = []
  let i = 0;
  for (; i < reviewItem.totalScore; ++i) {
    starList.push(<span><img className="infoReview_ystar" src={ystar} alt="" /></span>)
  }
  const longList = []
  let j = 0;
  for (; j < reviewItem.longevity; ++j) {
    longList.push(<span><img className="infoReview_long" src={ystar} alt="" /></span>)
  }
  const sillageList = []
  let k = 0;
  for (; k < reviewItem.sillageScore; ++k) {
    sillageList.push(<span><img className="infoReview_sillage" src={ystar} alt="" /></span>)
  }
  // let j = 0;
  // for (; j < )
  // console.log("별점 만들기", starList)
  console.log(reviewItem)
  return (
    <div id="infoReview" className="infoReview flex">

        <button  className="infoReview_frame" onClick={() => window.location.replace(`/detail/${reviewItem.perfumeIdx}`)} type="button">
          <img  className="infoReview_frame_img" src={reviewItem.reviewImg} alt="향수이미지" />
        </button>

      <div className="infoReview_extra">
        <div className="infoReview_extra_name notoBold fs-24">{reviewItem.perfumeName}</div>
        <div className="infoReview_extra_rating flex">
          <div className="notoMid">종합   </div>
          <div className="infoReview_container">{starList}</div>
          <div className="infoReview_block"> </div>
          <div className="infoReview_extra_rating_date notoMid fs-15">{reviewItem.time?.slice(0,10)}</div>
        </div>
        <div className="infoReview_extra_rating flex">
          <div className="notoMid">지속   </div>
          <div className="infoReview_container">{longList}</div>
        </div>
        <div className="infoReview_extra_rating flex">
          <div className="notoMid">잔향   </div>
          <div className="infoReview_container">{sillageList}</div>
        </div>

        <div className="infoReview_extra_comment notoMid fs-20">
          {reviewItem.content}
        </div>
      </div>
    </div>
  );
}
export default InfoReview;
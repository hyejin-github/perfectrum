import React, { useState, useRef } from "react";
import { useNavigate, useParams } from "react-router-dom";
import AddAPhotoIcon from '@mui/icons-material/AddAPhoto';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import SaveAsSharpIcon from '@mui/icons-material/SaveAsSharp';
import Rating from '@mui/material/Rating';
import axios from "axios";
import { useRecoilValue } from 'recoil';
import { userProfileState, userState } from "../../atom";
import { storage } from "../../firebase";
import dummyProfile from "@images/icon/dummyIcon.png";
// import starRating from "@images/icon/star.svg";
import ystar from "@images/icon/ystar.png";
import long from "@images/icon/long.png";
import sillage from "@images/icon/sillage.png";
import "./PerfumeReview.scss";

function PerfumeReview(props) {
  const user = useRecoilValue(userState);
  const userProfile = useRecoilValue(userProfileState);
  const reviewRef = useRef();
  const inputRef = useRef();
  const { id } = useParams();
  const [editInfo, setEditInfo] = useState(false);
  const changeEdit = () => {
    setEditInfo(!editInfo);
  };
  const navigate = useNavigate();
  const move = () => {
    navigate(`/detail/${id}`);
    window.location.reload();
  };
  const perfumeItem = props.item;
  // console.log(perfumeItem);
  const [total, setTotal] = useState(perfumeItem?.totalScore);
  const [longevity, setLongevity] = useState(perfumeItem?.longevity);
  const [sil, setSil] = useState(perfumeItem?.sillageScore);
  const [image, setImage] = useState({
    image_file: "",
    preview_URL: "",
  });
  const [imageFile, setImageFile] = useState(perfumeItem?.reviewImg);
  const starList = [];
  let i = 0;
  for (; i < perfumeItem.totalScore; ++i) {
    starList.push(<span><img className="perfumeReview_rating_total_ystar" src={ystar} alt="" /></span>)
  }
  const longList = [];
  let j = 0;
  for (; j < perfumeItem.longevity; ++j) {
    longList.push(<span><img className="perfumeReview_rating_long_emo" src={ystar} alt="" /></span>)
  }
  const sillageList = [];
  let k = 0;
  for (; k < perfumeItem.sillageScore; ++k) {
    sillageList.push(<span><img className="perfumeReview_rating_sil_emo" src={ystar} alt="" /></span>)
  };
  const uploadImg = (e) => {
    e.preventDefault();
    inputRef.current.click();
  };
  const handleUpload = (e) => {
    e.preventDefault();
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
      // console.log(image)
      const storageRef = storage.ref("review/test/")
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
            setImageFile(downloadURL);
            console.log(downloadURL);
          });
        }
      )
    }
  };
  const updateReview = () => {
    axios({
      method: "put",
      url: `http://j7c105.p.ssafy.io:8083/detail/${id}/review/${perfumeItem.idx}`,
      headers: {
        Authorization: user.sToken
      },
      data: {
        "reviewImg": imageFile,
        "totalScore": total,
        "longevity": longevity,
        "sillageScore": sil,
        "content" : reviewRef.current.value
      }
    })
    .then(res => console.log("엑시오스", res))
    .catch(err => console.log(err))
    move();
  };
  const deleteReview = () => {
    if (window.confirm("리뷰를 삭제하시겠습니까?")) {
      axios({
        method : "put",
        url : `http://j7c105.p.ssafy.io:8083/detail/${id}/review/delete/${perfumeItem.idx}`,
        headers : {
          Authorization : user.sToken
        }
      })
      .then(res => console.log(res))
      .catch(error => console.log(error))
      move();
    }
  };
  return (
    <div id="perfumeReview" className="perfumeReview flex">
      {!editInfo &&
        <div className="perfumeReview_profile flex">
          <div className="perfumeReview_profile_img flex">
            {/* {perfumeItem.review} */}
            {perfumeItem?.userProfileimg && (
              <img src={perfumeItem.userProfileimg} alt="프로필이미지" />
            )}
            {!perfumeItem?.userProfileimg && (
              <img src={dummyProfile} alt="프로필이미지" />
            )}
          </div>
          <div className="perfumeReview_profile_name notoBold fs-16 flex">
            <div className="perfumeReview_profile_name_nickname">
              {perfumeItem.userNickname}
            </div>
          </div>
        </div>
      }
      {editInfo &&
        <div className="perfumeReview_profile flex">
          <input type="file" accept=".jpg, .jpeg, .png, .JPG, .JPEG, .PNG" className="perfumeReview_profile_imginput" ref={inputRef} onChange={handleUpload} />
          <button type="button" className="perfumeReview_profile_btn flex" onClick={uploadImg}>
            <AddAPhotoIcon sx={{ fontSize: 80, color: "skyblue" }} />
          </button>
          <div className="perfumeReview_profile_fix notoBold fs-16 flex">
            <button type="button" className="perfumeReview_profile_fix_nickname" onClick={uploadImg}>
              이미지 변경
            </button>
          </div>
        </div>
      }
      {!editInfo && 
        <div className="perfumeReview_upload flex">
          {perfumeItem?.reviewImg && (
            <img src={perfumeItem?.reviewImg} alt="업로드한 이미지" />
          )}
          {!perfumeItem?.reviewImg && (
            <img src={dummyProfile} alt="업로드한 이미지" />
          )}
        </div>
      }
      {editInfo &&
        <div className="perfumeReview_upload flex">
          {perfumeItem?.reviewImg && (
            <img src={imageFile} alt="업로드한 이미지" />
          )}
          {!perfumeItem?.reviewImg && (
            <img src={dummyProfile} alt="업로드한 이미지" />
          )}
        </div>
      }
      {!editInfo && 
        <div className="perfumeReview_rating flex kyobo fs-16">
          <div className="perfumeReview_rating_total flex">
            <div className="perfumeReview_rating_total_sub">종합</div>
            {starList}
          </div>
          <div className="perfumeReview_rating_long flex">
            <div className="perfumeReview_rating_long_sub">지속</div>
            {longList}
          </div>
          <div className="perfumeReview_rating_sil flex">
            <div className="perfumeReview_rating_sil_sub">잔향</div>
            {sillageList}
          </div>
        </div>
      }
      {editInfo &&
        <div className="perfumeReview_rating flex kyobo fs-16">
          <div className="perfumeReview_rating_total flex">
            <div className="perfumeReview_rating_total_sub">종합</div>
            <Rating
              name="simple-controlled"
              value={total}
              onChange={(event, newValue) => {
                setTotal(newValue);
              }}
              sx = {{ fontSize: 20}}
            />
          </div>
          <div className="perfumeReview_rating_long flex">
            <div className="perfumeReview_rating_long_sub">지속</div>
            <Rating
              name="simple-controlled"
              value={longevity}
              onChange={(event, newValue) => {
                setLongevity(newValue);
              }}
              sx = {{ fontSize: 20}}
            />
          </div>
          <div className="perfumeReview_rating_sil flex">
            <div className="perfumeReview_rating_sil_sub">잔향</div>
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
      }
      {!editInfo && 
        <div className="perfumeReview_info flex">
          <div className="perfumeReview_info_contents notoMid fs-22">
            {perfumeItem.content}
          </div>
          <div className="perfumeReview_info_date roReg fs-16">
            {perfumeItem.time?.slice(0,10)}
          </div>
        </div>
      }
      {editInfo && 
        <div className="perfumeReview_info flex">
          <div className="perfumeReview_info_input">
            <textarea type="textarea" className="perfumeReview_info_input_board notoReg fs-18" ref={reviewRef} defaultValue={perfumeItem?.content}/>
          </div>
        </div>
      }
      {!editInfo && 
        <div className="perfumeReview_btns flex">
          <button className="perfumeReview_btns_edit" type="button" onClick={changeEdit}>
            <EditIcon sx={{ fontSize: 36, color: "black"}} />
          </button>
          <button className="perfumeReview_btns_del" type="button" onClick={deleteReview}>
            <DeleteIcon sx={{ fontSize: 36, color: "black"}} />
          </button>
        </div>
      }
      {editInfo &&
        <div className="perfumeReview_btns flex">
          <button className="perfumeReview_btns_save" type="button" onClick={updateReview}>
            <SaveAsSharpIcon sx={{ fontSize: 36, color: "black"}} />
          </button>
          <button className="perfumeReview_btns_cancel" type="button" onClick={changeEdit}>
            <HighlightOffIcon sx={{ fontSize: 36, color: "black"}} />
          </button>
        </div>
      }
    </div>
  );
}
export default PerfumeReview;

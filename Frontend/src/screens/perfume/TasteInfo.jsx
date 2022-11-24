import React, { useState } from "react";
import "./TasteInfo.scss";

function TasteInfo() {
  const data = [
    { id: 0, title: "향1" },
    { id: 1, title: "향2" },
    { id: 2, title: "향3" },
    { id: 3, title: "향4" },
    { id: 4, title: "향5" },
    { id: 5, title: "향6" },
    { id: 6, title: "향7" },
    { id: 7, title: "향8" }
  ];
  const [checkItems, setCheckItems] = useState([]);
  const handleSingleCheck = (checked, id) => {
    if (checked) {
      // 단일 선택 시 체크된 아이템을 배열에 추가
      setCheckItems(prev => [...prev, id]);
    } else {
      // 단일 선택 해제 시 체크된 아이템을 제외한 배열 (필터)
      setCheckItems(checkItems.filter(el => el !== id));
    }
  };
  const handleAllCheck = checked => {
    if (checked) {
      // 전체 선택 클릭 시 데이터의 모든 아이템(id)를 담은 배열로 checkItems 상태 업데이트
      const idArray = [];
      data.forEach(el => idArray.push(el.id));
      setCheckItems(idArray);
    } else {
      // 전체 선택 해제 시 checkItems 를 빈 배열로 상태 업데이트
      setCheckItems([]);
    }
  };
  return (
    <div className="container flex">
      <div className="tasteInfo flex justify-center">
        <div className="tasteInfo_title notoBold fs-40 flex">향 찾기</div>
        <div className="tasteInfo_checkbox notoMid fs-24 flex">
          <div className="tasteInfo_checkbox_all flex">
            {checkItems.length === data.length && (
              <input
                type="checkbox"
                name="all"
                onChange={e => handleAllCheck(e.target.checked)}
                checked
              />
            )}
            {checkItems.length !== data.length && (
              <input
                type="checkbox"
                name="all"
                onChange={e => handleAllCheck(e.target.checked)}
                checked={false}
              />
            )}
            <div className="tasteInfo_checkbox_all_title flex justify-center">
              모두
            </div>
          </div>
          <div className="tasteInfo_checkbox_single flex">
            {data.map((item, key) => (
              <div className="tasteInfo_checkbox_single_items flex" key={key}>
                <input
                  type="checkbox"
                  name={`select-${item.id}`}
                  onChange={e => handleSingleCheck(e.target.checked, item.id)}
                  checked={checkItems.includes(item.id) ? 1 : 0}
                />
                <div className="tasteInfo_checkbox_single_items_title flex justify-center">
                  {item.title}
                </div>
              </div>
            ))}
          </div>
        </div>
        <div className="divide" />
        <div className="tasteInfo_result flex justify-center">
          이하에 결과 보여주기
        </div>
      </div>
    </div>
  );
}
export default TasteInfo;

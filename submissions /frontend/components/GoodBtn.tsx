import React, { useState } from "react"
import BlueLike from "../assets/ic_like_on.svg"
import GrayLike from "../assets/ic_like_off.svg"

const blue = {
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  width: "4.375rem",
  height: "4.375rem",
  flexShrink: "0",
  borderRadius: "0.9375rem",
  border: "1px solid #06F",
  background: "#FFF",
}

const gray = {
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  width: "4.375rem",
  height: "4.375rem",
  flexShrink: "0",
  borderRadius: "0.9375rem",
  border: "1px solid #DDD",
  background: "#FFF",
}

const GoodBtn = ({ isActive, onClick }) => {
  const [isBlueActive, setIsBlueActive] = useState(true)

  const handleClick = () => {
    setIsBlueActive(!isBlueActive)
    console.log("Good Button Clicked!") // 좋아요 버튼 확인용
  }

  return (
    <div>
      <div
        style={isActive ? blue : gray}
        onClick={() => {
          onClick()
          handleClick()
        }}
      >
        <img src={isActive ? BlueLike : GrayLike} alt="GoodBtn" />
      </div>
    </div>
  )
}

export default GoodBtn

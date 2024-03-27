import React, { useState } from "react"
import BlueHate from "../assets/ic_dislike_on.svg"
import GrayHate from "../assets/ic_dislike_off.svg"

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

const HateBtn = ({ isActive, onClick }) => {
  const [isBlueActive, setIsBlueActive] = useState(true)

  const handleClick = () => {
    setIsBlueActive(!isBlueActive)
    console.log("Hate Button Clicked!") // 싫어요 버튼 확인용
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
        <img src={isActive ? BlueHate : GrayHate} alt="HateBtn" />
      </div>
    </div>
  )
}

export default HateBtn

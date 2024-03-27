import React from "react"
import smallLogo from "../assets/lg_purify_full.svg"
import arrowLeft from "../assets/ic_arrow_left.svg"
import { useLocation, useNavigate } from "react-router-dom"

const headerStyle = {
  position: "fixed" as const,
  top: "0",
  display: "flex",
  alignItems: "center",
  width: "100%",
  height: "6.25rem",
  paddingLeft: "200px",
  flexShrink: "0",
  background: "#FFF",
  boxShadow: "0px 4px 4px 0px rgba(0, 0, 0, 0.10)",
  zIndex: "1",
}

const logoStyle = {}

const backBtnStyle = {
  width: "30px",
}

const Logo = () => {
  const location = useLocation()
  const navigate = useNavigate()

  const goToBack = () => {
    navigate('/profile')
  }

  return (
    <header style={headerStyle}>
      {location.pathname === "/comment" ||
      location.pathname === "/searchDetail" ? (
        <button
          onClick={goToBack}
          style={{ background: "none", cursor: "pointer" }}
        >
          <img src={arrowLeft} style={backBtnStyle} />
        </button>
      ) : (
        <img src={smallLogo} style={logoStyle} />
      )}
    </header>
  )
}

export default Logo

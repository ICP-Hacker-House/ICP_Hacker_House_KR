import React, { useEffect, useState } from "react"
import "../styles/center.css"
import { Link } from "react-router-dom"
import qrcode from "qrcode"
import { useCanister } from "@connect2ic/react"
import { authenticator as auth } from "@otplib/preset-browser"
const nicknameStyle = {
  color: "#444",
  fontFamily: "Poppins",
  fontSize: "1.875rem",
  fontStyle: "normal",
  fontWeight: "500",
  lineHeight: "1.875rem",
}

const buttonStyle = {
  width: "18.75rem",
  height: "3.75rem",
  flexShrink: "0",
  borderRadius: "1.875rem",
  background: "#06F",
  color: "#FFF",
  fontFamily: "Poppins",
  fontSize: "1.125rem",
  fontStyle: "normal",
  fontWeight: "500",
  lineHeight: "1rem",
  marginTop: "2.5rem",
}

const bgStyle = {
  width: "18.75rem",
  height: "18.75rem",
  flexShrink: "0",
  background: "#D9D9D9",
  marginTop: "2.5rem",
}
interface CreateORPCodeProps {
  principal: string
}
const CreateOTPCode = ({ principal }: CreateORPCodeProps) => {
  const [authentication] = useCanister("authentication")
  const [qr, setQr] = useState("")
  useEffect(() => {
    createQr()
  }, [])
  const createQr = async () => {
    console.log("useEffect Check!")

    console.log("Start google authenticator")
    const user = "username"
    const service = "Lens Temperature"
    const secret = auth.generateSecret()

    // 시크릿 저장
    console.log("secret", secret)
    const secretHashRes = await authentication.update_secretHash(
      principal,
      secret,
    )
    console.log("secretHash updated", secretHashRes)

    // QR 생성
    const otpauth = auth.keyuri(user, service, secret)
    qrcode.toDataURL(otpauth, (err, imageUrl) => {
      if (err) {
        console.log("Error with QR")
        return
      }
      console.log(imageUrl)
      setQr(imageUrl)
    })

    await authentication.update_secretProvided(principal, true)
  }
  return (
    <div className="center">
      <p style={nicknameStyle}>Create OTP Code</p>
      {/* <div style={bgStyle}>QR코드 나타는 곳</div> */}
      <img style={bgStyle} src={qr} />

      <Link to="/verifyOTP">
        <button style={buttonStyle}>verify OTP</button>
      </Link>
    </div>
  )
}

export default CreateOTPCode

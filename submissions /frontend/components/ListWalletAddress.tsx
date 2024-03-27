import React from "react"
import { useNavigate } from "react-router-dom";
import "../styles/ListWallet.css"

const ListWalletAddress = ({walletAddress}) => {
  const navigate = useNavigate();

  const detailNavigate = () => {
    navigate("/searchDetail");
  };
  
  return (
    <div className="ListWalletAddressBox">
      <div className="friendTechBox">
        <img
          src={
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRa_pPidUjfvJxwf0A93KClQX6kQiJD8C3Z0A&usqp=CAU"
          }
          alt="friend tech"
          style={{ width: "100%", height: "100%", borderRadius: "30%" }}
        />
      </div>
      {/* <span>{name}</span> */}
      <span style={{ marginLeft: "1rem" }}>pingping</span>{" "}
      {/* walletAddress에 맞는 socialFi*/}
      <span style={{ marginLeft: "0.63rem" }}>@beakerjin</span>{" "}
      {/* walletAddress에 맞는  */}
      <button className="detail-btn" onClick={detailNavigate}>
        detail
        {/* searchDetail로 가는 코드 추가해야됨 */}
      </button>
    </div>
  )
}

export default ListWalletAddress
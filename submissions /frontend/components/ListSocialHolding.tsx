import React from "react"
import styles from "../styles/Profile.module.css"
// images
function makeSimpleAddress(address: string) {
  return address.slice(0, 6) + "..." + address.slice(-4)
}
const ListSocialHolding = ({ onClick, name, address, principal }) => {
  
  return (
    <div className={styles.box_holding_social_list}>
      <div className={styles.box_social_img}>
        <img
          src={
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRa_pPidUjfvJxwf0A93KClQX6kQiJD8C3Z0A&usqp=CAU"
          }
          alt="friend tech"
        />
      </div>
      <span className={styles.txt_social_name}>{name}</span>
      <span className={styles.txt_social_username}>
        {makeSimpleAddress(address)}
      </span>
      <button className={styles.btn_social_detail} onClick={onClick}>
        detail
      </button>
    </div>
  )
}

export default ListSocialHolding

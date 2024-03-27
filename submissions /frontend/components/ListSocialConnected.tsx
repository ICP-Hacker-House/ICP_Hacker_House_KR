import React, { useEffect, useState } from "react"
import styles from "../styles/Profile.module.css"

function makeSimpleAddress(address: string) {
  return address.slice(0, 6) + "..." + address.slice(-4)
}
const ListSocialConnected = ({ disconnect, address }) => {
  return (
    <>
      {address && (
        <div className={styles.box_connected_social_list}>
          <div className={styles.box_social_img}>
            <img
              src={
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRa_pPidUjfvJxwf0A93KClQX6kQiJD8C3Z0A&usqp=CAU"
              }
              alt="friend tech"
            />
          </div>
          <span className={styles.txt_social_name}>lens protocol</span>
          <span className={styles.txt_social_address}>
            {makeSimpleAddress(address)}
          </span>
          <button className={styles.btn_social_disconnect} onClick={disconnect}>
            disconnect
          </button>
        </div>
      )}
    </>
  )
}

export default ListSocialConnected

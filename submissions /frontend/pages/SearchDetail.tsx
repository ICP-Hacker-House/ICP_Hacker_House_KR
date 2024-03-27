import styles from "../styles/Profile.module.css"
import React, { useEffect, useState } from "react"
import { useCanister } from "@connect2ic/react"
import { useDisconnect } from "wagmi"
// components
import Logo from "../components/Logo"
import ProfileTop from "../components/ProfileTop"
import ProfileBottomButton from "../components/ProfileBottomButton"
import UserComment from "../components/UserComment"

import { useAccount } from "wagmi"

//image
import message from "../assets/ic_message.svg"

const baseURL = "https://base.llamarpc.com/"
// interface ProfileProps {
//   principal: string
// }
export default function SearchDetail({ principal, searchPrincipal }) {
  // Canisters
  const [purify] = useCanister("purify")

  const [holding, setHolding] = useState(false)

  // Profile Query
  const [profile, setProfile] = useState(null)
  const [comments, setComments] = useState(null)

  const [connected, setConnected] = useState([])
  const { disconnect } = useDisconnect()

  // Name, PFP
  const [name, setName] = useState("")
  const [pfp, setPfp] = useState("")

  // Like, Dislike, vly point
  const [like, setLike] = useState(0)
  const [dislike, setDislike] = useState(0)
  const [vlyPoint, setVlyPoint] = useState(0)

  // Commented
  const [commented, setCommented] = useState(false)

  const { address, isConnected } = useAccount()

  useEffect(() => {
    if (isConnected) {
      queryProfile()
    }
  }, [isConnected])

  useEffect(() => {
    if (commented) {
      queryProfile()
      setCommented(false)
    }
  }, [commented])

  const queryProfile = async () => {
    console.log("Querying profile")
    if (searchPrincipal === null) {
      return
    }
    const profile = (await purify.query_profile(searchPrincipal)) as any
    const comments = await purify.query_comments(searchPrincipal)
    console.log("Profile queried")
    console.log(profile)
    if (profile.length === 0) {
      await purify.create_profile(searchPrincipal)
      console.log("Profile created")
    } else {
      setProfile(profile)
      setComments(comments)
      setName(profile[1])
      setPfp(profile[2])
      setVlyPoint(profile[5])
      setLike(profile[3])
      setDislike(profile[4])
      console.log("Profile set", profile)
      console.log("Comments set", comments)
      console.log("Name set", name)
      console.log("Pfp set", pfp)
    }
  }

  return (
    <div>
      <Logo />
      <ProfileTop
        name={name}
        pfp={pfp}
        principal={searchPrincipal}
        like={like}
        dislike={dislike}
        vlyPoint={vlyPoint}
      />
      <section className={styles.section_profile_bottom_title}>
        <img src={message} className={styles.msgImg} />
        <ProfileBottomButton
          className={holding ? "" : styles.btn_profile_bottom_active}
          content="Comment"
          onClick={() => setHolding(false)}
        />
      </section>
      <UserComment comments={comments} />
    </div>
  )
}

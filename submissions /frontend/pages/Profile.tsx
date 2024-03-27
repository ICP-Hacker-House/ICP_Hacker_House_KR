import styles from "../styles/Profile.module.css"
import React, { ChangeEvent, useEffect, useState } from "react"
import { useCanister } from "@connect2ic/react"
import { useDisconnect } from "wagmi"
import SendImg from "../assets/ic_send.svg"

// components
import Logo from "../components/Logo"
import ProfileTop from "../components/ProfileTop"
import ProfileBottomButton from "../components/ProfileBottomButton"
import ListSocialHolding from "../components/ListSocialHolding"
import ListSocialConnected from "../components/ListSocialConnected"
import SocialConnect from "../components/SocialConnect"
import ListWalletAddress from "../components/ListWalletAddress"

import { useAccount } from "wagmi"
import { useWeb3Modal } from "@web3modal/wagmi/react"
import { useNavigate } from "react-router-dom"

import axios from "axios"

enum SocialFi {
  NextId,
  PostTech,
  FriendTech,
  StarsArena,
}
export default function Profile({
  principal,
  setCommentPrincipal,
  setSearchPrincipal,
}) {
  const openHolding = () => {
    setBottomChk([false, true, false])
  }
  // navigate
  const navigate = useNavigate()
  // Canisters
  const [httpOutcalls] = useCanister("httpOutcalls")
  const [purify] = useCanister("purify")
  const [authentication] = useCanister("authentication")

  const [bottomChk, setBottomChk] = useState([true, false, false])
  const [connected, setConnected] = useState(false)
  const [search, setSearch] = useState(false)
  // Profile Query
  const [index, setIndex] = useState<string[]>()
  const [profile, setProfile] = useState<string[]>()
  const [comments, setComments] = useState(null)

  const { disconnect } = useDisconnect()
  // const {connect} = useConnect()
  const { open: connect } = useWeb3Modal()

  // Name, PFP
  const [name, setName] = useState("")
  const [pfp, setPfp] = useState("")
  const [holdings, setHoldings] = useState<any>()
  const [walletAddress, setWalletAddress] = useState("")

  // like, dislike, vly point
  const [like, setLike] = useState(0)
  const [dislike, setDislike] = useState(0)
  const [vlyPoint, setVlyPoint] = useState(0)

  // rerender holdings
  const [rerender, setRerender] = useState(false)

  const { address, isConnected } = useAccount()
  const [socialFi, setSocialFi] = useState<SocialFi>()

  const [isInputEmpty, setInputEmpty] = useState(true)
  const inputStyle = isInputEmpty ? inputStyleGray : inputStyleBlue
  const btnStyle = isInputEmpty ? btnStyleGray : btnStyleBlue

  useEffect(() => {
    connectWalletAndQuery()
  }, [])

  useEffect(() => {
    handleRerender()
  }, [rerender])
  const handleRerender = () => {
    queryHolder()
    queryFriendTech()
    setRerender(false)
  }
  const connectWalletAndQuery = async () => {
    await connect()
    console.log("connected")
    await queryAll()
  }

  const queryAll = async () => {
    await queryIndex()
    await queryProfile()
    await queryFriendTech()
    await queryHolder()
  }

  const handleSocialFi = (e: ChangeEvent<HTMLSelectElement>) => {
    if (e.target.value === "friendTech") {
      setSocialFi(SocialFi.FriendTech)
    } else if (e.target.value === "starsArena") {
      setSocialFi(SocialFi.StarsArena)
    } else if (e.target.value === "postTech") {
      setSocialFi(SocialFi.PostTech)
    } else if (e.target.value === "nextId") {
      setSocialFi(SocialFi.NextId)
    }
  }
  const queryIndex = async () => {
    console.log("Querying index")
    const index = (await purify.query_index(principal)) as string[]
    console.log("Index queried")
    console.log(index)
    if (index.length === 0) {
      await purify.create_index(principal)
      console.log("Index created")
    } else {
      setIndex(index)
    }
  }

  const queryProfile = async () => {
    console.log("Querying profile")
    const profile = (await purify.query_profile(principal)) as string[]
    const comments = await purify.query_comments(principal)
    console.log("Profile queried")
    console.log(profile)
    if (profile.length === 0) {
      await purify.create_profile(principal)
      console.log("Profile created")
    } else {
      setProfile(profile)
      setComments(comments as string)
      setName(profile[1])
      setPfp(profile[2])
      setVlyPoint(Number(profile[5]))
      setLike(Number(profile[3]))
      setDislike(Number(profile[4]))
    }
  }

  const queryFriendTech = async () => {
    console.log("queryFriendTech")
    console.log("address", address)
    console.log("httpOutcalls", httpOutcalls)
    try {
      // const res = await httpOutcalls.queryFriendTech(address)
      // console.log("res", res)
      // const jsonRes = JSON.parse(res as string)
      const response = await axios.get(
        "https://prod-api.kosetto.com/users/" + address,
      )
      const jsonRes = response.data
      console.log("success!", jsonRes)
      setName(jsonRes.twitterName)
      await purify.update_profile(principal, jsonRes.twitterName, 0)
      await purify.update_profile(principal, jsonRes.twitterPfpUrl, 1)
      await purify.update_index(principal, address, 2)
      // 이더 -> 프린 으로 고치셈
      await authentication.update_ethAddress(address, principal)
      await authentication.update_ethAddress(address.toLowerCase(), principal)
      console.log("updated ethAddress", address, principal)
    } catch (err) {
      console.log("error!", err)
    }
  }

  const queryHolder = async () => {
    console.log("queryHolder")
    console.log("address", address)
    console.log("httpOutcalls", httpOutcalls)
    try {
      // const res = await httpOutcalls.queryHolder(address)
      // if (!res) return
      // const jsonRes = JSON.parse(res as string)
      const options = {
        method: "GET",
        url:
          "https://friend-tech.p.rapidapi.com/users/" +
          address +
          "/token/holders",
        headers: {
          "X-RapidAPI-Key":
            "193486b59emsh42493bbebcba2aep1af1cdjsn4ddd64333322",
          "X-RapidAPI-Host": "friend-tech.p.rapidapi.com",
        },
      }
      const response = await axios.request(options)
      const jsonRes = response.data
      console.log(jsonRes.users)
      // const holdingsArr = Object.keys(jsonRes.users).map((key) => jsonRes[key])
      // const list1 = list2.map(innerList => innerList[1]);
      const holdingsArr = Object.entries(jsonRes.users)
      const holdingsUsersArr = holdingsArr.map((holding) => holding[1])
      console.log("holdingsArr", holdingsUsersArr)

      setHoldings(holdingsUsersArr)
      console.log("updated profile")
    } catch (err) {
      console.log("error!", err)
    }
  }

  const handleComment = async (commentAddress) => {
    // setCommentPrincipal(holding.principal)
    // 현재는 프린 -> 이더로 되어있음 고치셈
    const res = await authentication.query_ethAddress(commentAddress)
    if (!res) {
      console.log("Queryying with address", commentAddress)
      console.log("res", res)
      console.log("No Purify ACC found")
      return
    } else {
      console.log("Purify ACC found", res)
      setCommentPrincipal(res)
      navigate("/comment")
    }
  }

  const handleSearch = async () => {
    console.log("Searching with address", walletAddress)
    const res = await authentication.query_ethAddress(walletAddress)
    if (!res) {
      console.log("Queryying with address", walletAddress)
      console.log("res", res)
      console.log("No Purify ACC found")
      return
    } else {
      console.log("Purify ACC found", res)
      setSearchPrincipal(res)
      navigate("/searchDetail")
    }
  }

  return (
    <div>
      <Logo />
      <ProfileTop
        name={name}
        pfp={pfp}
        principal={principal}
        like={like}
        dislike={dislike}
        vlyPoint={vlyPoint}
      />
      <section className={styles.section_profile_bottom_title}>
        <ProfileBottomButton
          className={bottomChk[0] ? styles.btn_profile_bottom_active : ""}
          content="connected social"
          onClick={() => {
            setBottomChk([true, false, false])
          }}
        />
        <ProfileBottomButton
          className={bottomChk[1] ? styles.btn_profile_bottom_active : ""}
          content="holding"
          onClick={() => {
            setBottomChk([false, true, false])
          }}
        />
        <ProfileBottomButton
          className={bottomChk[2] ? styles.btn_profile_bottom_active : ""}
          content="search"
          onClick={() => {
            setBottomChk([false, false, true])
          }}
        />
      </section>
      <section>
        {/* connected social */}
        {bottomChk[0] ? (
          <section className={styles.section_connected_social}>
            {index &&
              index.map(
                (address, key) =>
                  address && (
                    <ListSocialConnected
                      key={key}
                      address={address}
                      disconnect={() => disconnect()}
                    />
                  ),
              )}
            <SocialConnect
              handleSocialFi={handleSocialFi}
              socialFi={socialFi}
              purify={purify}
              principal={principal}
              setIndex={setIndex}
              setRerender={setRerender}
              openHolding={openHolding}
            />
          </section>
        ) : bottomChk[1] ? (
          // holding
          <section className={styles.section_holding}>
            {holdings &&
              holdings.map((holding) => (
                // <div key={holding.id}>{holding.twitterName}</div>
                <ListSocialHolding
                  key={holding.id}
                  name={holding.twitterName}
                  address={holding.address}
                  principal={holding.principal}
                  onClick={() => {
                    handleComment(holding.address)
                  }}
                />
              ))}
          </section>
        ) : (
          // search
          <section>
            <div style={inputBox}>
              <input
                value={walletAddress} // 지갑 주소 검색
                style={isInputEmpty ? inputStyleGray : inputStyleBlue}
                onChange={(e) => {
                  setWalletAddress(e.target.value)
                  setInputEmpty(e.target.value === "")
                }}
              />
              <button
                style={btnStyle as React.CSSProperties}
                type="submit"
                onClick={handleSearch}
              >
                <img
                  src={SendImg}
                  style={{ marginTop: "0.3rem", marginLeft: "0.3rem" }}
                />
              </button>
            </div>
            {/* 여기에 친구 리스트가 뜸*/}
            {/* <ListWalletAddress
              walletAddress={walletAddress} */}
            {/* // friendList={friendList}
              // 친구 리스트 어떻게 가져오지...... */}
            {/* /> */}
          </section>
        )}
      </section>
    </div>
  )
}

const inputStyleGray = {
  marginTop: "1.06rem",
  outline: "none",
  borderRadius: "1.5625rem",
  border: "1px solid #DDD",
  background: "#FFF",
  width: "100%",
  height: "2.9375rem",
  display: "flex",
  padding: "0 30px",
}

const inputStyleBlue = {
  marginTop: "1.06rem",
  outline: "none",
  borderRadius: "1.5625rem",
  border: "1px solid #06F",
  background: "#FFF",
  width: "100%",
  height: "2.9375rem",
  display: "flex",
  padding: "0 30px",
}

const inputBox = {
  display: "flex",
  marginLeft: "12.25rem",
  marginRight: "12.44rem",
}

const btnStyleGray = {
  width: "2.875rem",
  height: "2.9375rem",
  flexShrink: "0",
  borderRadius: "1.5625rem",
  border: "1px solid #DDD",
  background: "#DDD",
  position: "relative",
  marginLeft: "-40px",
  marginTop: "1.09rem",
}

const btnStyleBlue = {
  width: "2.875rem",
  height: "2.9375rem",
  flexShrink: "0",
  borderRadius: "1.5625rem",
  border: "1px solid #06F",
  background: "#06F",
  position: "relative",
  marginLeft: "-40px",
  marginTop: "1.09rem",
}

import React, { useState } from "react"
import styles from "../styles/Profile.module.css"
import { useAccount, useDisconnect } from "wagmi"

interface ModalProps {
  close: () => void
}

const sleep = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms))
const Modal = ({ close }: ModalProps) => {
  const [spin, setSpin] = useState(0)
  const onClose = async () => {
    await verify()

    setSpin(1)
    await sleep(2000);

    setSpin(2)
    await sleep(2000);

    close()
  }
  return (
    <div className="modal">
      <div className="modal-content">
        <h4>Verify Ownership</h4>
        <div className="spinner-container">
          { spin === 0 ? (
            <p> Are you owner?</p>
          ) : spin === 1 ? (
            <div className="spinner" />
          ) : (
            <p>Verified</p>
          )}
        </div>
        <div>
          <button onClick={onClose}>It's me!</button>
        </div>
      </div>
    </div>
  )
}

async function verify() {}
const SocialConnect = ({
  handleSocialFi,
  socialFi,
  purify,
  principal,
  setIndex,
  setRerender,
  openHolding,
}) => {
  const { address } = useAccount()
  // const { open } = useWeb3Modal()
  const { disconnect } = useDisconnect()
  const updateIndex = async (type: number) => {
    console.log("update index", type)
    await purify.update_index(principal, address, type)
    setRerender(true)
    const _index = await purify.query_index(principal)
    setIndex(_index)
    console.log("index", _index)
  }

  const [isModalOpen, setIsModalOpen] = useState(false)
  const openModal = () => setIsModalOpen(true)
  const closeModal = () => {
    openHolding()
    setIsModalOpen(false)
  }

  const open = async () => {
    // socialFi && (await updateIndex(socialFi))
    openModal()
    console.log("socialFi", socialFi)
  }

  return (
    <>
      <div className={styles.box_connected_social_add}>
        <select
          name="social"
          className={styles.select_social}
          onChange={handleSocialFi}
        >
          <option value="">select social</option>
          <option value="friendTech">lens protocol</option>
        </select>
        <button
          className={styles.btn_social_connect}
          disabled={!socialFi}
          onClick={open}
        >
          verify ownership
        </button>
      </div>
      {isModalOpen && <Modal close={closeModal} />}
    </>
  )
}

export default SocialConnect

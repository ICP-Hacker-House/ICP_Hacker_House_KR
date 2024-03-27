import React from "react"
import styles from "../styles/components/Button.module.css"

type ButtonProps = {
  content: string
  onClick: () => void
}

const ButtonSolid = ({ content, onClick }: ButtonProps) => {
  return (
    <button className={styles.btn_solid} onClick={onClick}>
      {content}
    </button>
  )
}

export default ButtonSolid

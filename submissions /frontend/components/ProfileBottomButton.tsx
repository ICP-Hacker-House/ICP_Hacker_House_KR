import React from "react"

type ProfileBottomButtonProps = {
  className: string
  onClick: () => void
  content: string
}

const ProfileBottomButton = ({
  className,
  onClick,
  content,
}: ProfileBottomButtonProps) => {
  return (
    <button className={className} onClick={onClick}>
      {content}
    </button>
  )
}

export default ProfileBottomButton

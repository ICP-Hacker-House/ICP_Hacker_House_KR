import React from "react"
import PuriImg from "../assets/lg_purify.svg"
import "../styles/components/UserComment.css"
import CommentList from "./CommentList"

const UserComment = ({ comments }) => {
  return (
    <div className="userCommnetBox">
      <CommentList comments={comments} />
    </div>
  )
}

export default UserComment

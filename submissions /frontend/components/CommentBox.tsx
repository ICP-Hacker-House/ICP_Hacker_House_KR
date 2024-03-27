import React, { useEffect, useState } from "react"
import CommentInput from "../components/CommentInput"
import CommentList from "../components/CommentList"

const CommentBox = ({
  comments: commentsICP,
  principal,
  commentPrincipal,
  setCommented,
}) => {
  const [comments, setComments] = useState([])

  useEffect(() => {
    console.log("Updating comments", commentsICP)
    console.log("principal", principal)
    console.log("commentPrincipal", commentPrincipal)
    if (commentsICP !== null) {
      setComments(commentsICP)
    }
  }, [commentsICP])

  return (
    <div className="commentBox">
      <section style={{ marginTop: "1.25rem", marginLeft: "12rem" }}>
        <CommentList comments={comments} />
        <CommentInput
          principal={principal}
          commentPrincipal={commentPrincipal}
          setCommented={setCommented}
        />
      </section>
    </div>
  )
}

export default CommentBox

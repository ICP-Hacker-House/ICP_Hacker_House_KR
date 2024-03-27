import { useCanister, useConnect } from "@connect2ic/react"
import React, { useEffect, useState } from "react"

interface PurifyProps {
  TFAuthed: boolean
  principal: String
}

const Purify = ({ TFAuthed, principal }: PurifyProps) => {
  const [purify] = useCanister("purify")
  const [profile, setProfile] = useState(null)
  const [index, setIndex] = useState(null)
  const [comments, setComments] = useState(null)
  const [rateComment, setRateComment] = useState(null)
  const [ratePrincipal, setRatePrincipal] = useState(null)

  const createProfile = async () => {
    console.log("Creating profile")
    await purify.create_profile(principal)
    console.log("Profile created")
  }

  const createIndex = async () => {
    console.log("Creating index")
    await purify.create_index(principal)
    console.log("Index created")
  }

  const queryProfile = async () => {
    console.log("Querying profile")
    const profile = await purify.query_profile(principal)
    const comments = await purify.query_comments(principal)
    console.log("Profile queried")
    console.log(profile)
    console.log(comments)
    setProfile(profile)
    setComments(comments)
  }

  const queryIndex = async () => {
    console.log("Querying index")
    const index = await purify.query_index(principal)
    console.log("Index queried")
    console.log(index)
    setIndex(index)
  }

  const rate = async (
    comment: String,
    commentor: String,
    like: Boolean,
    principal: String,
  ) => {
    console.log("Rating")
    await purify.rate(comment, commentor, like, principal)
    console.log("Rated")
  }

  return (
    <div>
      Welcome to Purify
      <div>
        {TFAuthed && (
          <div>
            <button onClick={() => createProfile()}>Create Profile</button>

            <button onClick={() => createIndex()}>Create Index</button>

            <button
              onClick={() =>
                rate(rateComment, "commentor", true, ratePrincipal)
              }
            >
              Rate
            </button>
            <input
              type="text"
              placeholder="Comment"
              onChange={(e) => setRateComment(e.target.value)}
            />
            <input
              type="text"
              placeholder="Principal"
              onChange={(e) => setRatePrincipal(e.target.value)}
            />

            <button onClick={() => queryProfile()}>Query Profile</button>

            <div>
              {profile && (
                <div>
                  <div>Profile</div>
                  <div>Name: {profile[0]}</div>
                  <div>Profile_pic: {profile[1]}</div>
                  <div>Principal: {profile[2]}</div>
                  <div>Plus: {profile[3]}</div>
                  <div>Minus: {profile[4]}</div>
                  <div>Vely Points: {profile[5]}</div>
                  {comments &&
                    comments.map((comment) => (
                      <div>
                        <p>{`Comment: ${comment.comment}`}</p>
                        <p>{`Commentor: ${comment.commentor}`}</p>
                      </div>
                    ))}
                </div>
              )}
            </div>

            <button onClick={() => queryIndex()}>Query Index</button>
            <div>
              {index && (
                <div>
                  <div>Index</div>
                  <div>{index[0]}</div>
                  <div>{index[1]}</div>
                  <div>{index[2]}</div>
                </div>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  )
}

export { Purify }

// import { authenticator } from "otplib"
import { authenticator as a } from "@otplib/preset-browser"
import React, { useEffect, useState } from "react"
import qrcode from "qrcode"
import { useCanister } from "@connect2ic/react"

// II DEV
import { AuthClient } from "@dfinity/auth-client"
import { HttpAgent } from "@dfinity/agent"

interface AuthenticatorProps {
  TFAuthed: boolean
  setTFAAuthed: (value: boolean) => void
  principal: String
  setPrincipal: any
}

const Authenticator = ({
  TFAuthed,
  setTFAAuthed,
  principal,
  setPrincipal,
}: AuthenticatorProps) => {
  const [qr, setQr] = useState(null)
  const [secret, setSecret] = useState(null)
  const [token, setToken] = useState("")
  const [authentication] = useCanister("authentication")
  const [pInput, setPInput] = useState("")
  const [TFRegistered, setTFRegistered] = useState(false)

  // II
  const [isConnected, setIsConnected] = React.useState(false)
  // const [principal, setPrincipal] = React.useState(null)

  const login = async () => {
    const authClient = await AuthClient.create()
    const internetIdentity = import.meta.env.VITE_INTERNET_IDENTITY
    authClient.login({
      identityProvider: `http://localhost:4943/?canisterId=${internetIdentity}`,
      onSuccess: () => {
        console.log("Logged in")
        setIsConnected(true)
      },
    })

    const identity = await authClient.getIdentity()
    setPrincipal(identity.getPrincipal().toString())
    console.log("principal", identity.getPrincipal().toString())
    const agent = new HttpAgent({ identity })

    const res = await authentication.query_secretProvided(
      identity.getPrincipal().toString(),
    )
    if (res) {
      // setTFRegistered(true)
    }
  }

  // Authenticator
  const authenticatorInitialize = async () => {
    const res = await authentication.query_secretProvided(principal)
    if (res) {
      console.log("Secret already provided", res)
      setTFRegistered(true)
      // return
    }

    console.log("Start google authenticator")
    const user = "username"
    const service = "Lens Temperature"
    const secret = a.generateSecret()

    // 시크릿 저장
    console.log("secret", secret)
    setSecret(secret)
    const secretHashRes = await authentication.update_secretHash(
      principal,
      secret,
    )
    console.log("secretHash updated", secretHashRes)

    // QR 생성
    const otpauth = a.keyuri(user, service, secret)
    qrcode.toDataURL(otpauth, (err, imageUrl) => {
      if (err) {
        console.log("Error with QR")
        return
      }
      console.log(imageUrl)
      setQr(imageUrl)
    })

    // Auth 모토코 업데이트
    let updateRes = await authentication.update_secretProvided(principal, true)
    console.log("secretProvided updated", updateRes)

    setTFRegistered(true)
  }

  // 2FA 인증
  const verifyAuthenticator = async (token) => {
    console.log("verifyAuthenticator")

    const res = await authentication.query_secretProvided(principal)
    if (!res) {
      console.log("Secret not provided")
      return
    }

    // Auth 모토코 쿼리
    const secret = await authentication.query_secretHash(principal)

    try {
      console.log("token", token)
      console.log("secret", secret)
      const isValid = a.verify({ token, secret })
      console.log("AUTH RESULT", isValid)
      setTFAAuthed(isValid)
    } catch (e) {
      console.error(e)
    }
  }

  return (
    <div>
      <div>
        {isConnected ? (
          <div>
            <div>User: {principal}</div>
          </div>
        ) : (
          <button onClick={login}>Login</button>
        )}
      </div>
      {isConnected && !TFRegistered && (
        <div>
          You are not registered for 2FA. Please initialize your authenticator.
          <button onClick={() => authenticatorInitialize()}>Initialize</button>
        </div>
      )}
      <div>
        <img src={qr} />
      </div>
      {TFRegistered && (
        <div>
          Please verify your authenticator
          <div>
            <button onClick={() => verifyAuthenticator(token)}>Verify</button>
          </div>
          <input
            type="number"
            placeholder="Enter Token"
            value={token}
            onChange={(e) => setToken(e.target.value)}
          />
        </div>
      )}
    </div>
  )
}

export { Authenticator }

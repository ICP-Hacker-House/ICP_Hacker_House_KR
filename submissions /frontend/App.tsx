import React, { useState } from "react"
import { Route, Routes } from "react-router-dom"
import logo from "./assets/dfinity.svg"
import { createClient } from "@connect2ic/core"
import { defaultProviders } from "@connect2ic/core/providers"
import {
  ConnectButton,
  ConnectDialog,
  Connect2ICProvider,
  useConnect,
  useCanister,
} from "@connect2ic/react"
// import "@connect2ic/core/style.css"
import * as purify from "../.dfx/local/canisters/purify"
import * as authentication from "../.dfx/local/canisters/authentication"
import * as httpOutcalls from "../.dfx/local/canisters/httpOutcalls"
import { Purify } from "./components/Purify"

// Pages
import { Authenticator } from "./components/Authenticator"
import Login from "./pages/Login"
import SearchDetail from "./pages/SearchDetail"

// SocialQuery
import { SocialQuery } from "./components/SocialQuery"
import Profile from "./pages/Profile"

import { createWeb3Modal } from "@web3modal/wagmi/react"
import { walletConnectProvider } from "@web3modal/wagmi"

import { WagmiConfig, configureChains, createConfig } from "wagmi"
import { publicProvider } from "wagmi/providers/public"
import { polygon } from "wagmi/chains"
import { InjectedConnector } from "wagmi/connectors/injected"
import { WalletConnectConnector } from "wagmi/connectors/walletConnect"
import { alchemyProvider } from "wagmi/providers/alchemy"

// 1. Get PROJECT_ID
const projectId = import.meta.env.VITE_PROJECT_ID
const apikey = import.meta.env.VITE_ALCHEMY_KEY

// 2. Create wagmiConfig
const { chains, publicClient } = configureChains(
  [polygon],
  [
    walletConnectProvider({ projectId }),
    publicProvider(),
    alchemyProvider(apikey),
  ],
)

const metadata = {
  name: "Web3Modal",
  description: "Web3Modal Example",
  url: "https://web3modal.com",
  icons: ["https://avatars.githubusercontent.com/u/37784886"],
}

const wagmiConfig = createConfig({
  autoConnect: true,
  connectors: [
    new WalletConnectConnector({
      chains,
      options: { projectId, showQrModal: false, metadata },
    }),
    new InjectedConnector({ chains, options: { shimDisconnect: true } }),
  ],
  publicClient,
})

// 3. Create modal
createWeb3Modal({ wagmiConfig, projectId, chains })

//4. (임시) comment 테스트용
import Comment from "./pages/Comment"

function App() {
  const [TFAuthed, setTFAAuthed] = useState(false)
  const [principal, setPrincipal] = useState("")
  const [commentPrincipal, setCommentPrincipal] = useState("")
  const [searchPrincipal, setSearchPrincipal] = useState("")

  return (
    <WagmiConfig config={wagmiConfig}>
      {/* {isConnected && (
        <div>
          <div>Connected</div>
          <div>{principal}</div>
        </div>
      )} */}
      {/* <div>
        <ConnectButton />
      </div>
      <ConnectDialog /> */}
      <Routes>
        <Route
          path="/"
          element={
            <Login
              TFAuthed={TFAuthed}
              setTFAAuthed={setTFAAuthed}
              // principal={principal}
              setPrincipal={setPrincipal}
            />
          }
        />
        <Route
          path="/profile"
          element={
            <Profile
              principal={principal}
              setCommentPrincipal={setCommentPrincipal}
              setSearchPrincipal={setSearchPrincipal}
            />
          }
        />
        <Route
          path="/comment"
          element={
            <Comment
              principal={principal}
              commentPrincipal={commentPrincipal}
            />
          }
        />
        <Route
          path="/searchDetail"
          element={
            <SearchDetail
              principal={principal}
              searchPrincipal={
                "z3n7l-2mcjt-4nd2g-r2ikt-bq3ve-gs63m-gnq3c-bv3z6-twz6p-p6h4v-bqe"
              }
            />
          }
        />
      </Routes>
    </WagmiConfig>
  )
}

const client = createClient({
  canisters: {
    purify,
    authentication,
    httpOutcalls,
  },
  providers: defaultProviders,
  globalProviderConfig: {
    dev: import.meta.env.DEV,
  },
})

export default () => (
  <Connect2ICProvider client={client}>
    <App />
  </Connect2ICProvider>
)

# 2024 ICP Korea Hacker House

## Content
- [Hack-Challenges](#hack-challenges)
  * [Tracks](#tracks)
    + [xChain dapps  - Total Prize pool of USD 5k](#xchain-dapps----total-prize-pool-of-usd-5k)
    + [Fully on-chain dapps - Total Prize pool of USD 2k](#fully-on-chain-dapps---total-prize-pool-of-usd-2k)
    + [Build solidity smart contracts on ICP using the Bitfinity EVM - Total Prize pool of USD 3k](#build-solidity-smart-contracts-on-icp-using-the-bitfinity-evm---total-prize-pool-of-usd-3k)
  * [Resources](#resources)
  * [Submission](#submission)
  * [Judging Criteria](#judging-criteria)



## Hack-Challenges

### Tracks

Here are the four different tracks we have open and some example ideas you can
inspire yourself from/copy

#### xChain dapps  - Total Prize pool of USD 5k


The Internet Computer is a unique platform to build cross-chain dapps. In the
workshops, you’ll get introduced to threshold ECDSA, the Bitcoin integration,
ckBTC, HTTPS outcalls, and the IC-ETH-Starter template.

In this track, you are challenged to use one or more of these powerful building
blocks to create a novel application. In the following, we have listed some
projects you could tackle.

##### Prizes

1. USD 3k in ICP grants
2. USD 2k in ICP grants

You can pick ideas from https://github.com/dfinity/grant-rfps/blob/main/requests-for-startups.md

These bounties focus on adding to the IC-ETH-Starter template which is a starter
project that uses an IC canister to verify the ownership of Ethereum NFTs:
https://github.com/dfinity/ic-eth-starter

**How to Get Started:**
1.  Attend the Integrating with Ethereum session in the hacker house or watch this [Youtube tutorial video](https://www.youtube.com/watch?v=gEuI43g1m94) to get an overview of the IC-ETH-Starter project.
2.  Clone/Fork the IC-ETH-Starter template.

##### Add verification of ERC-20 tokens. (Beginner)
The IC-ETH-Starter can currently verify ownership of ERC-721 and ERC-1155 NFTs. Add support for ERC-20 tokens.

**Requirements:**
-   A user would input an Etherscan link to an ERC20 smart contract address in the OpenSea / Etherscan link section on the Verify page.
-   Once a user clicks on Verify Token, the app will request the user to sign and verify if the user does own a positive balance of the ERC20 token.
-   The ERC20 token verification with the user wallet address will be listed on the Home page.

**Hints:**

-   Enable ERC-20 as a token type
    -   Add erc20 as a token type on Line 11 in `src/services/HistoryService.ts`
-   Add a function to verify ownership of an ERC-20 token
    -   Add a function similar to `erc721_owner_of` and `erc1155_balance_of` to check
    for the user balance of the ERC20 token.

-   Enable the user to input a link from Ethereum that references an ERC20 smart contract
    -   Adjust the `verifyNFT` function in `src/components/WalletArea.tsx` to check the user balance of the ERC20 token using the function created in Add a function to verify ownership of an ERC-20 token.

[**Full Tutorial Link**](/erc20icp.md)

##### Add other ownership details. (Beginner)

The IC-ETH-Starter shows the NFT image, name, and link to OpenSea alongside the wallet address of the user who verified ownership of the NFT. Provide owners with other information on their verified NFTs such as:

-   date and time of purchase
-   the purchase amount in ETH
-   the name of the NFT collection

**Requirements:**
-   Upon token verification, the verification listed on the Home page will list additional details than currently provided.

**Hints:**
-   Add a function that queries additional data on NFTs
    -   Add a function that queries additional data on NFTs in `src/components/WalletArea.tsx`
    -   Call the function in the VerifyNFT function when verifying the ownership of the NFT

-   Save the data
    -   Update the NFT type and `addNFTs` function to accept additional data that you would like to save about the ownership of the NFT
    -   Enable verification of NFTs on other chains. (Beginner)

#### Enable verification of NFTs on other chains. (Intermediate)

The IC-ETH-Starter can currently verify ownership of ERC-721 and ERC-1155 NFTs on Ethereum and its testnets (Goerli and Sepolia). Adding support for other EVM-compatible chains, such as:

-   Arbitrum
-   Avalanche
-   Optimism
-   Polygon
-   Binance Smart Chain

**Requirements:**
-   A user can input a block explorer link to an NFT on another chain. You can optionally add a dropdown that allows the user to select the chain of the NFT that they want to verify ownership of.
-   Click "Verify Token" and sign your message.
-   The app will verify that you own the NFT.

**Hints:**

-   Add a corresponding chain RPC
    -   Add the corresponding chain RPC under the get_rpc_endpoint function in `canisters/ic_eth/src/eth_rpc.rs`

-   Adjust functions to accept the new chain
    -   Adjust `parseOpenSeaNft` to accept a newly supported chain. Add another parse function to handle a URL link from a supported block explorer

##### Create attestations during verification (Advanced)

Verified owners should be able to add notes to the verification process. Upon
verifying their NFT ownership, owners can add different notes under their
verified NFTs. Bonus points if you can keep the notes only visible to certain
Internet Computer principals using vetKeys.

**Requirements:**
-   A user inputs the block explorer link to the NFT you want to verify and add a message.
-   Click "Verify Token" and sign your message.
-   The app will verify that you own the NFT.
-   If the verification is successful, your NFT verification details and message will be displayed on the home page.

**Resources:**

[vetKeys](https://internetcomputer.org/docs/current/developer-docs/integrations/vetkeys/technology-overview)

##### Create a Chat Between Verified Owners of the NFTs in the same Collection (Advanced)
Create a chat between verified owners of any NFT collection. For example, if an
NFT owner verifies their ownership of a Bored Ape, the app will open a chat
between any other Bored Ape owners who have verified their ownership. You can
gamify the experience by giving the first x owners to verify their NFTs in a
collection a special reward, such as an NFT or token.

**Requirements:**
-   A chat that is only visible to wallet addresses that verified NFTs/tokens in the same collection.

#### Ideas to build with the Bitcoin integration and ckBTC

##### Augment PoS example to fully fledged product website - virtual store with ckBTC (Intermediate)
Using the [POS](https://internetcomputer.org/docs/current/samples/pos) example, from the internet computer documentation. You can extend it
to have an inventory of items and

##### Add NFC payments to the PoS example dapp (Advanced)
Extend the last POS example to request payments via NFC instead of QR code!

##### File Sharing Payment (Advanced)
Upload files locally and exchange them with various principals through ckBTC.

The cli utility wrapping around dfx should upload binary blob with a hash -
anyone should be able buy a hash for a given amount - would get copied to their own storage canister - which they would be allowed to download.


#### Fully on-chain dapps - Total Prize pool of USD 2k

Utilize the Internet Computer to build a fully on-chain dapp. Consider using a
starter project or a sample dapp to kickstart your development. Here are some
possible directions you could take


-   **SocialFi applications** - Social interactions on chains means that users own their data. On ICP you can host websites and interact with smart canisters directly from the browser. On top of this, ICP has the smoothest user onboarding experience. No wallet necessary.
-   **DAO tooling** - ICP has the capability of hosting the entire governance process for DAOs on-chain. Show us what you can build to make DAOs thrive.
-   **Privacy focused applications** - Blockchains in general are not the best for privacy of data. vetKeys on ICP help you build dapps in which users can preserve their privacy.
-   **RWA DeFi protocols** - With https outcalls, ICP canisters can bring in real world data streams on chain. We are looking for projects that tokenize new assets and bring them on chain.

##### Prizes

1. USD 1k in ICP grants
2. USD 0.5k in ICP grants


#### Build solidity smart contracts on ICP using the Bitfinity EVM - Total Prize pool of USD 3k

[Bitfinity](Bitfinity is an Ethereum Virtual Machine (EVM) implemented as a smart contract on Internet Computer Protocol) is an Ethereum Virtual Machine (EVM) implemented as a smart contract on Internet Computer Protocol
It helps scale the Ethereum ecosystem for developers to operate their apps on an Ethereum-compatible, high-throughput, scalable, and future-safe platform, with low transaction costs for their users.

Here are some suggested themes. Projects are not limited to these verticals.

- DeFi (e.g. AMM, Aggregator, Lending & Borrowing, etc.)
- NFT & Gaming (e.g. NFT Platform, POAP dApp, Marketplace, Web3 Game etc.)
- Infrastructure (e.g. Remix, Bridge, Text Messaging, Internet Identity etc.)


##### Prizes

1. USD 2k in ICP
2. USD 1k in ICP



## Resources
### General
- [101 Tutorial](https://dacade.org/ko/communities/icp/courses/typescript-smart-contract-101/learning-modules/b14741ea-ee33-43a4-a742-9cdc0a6f0d1c)
- [ICP Hackathon Cheatsheet](https://dfinityorg.notion.site/ICP-Hackathon-Cheat-Sheet-b2921239266149de81021412f572351c)
- [Awesome Internet Computer Dev Resources](https://github.com/dfinity/awesome-internet-computer)
- [ICP Zero to Dapp Series](https://youtube.com/playlist?list=PLuhDt1vhGcrcRcHvSKmxIgJAh1b3rcR7N&si=Dy3Zm1t5oBJY916n)
- [Rust CDK docs](https://docs.rs/ic-cdk/latest/ic_cdk/)
- [AI DevRel](https://icp-ai-chat-frontend.vercel.app/)
- [Azel Book](https://demergent-labs.github.io/azle/the_azle_book.html)
- [Various Courses in multiple languages](https://dacade.org/communities/icp)
- [BITFINITY EVM](https://docs.bitfinity.network/)
- [Tutorial BITFINITY](https://www.youtube.com/playlist?list=PLUDcVqFK2t-C36KaTMzV3y1wu8Hf-qyAI)

### One-click development environments for macOS, Windows and Linux:
- [101 Tutorial with Azle](https://github.com/dacadeorg/icp-message-board-contract)
- [Azle and React Frontend & Backend Starter](https://github.com/fxgst/azle-react)
- [Rust, React and EVM RPC canister](https://github.com/fxgst/evm-rpc-rust)
  

### Cycles

Ask your hacker house mentor

## Submission
To submit a project for the hackathon - raise a pull request and  every commit
before the deadline will be considered.

Your submission needs to be in `submissions/`

In order for your PR to be considered you need to have a `README.md` file at the
root of the repository which contains:
-   project description
-   track
-   team participants
    -   please use GitHub username for everyone
    -   Discord IDs
-   canister ID(s) of mainnet deployment
-   feedback part
    -   what have you learned
    -   what were the challenges you ran into
    -   what are you proud of

## Judging

### Presentations and judging
While the mentors will be judging submissions,
each group will be presenting the project to hackers around their table in a
small committee.

The winner(s) of each track will then present their project on stage.

### Judging Criteria

-   **X Factor and Potential**
    -   What is the wow factor of the idea? Does it have potential?
-   **Technical Competence**
    -   What is the quality of the technical designs and source code?
-   **Impact**
    -   Does this project solve a real-world problem? Can people use it?
-   **Progression**
    -   Can a library be used? Is a service actually usable? How far did you go?
-   **IC Superpowers**
    -   How well did you utilize the superpowers of the Internet Computer?

[DFINITY Hackathon Terms and Conditions](https://www.notion.so/dfinityorg/DFINITY-HACKATHON-TERMS-AND-CONDITIONS-Fully-On-Chain-with-ICP-1faf692d49a64c89afb5589a7219eee2?pvs=4)

require('dotenv').config();
require("@nomiclabs/hardhat-ethers");
require("solidity-coverage");
require("hardhat-gas-reporter");


/** @type import('hardhat/config').HardhatUserConfig */
module.exports = {
  solidity: "0.8.20",
  plugins: ["solidity-coverage"],
  networks: {
    hardhat: {},
    polygon_mumbai: {
      url: "https://polygon-mumbai.g.alchemy.com/v2/vXdmKq0dv2MoTv43E41Jt1RNiWcGEggj",
      accounts: [process.env.PRIVATE_KEY]
    },
  }
};
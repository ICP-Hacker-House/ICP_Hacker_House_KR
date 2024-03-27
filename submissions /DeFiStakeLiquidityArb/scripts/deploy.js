require('dotenv').config();
const hre = require("hardhat");

async function main() {
  const [deployer] = await hre.ethers.getSigners();

  console.log("Deploying contracts with the account:", deployer.address);

  try {
    const MyToken = await hre.ethers.getContractFactory("MyToken");
    const initialSupply = hre.ethers.utils.parseUnits(process.env.INITIAL_SUPPLY, 18);
    const myToken = await MyToken.deploy(initialSupply, { gasPrice: hre.ethers.utils.parseUnits(process.env.GAS_PRICE_GWEI, 'gwei') });
    await myToken.deployed();
    console.log("MyToken deployed to:", myToken.address);

    try {
      const TokenStake = await hre.ethers.getContractFactory("TokenStake");
      const tokenStake = await TokenStake.deploy(myToken.address, { gasPrice: hre.ethers.utils.parseUnits('10', 'gwei') });
      await tokenStake.deployed();
      console.log("TokenStake deployed to:", tokenStake.address);

      try {
        const LiquidityProviderPool = await hre.ethers.getContractFactory("LiquidityProviderPool");
        const gasPriceGwei = process.env.GAS_PRICE_GWEI;
        const gasPrice = hre.ethers.utils.parseUnits(gasPriceGwei, 'gwei');
        const liquidityProviderPool = await LiquidityProviderPool.deploy(myToken.address, deployer.address, { gasPrice });
        await liquidityProviderPool.deployed();
        console.log("LiquidityProviderPool deployed to:", liquidityProviderPool.address);
      } catch (error) {
        console.error("An error occurred while deploying LiquidityProviderPool:", error);
        process.exit(1);
      }
    } catch (error) {
      console.error("An error occurred while deploying TokenStake:", error);
      process.exit(1);
    }
  } catch (error) {
    console.error("An error occurred while deploying MyToken:", error);
    process.exit(1);
  }
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error("An error occurred during the deployment process:", error);
    process.exit(1);
  });
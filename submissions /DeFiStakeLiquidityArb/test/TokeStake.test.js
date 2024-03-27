const { expect } = require("chai");
const { ethers } = require("hardhat");

describe("TokenStake contract", function () {
  let Token;
  let token;
  let TokenStake;
  let tokenStake;
  let owner;
  let user1;
  let user2;
  let Core;

  const initialSupply = ethers.utils.parseUnits("1000000", 18);

  beforeEach(async function () {
    // Deploy MyToken
    Token = await ethers.getContractFactory("MyToken");
    token = await Token.deploy(initialSupply);
    [owner, user1, user2, Core] = await ethers.getSigners();
  
    // Deploy TokenStake contract
    TokenStake = await ethers.getContractFactory("TokenStake");
    tokenStake = await TokenStake.deploy(token.address);
  
    // Transfer some tokens to user1 for testing
    await token.transfer(user1.address, ethers.utils.parseUnits("1000", 18));
  
    // Transfer some tokens to tokenStake contract
    await token.transfer(tokenStake.address, ethers.utils.parseUnits("1000", 18));
  });

  describe("Deployment", function () {
    it("Should deploy with the correct token address", async function () {
      expect(await tokenStake.token()).to.equal(token.address);
    });
  });

  describe("Stake functionality", function () {
    it("Allows a user to stake tokens", async function () {
      const stakeAmount = ethers.utils.parseUnits("100", 18);
      await token.connect(user1).approve(tokenStake.address, stakeAmount);
      await tokenStake.connect(user1).stake(user1.address, stakeAmount);
  
      const stake = await tokenStake.stakes(user1.address);
      expect(stake.amount.eq(stakeAmount)).to.be.true; // Use eq to compare BigNumbers
    });
  
    it("Updates total staked amount correctly", async function () {
      const stakeAmount = ethers.utils.parseUnits("100", 18);
      await token.connect(user1).approve(tokenStake.address, stakeAmount);
      await tokenStake.connect(user1).stake(user1.address, stakeAmount);
  
      const totalStaked = await tokenStake.totalStaked();
      expect(totalStaked.eq(stakeAmount)).to.be.true; // Use eq to compare BigNumbers
    });
  });
  
  describe("Unstake functionality", function () {
    it("Allows a user to unstake tokens", async function () {
      const stakeAmount = ethers.utils.parseUnits("100", 18);
      await token.connect(user1).approve(tokenStake.address, stakeAmount);
      await tokenStake.connect(user1).stake(user1.address, stakeAmount);
  
      const balanceBefore = await token.balanceOf(user1.address);
      console.log(`Balance before unstaking: ${balanceBefore.toString()}`);
  
      await tokenStake.connect(user1).unstake(stakeAmount, user1.address);
  
      const balanceAfter = await token.balanceOf(user1.address);
      console.log(`Balance after unstaking: ${balanceAfter.toString()}`);
  
      const stake = await tokenStake.stakes(user1.address);
      expect(stake.amount.eq(0)).to.be.true; // Use eq to compare BigNumbers
    });
  });

  describe("Claim rewards", function () {
    it("Allows a user to claim earned rewards", async function () {
      // User stakes tokens
      const stakeAmount = ethers.utils.parseUnits("100", 18);
      await token.connect(user1).approve(tokenStake.address, stakeAmount);
      await tokenStake.connect(user1).stake(user1.address, stakeAmount);
  
      // Simulate time passing for rewards to accumulate
      await ethers.provider.send("evm_increaseTime", [3600 * 24 * 30]); // Simulate 30 days
      await ethers.provider.send("evm_mine");
  
      // User claims rewards
      const initialBalance = await token.balanceOf(user1.address);
      await tokenStake.connect(user1).claimRewards();
      const finalBalance = await token.balanceOf(user1.address);
  
      expect(finalBalance.gt(initialBalance)).to.be.true; // Ensure user's balance increased
    });
  });
  

  describe("Restake rewards", function () {
    it("Allows a user to restake earned rewards", async function () {
      // User stakes tokens
      const stakeAmount = ethers.utils.parseUnits("100", 18);
      await token.connect(user1).approve(tokenStake.address, stakeAmount);
      await tokenStake.connect(user1).stake(user1.address, stakeAmount);
  
      // Simulate time for rewards to accumulate
      await ethers.provider.send("evm_increaseTime", [3600 * 24 * 30]); // Simulate 30 days
      await ethers.provider.send("evm_mine");
  
      // User restakes rewards
      const initialStake = await tokenStake.stakes(user1.address);
      await tokenStake.connect(user1).restake();
      const finalStake = await tokenStake.stakes(user1.address);
  
      expect(finalStake.amount.gt(initialStake.amount)).to.be.true; // Ensure staked amount increased
    });
   });
  

   /*describe("Core contract integration", function () {
    let liquidityProviderPool;
    let arbitrageBot;
    let core;
  
    before(async function () {
      // Deploy additional required contracts
      const LiquidityProviderPoolFactory = await ethers.getContractFactory("LiquidityProviderPool");
      liquidityProviderPool = await LiquidityProviderPoolFactory.deploy(token.address, owner.address);
      await liquidityProviderPool.deployed();
  
      const ArbitrageBotFactory = await ethers.getContractFactory("ArbitrageBot");
      arbitrageBot = await ArbitrageBotFactory.deploy(owner.address, token.address);
      await arbitrageBot.deployed();
  
      // Deploy Core contract
      const CoreFactory = await ethers.getContractFactory("Core");
      core = await CoreFactory.deploy(token.address, tokenStake.address, liquidityProviderPool.address, arbitrageBot.address);
      await core.deployed();
    });
  
    it("Allows Core contract to restake for a user through handleRewardsAndRestake", async function () {
      const stakeAmount = ethers.utils.parseUnits("100", 18);
  
      // Transfer tokens to user1 and set allowance for Core contract
      await token.transfer(user1.address, stakeAmount);
      await token.connect(user1).approve(core.address, ethers.constants.MaxUint256);
  
      // Stake tokens through Core contract
      await core.connect(user1).stakeTokens(stakeAmount);
  
      // Simulate time for rewards to accumulate
      await ethers.provider.send("evm_increaseTime", [3600 * 24 * 30]);
      await ethers.provider.send("evm_mine");
  
      // Initial stake for verification
      const initialStake = await tokenStake.stakes(user1.address);
  
      // Call handleRewardsAndRestake on Core contract
      await core.handleRewardsAndRestake();
  
      // Final stake for comparison
      const finalStake = await tokenStake.stakes(user1.address);
  
      // Verify if the staked amount has increased, indicating successful restake
      expect(finalStake.amount).to.be.gt(initialStake.amount);
  
      // Optionally, verify balances and allowances if necessary
      const finalUserBalance = await token.balanceOf(user1.address);
      console.log(`Final user balance: ${finalUserBalance.toString()}`);
      const coreAllowance = await token.allowance(user1.address, core.address);
      console.log(`Core allowance after restaking: ${coreAllowance.toString()}`);
    });
  });*/
  
  
});
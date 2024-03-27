// test/ArbitrageBot.test.js

const { expect } = require("chai");
const { ethers } = require("hardhat");

describe("ArbitrageBot", function () {
  let ArbitrageBot, arbitrageBot, owner, addr1, addr2, token;

  beforeEach(async function () {
    // Deploy the MyToken contract
    const MyToken = await ethers.getContractFactory("MyToken");
    token = await MyToken.deploy(ethers.utils.parseEther("10000"));

    // Deploy the ArbitrageBot contract
    ArbitrageBot = await ethers.getContractFactory("ArbitrageBot");
    [owner, addr1, addr2, ...addrs] = await ethers.getSigners();
    arbitrageBot = await ArbitrageBot.deploy(owner.address, token.address);

    // Owner approves the contract to spend tokens
    await token.connect(owner).approve(arbitrageBot.address, ethers.utils.parseEther("1000"));
  });

  describe("Deployment", function () {
    it("Should set the right owner", async function () {
      expect(await arbitrageBot.owner()).to.equal(owner.address);
    });

    it("Should set the right token", async function () {
      expect(await arbitrageBot.token()).to.equal(token.address);
    });
  });

  describe("receiveTokenFunds", function () {
    it("Should receive token funds correctly", async function () {
      await arbitrageBot.receiveTokenFunds(ethers.utils.parseEther("100"));
      expect((await arbitrageBot.tokenFundsAvailable()).eq(ethers.utils.parseEther("100"))).to.be.true;
    });

    it("Should emit FundsReceived event correctly", async function () {
      // Define the amount of tokens to be received
      const amount = ethers.utils.parseEther("100");
    
      // Call the notifyTokenReceived function
      const tx = await arbitrageBot.connect(owner).notifyTokenReceived(amount);
    
      // Wait for the transaction to be mined and get the receipt
      const receipt = await tx.wait();
    
      // Find the FundsReceived event in the receipt
      const event = receipt.events?.find(e => e.event === "FundsReceived");
    
      // Check that the FundsReceived event was emitted with the correct parameters
      expect(event).to.not.be.undefined;
      expect(event.args.from).to.equal(owner.address);
      expect(event.args.amount.eq(amount)).to.be.true;
    });
  });

  describe("executeArbitrage", function () {
    it("Should execute arbitrage correctly", async function () {
      // Define the initial amount of tokens
      const initialAmount = ethers.utils.parseEther("100");
  
      // Call the receiveTokenFunds function to add some funds to the contract
      await arbitrageBot.receiveTokenFunds(initialAmount);
  
      // Call the executeArbitrage function
      const tx = await arbitrageBot.connect(owner).executeArbitrage();
  
      // Wait for the transaction to be mined and get the receipt
      const receipt = await tx.wait();
  
      // Find the ArbitrageExecuted event in the receipt
      const event = receipt.events?.find(e => e.event === "ArbitrageExecuted");
  
      // Check that the ArbitrageExecuted event was emitted with the correct profit
      expect(event).to.not.be.undefined;
      expect(event.args.profit.eq(initialAmount.div(10))).to.be.true;
  
      // Check that tokenFundsAvailable has increased by 10%
      const finalAmount = await arbitrageBot.tokenFundsAvailable();
      expect(finalAmount.eq(initialAmount.add(initialAmount.div(10)))).to.be.true;
    });
  });

  describe("withdrawProfit", function () {
    it("Should withdraw profit correctly", async function () {
      // First, send some tokens to the contract
      await token.transfer(arbitrageBot.address, ethers.utils.parseEther("500"));
    
      // Then, receive some token funds
      await arbitrageBot.receiveTokenFunds(ethers.utils.parseEther("500"));
    
      // Print out the balance of the contract before the withdraw
      let contractBalanceBefore = await token.balanceOf(arbitrageBot.address);
      console.log(`Contract balance before: ${ethers.utils.formatEther(contractBalanceBefore)}`);
    
      // Then, withdraw some profits
      await arbitrageBot.withdrawProfit(owner.address, ethers.utils.parseEther("100"));
    
      // Print out the balance of the contract after the withdraw
      let contractBalanceAfter = await token.balanceOf(arbitrageBot.address);
      console.log(`Contract balance after: ${ethers.utils.formatEther(contractBalanceAfter)}`);
    
      // Check the token balance of the contract
      expect(contractBalanceAfter.eq(ethers.utils.parseEther("900"))).to.be.true;
    });
  
    it("Should fail when trying to withdraw more profits than available", async function () {
      // Try to withdraw more profits than available
      try {
        await arbitrageBot.withdrawProfit(owner.address, ethers.utils.parseEther("10000"));
        // If the transaction doesn't revert, force the test to fail
        expect.fail("Expected withdrawProfit to revert");
      } catch (error) {
        // Check the error message
        expect(error.message).to.include("revert");
      }
    });
  });

  
  describe("withdrawToken", function () {
    it("Should withdraw token correctly", async function () {
      // First, send some tokens to the contract
      await token.transfer(arbitrageBot.address, ethers.utils.parseEther("500"));
  
      // Then, withdraw some tokens
      await arbitrageBot.withdrawToken(owner.address, token.address, ethers.utils.parseEther("100"));
  
      // Check the token balance of the contract
      const contractBalance = await token.balanceOf(arbitrageBot.address);
      expect(contractBalance.eq(ethers.utils.parseEther("400"))).to.be.true;
    });
  
    it("Should fail when trying to withdraw more tokens than available", async function () {
      // Try to withdraw more tokens than available
      try {
        await arbitrageBot.withdrawToken(owner.address, token.address, ethers.utils.parseEther("10000"));
        // If the transaction doesn't revert, force the test to fail
        expect.fail("Expected withdrawToken to revert");
      } catch (error) {
        // Check the error message
        expect(error.message).to.include("revert");
      }
    });
  });
  // Add more tests for other functions
});
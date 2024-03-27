const { expect } = require("chai");
const { ethers } = require("hardhat");

describe("MyToken contract", function () {
  let MyToken;
  let myToken;
  let owner;
  let addr1;
  let addr2;
  let addrs;

  beforeEach(async function () {
    MyToken = await ethers.getContractFactory("MyToken");
    [owner, addr1, addr2, ...addrs] = await ethers.getSigners();

    myToken = await MyToken.deploy(1000000); // Pass the initial supply as '1000000' (1 million)
  });

  describe("Deployment", function () {
    it("Has a name", async function () {
      expect(await myToken.name()).to.equal("MyToken");
    });

    it("Has a symbol", async function () {
      expect(await myToken.symbol()).to.equal("MTK");
    });

    it("Assigns the initial total supply to the owner", async function () {
      const ownerBalance = await myToken.balanceOf(owner.address);
      // The expected value is '1000000' tokens, adjusted for 18 decimals
      expect(ownerBalance.toString()).to.equal(ethers.utils.parseUnits("1000000", 18).toString());
    });
  });
  describe("Transactions", function () {
    it("Should transfer tokens between accounts", async function () {
      // Transfer 50 tokens from owner to addr1
      await myToken.connect(owner).transfer(addr1.address, ethers.utils.parseUnits("50", 18));
      const addr1Balance = await myToken.balanceOf(addr1.address);
      expect(addr1Balance.toString()).to.equal(ethers.utils.parseUnits("50", 18).toString());
  
      // Transfer 50 tokens from addr1 to addr2
      await myToken.connect(addr1).transfer(addr2.address, ethers.utils.parseUnits("50", 18));
      const addr2Balance = await myToken.balanceOf(addr2.address);
      expect(addr2Balance.toString()).to.equal(ethers.utils.parseUnits("50", 18).toString());
    });
  });
  describe("ERC20", function () {
    it("Should return the correct total supply", async function () {
      const totalSupply = await myToken.totalSupply();
      expect(totalSupply.toString()).to.equal(ethers.utils.parseUnits("1000000", 18).toString());
    });
  });
});
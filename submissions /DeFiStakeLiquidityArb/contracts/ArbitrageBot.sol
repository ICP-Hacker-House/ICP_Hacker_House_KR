// SPDX-License-Identifier: MIT

pragma solidity ^0.8.18;

import "@openzeppelin/contracts/access/Ownable.sol"; 
import "@openzeppelin/contracts/utils/ReentrancyGuard.sol"; 
import "@openzeppelin/contracts/token/ERC20/IERC20.sol";

contract ArbitrageBot is Ownable, ReentrancyGuard {
    IERC20 public token;
    uint256 public tokenFundsAvailable;

    event FundsReceived(address from, uint256 amount);
    event ArbitrageExecuted(uint256 profit);

    constructor(address initialOwner, address tokenAddress) Ownable(initialOwner) {
        token = IERC20(tokenAddress);
    }

    // Function to receive ERC20 token funds
    function receiveTokenFunds(uint256 amount) external {
        token.transferFrom(msg.sender, address(this), amount);
        tokenFundsAvailable += amount;
        emit FundsReceived(msg.sender, amount);
    }

    // Function to perform arbitrage
    function executeArbitrage() external onlyOwner nonReentrant {
        uint256 profit = tokenFundsAvailable / 10; // Assume 10% profit for demonstration
        tokenFundsAvailable += profit;
        emit ArbitrageExecuted(profit);
    }
        
    // Function to notify that funds have been received
    function notifyTokenReceived(uint256 amount) external onlyOwner {
        emit FundsReceived(msg.sender, amount); // Log the event of receiving funds
    }

    // Function to withdraw profits from the contract
    function withdrawProfit(address to, uint256 amount) external onlyOwner {
    require(amount <= tokenFundsAvailable, "Insufficient token funds."); // Ensure there are enough token funds
    tokenFundsAvailable -= amount;
    require(token.transfer(to, amount), "Token transfer failed."); // Transfer the specified amount of tokens to the provided address
    }

    // Function to withdraw any ERC20 token from the contract
    function withdrawToken(address to, address tokenAddress, uint256 amount) external onlyOwner {
        IERC20 tokenToWithdraw = IERC20(tokenAddress);
        uint256 balance = tokenToWithdraw.balanceOf(address(this));
        require(amount <= balance, "Insufficient token balance.");
        tokenToWithdraw.transfer(to, amount);
    }
}
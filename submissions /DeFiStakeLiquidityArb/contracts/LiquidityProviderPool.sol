// SPDX-License-Identifier: MIT
pragma solidity ^0.8.18;

import "@openzeppelin/contracts/token/ERC20/IERC20.sol"; 
import "@openzeppelin/contracts/utils/ReentrancyGuard.sol"; 
import "@openzeppelin/contracts/access/Ownable.sol"; 

contract LiquidityProviderPool is ReentrancyGuard, Ownable {
    IERC20 public liquidityToken;
    uint256 public totalLiquidity;
    mapping(address => uint256) public providerBalances;
    mapping(address => uint256) public providerTimestamps; 
    mapping(address => uint256) public rewards;

    event LiquidityAdded(address indexed provider, uint256 amount);
    event LiquidityRemoved(address indexed provider, uint256 amount);
    event RewardPaid(address indexed provider, uint256 reward);

    constructor(address _liquidityTokenAddress, address initialOwner) Ownable(initialOwner) {
        liquidityToken = IERC20(_liquidityTokenAddress);
    }
      
    function addLiquidity(uint256 amount) external nonReentrant {
        providerBalances[msg.sender] += amount;
        providerTimestamps[msg.sender] = block.timestamp; // Update the timestamp for the provider
        totalLiquidity += amount;
        liquidityToken.transferFrom(msg.sender, address(this), amount);
        emit LiquidityAdded(msg.sender, amount);
    }

    function removeLiquidity(uint256 amount, address recipient) external nonReentrant {
        require(providerBalances[msg.sender] >= amount, "Invalid amount");
        providerBalances[msg.sender] -= amount;
        totalLiquidity -= amount;
        liquidityToken.transfer(recipient, amount); // Transfer directly to a specified recipient
        emit LiquidityRemoved(msg.sender, amount);
    }

    // function to claim rewards
    function claimRewards() external nonReentrant {
        uint256 reward = rewards[msg.sender];
        require(reward > 0, "No rewards to claim");
        rewards[msg.sender] = 0; // Reset rewards
        liquidityToken.transfer(msg.sender, reward); // Transfer rewards
        emit RewardPaid(msg.sender, reward);
    }

    // function to restake rewards
    function restake() external nonReentrant {
        uint256 reward = rewards[msg.sender];
        require(reward > 0, "No rewards to restake");
        rewards[msg.sender] = 0; // Reset rewards
        providerBalances[msg.sender] += reward; // Add rewards to liquidity
        totalLiquidity += reward; // Update total liquidity
        emit LiquidityAdded(msg.sender, reward);
    }
    
    // Function to calculate and update rewards (simplified example)
    function updateRewards(address provider, uint256 reward) public onlyOwner {
        rewards[provider] += reward;
    }
    
    // Function to calculate interest for a provider
    function calculateInterest(address provider, uint256 amount) public view returns (uint256) {
        uint256 timeElapsed = block.timestamp - providerTimestamps[provider]; // Calculate time elapsed since last interaction
        uint256 interestRatePerWeek = 1; // 0.1% interest
        uint256 weeksElapsed = timeElapsed / 1 weeks; // Calculate weeks elapsed
        uint256 interest = (amount * interestRatePerWeek * weeksElapsed) / 1000; // Calculate interest
        return interest;
    }

    // Function to get the balance of a provider
    function getBalance(address provider) external view returns (uint256) {
        return providerBalances[provider]; // Return provider's balance
    }

}

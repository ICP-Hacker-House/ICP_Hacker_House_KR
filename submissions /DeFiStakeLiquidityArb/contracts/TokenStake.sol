// SPDX-License-Identifier: MIT
pragma solidity ^0.8.18;

import "@openzeppelin/contracts/token/ERC20/IERC20.sol";
import "@openzeppelin/contracts/token/ERC20/utils/SafeERC20.sol";
import "@openzeppelin/contracts/utils/ReentrancyGuard.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract TokenStake is ReentrancyGuard, Ownable {
    using SafeERC20 for IERC20;

    IERC20 public token;
    uint256 public totalStaked;
    uint256 private constant ANNUAL_INTEREST_RATE = 10; // Example: 10% annual interest rate
    uint256 private constant INTEREST_RATE_DIVISOR = 1000; // To allow for a decimal place in the interest rate

    // State variable to store the Core contract's address
    address private coreContract;

    struct Stake {
        uint256 amount;
        uint256 timestamp;
        uint256 accruedReward;
    }
    mapping(address => Stake) public stakes;

    event Staked(address indexed user, uint256 amount, uint256 timestamp);
    event Unstaked(address indexed user, uint256 amount, uint256 timestamp);
    event RewardPaid(address indexed user, uint256 reward);

    constructor(address tokenAddress) Ownable(msg.sender) {
        token = IERC20(tokenAddress);
    }
    
    function stake(address staker, uint256 amount) external nonReentrant {
    require(amount > 0, "Amount must be greater than 0");
    updateReward(staker);

    stakes[staker].amount += amount;
    stakes[staker].timestamp = block.timestamp;

    totalStaked += amount;
    token.safeTransferFrom(staker, address(this), amount);

    emit Staked(staker, amount, block.timestamp);
}

    function unstake(uint256 amount, address recipient) external nonReentrant {
    require(amount > 0, "Amount must be greater than 0");
    require(stakes[msg.sender].amount >= amount, "Invalid unstake amount");
    updateReward(msg.sender);

    stakes[msg.sender].amount -= amount;
    totalStaked -= amount;
    uint256 reward = stakes[msg.sender].accruedReward;
    stakes[msg.sender].accruedReward = 0;

    uint256 totalAmount = amount + reward;
    require(token.balanceOf(address(this)) >= totalAmount, "Insufficient contract balance");

    token.safeTransfer(recipient, amount);
    if (reward > 0) {
        token.safeTransfer(recipient, reward); // Directly transfer the reward if any
    }

    emit Unstaked(msg.sender, amount, block.timestamp);
}

    function claimRewards() external nonReentrant {
        updateReward(msg.sender);
        uint256 reward = stakes[msg.sender].accruedReward;
        require(reward > 0, "No rewards to claim");

        stakes[msg.sender].accruedReward = 0;
        token.safeTransfer(msg.sender, reward);

        emit RewardPaid(msg.sender, reward);
    }

    function updateReward(address user) private {
        Stake storage userStake = stakes[user];
        uint256 timeElapsed = block.timestamp - userStake.timestamp;
        if (timeElapsed > 0 && userStake.amount > 0) {
            uint256 reward = (userStake.amount * ANNUAL_INTEREST_RATE * timeElapsed) / (365 days * INTEREST_RATE_DIVISOR);
            userStake.accruedReward += reward;
            userStake.timestamp = block.timestamp;
        }
    }

    function restake() external nonReentrant {
        updateReward(msg.sender); // Ensure the latest rewards are calculated
        uint256 reward = stakes[msg.sender].accruedReward;
        require(reward > 0, "No rewards to restake");

        stakes[msg.sender].amount += reward; // Add rewards to stake
        totalStaked += reward; // Update total staked amount
        stakes[msg.sender].accruedReward = 0; // Reset accrued rewards

        emit Staked(msg.sender, reward, block.timestamp); // Log the restaking
    }

}
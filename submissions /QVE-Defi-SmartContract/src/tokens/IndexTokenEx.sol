// SPDX-License-Identifier: MIT
pragma solidity ^0.8.10;

import "@openzeppelin/contracts/token/ERC20/extensions/ERC20Burnable.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract Ethereum is ERC20Burnable, Ownable {
    uint256 public supply;

    // [------ Init -------] //
    constructor() ERC20("Ethereum", "ETH") {
        supply = 1000000000;
        _mint(msg.sender, supply * 10 ** 18);
    }

    // [------ functions -------] //
    function normal_transfer(address from, address target, uint256 amount) public returns(bool){
        _transfer(from, target, amount);
        return true;
    }
}

contract Solana is ERC20Burnable, Ownable {
    uint256 public supply;

    // [------ Init -------] //
    constructor() ERC20("Solana", "SOL") {
        supply = 1000000000;
        _mint(msg.sender, supply * 10 ** 18);
    }

    // [------ functions -------] //
    function normal_transfer(address from, address target, uint256 amount) public returns(bool){
        _transfer(from, target, amount);
        return true;
    }
}


// SPDX-License-Identifier: MIT
pragma solidity ^0.8.10;

import "@openzeppelin/contracts/token/ERC20/extensions/ERC20Burnable.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract QLPtoken is ERC20Burnable, Ownable {
    uint256 public supply;

    // [------ Init -------] //
    constructor() ERC20("QLP", "QLP") {
        supply = 10000;
        _mint(msg.sender, supply * 10 ** 18);
    }

    // [------ functions -------] //
    function normal_transfer(address from, address target, uint256 amount) public returns(bool){
        _transfer(from, target, amount);
        return true;
    }

    function exchangeWithIndex(address sender, uint256 indexAmount) public returns(bool){
        _mint(sender, indexAmount);
        return true;
    }
}
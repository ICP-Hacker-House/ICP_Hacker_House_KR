// SPDX-License-Identifier: MIT
pragma solidity ^0.8.10;

import "@openzeppelin/contracts/token/ERC20/extensions/ERC20Burnable.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "../util/Security.sol";
import "@openzeppelin/contracts/utils/math/SafeMath.sol";


contract QVEtoken is ERC20Burnable, Ownable, Security {
    uint256 public initialSupply;
    using SafeMath for uint256;
    // [------ Init -------] //
    constructor() ERC20("QVE", "QVE") {
        initialSupply = 0; 
        _mint(msg.sender, initialSupply.mul(1e18));
    }

    // [------ Event ------] //
    event TransferEvent(address from, address to, uint256 amount);
    event BurnEvent(address from, uint256 amount);
    event MintEvent(address to, uint256 amount);

    // [------ functions -------] //
    function normal_transfer(address from, address target, uint256 amount) NoReEntrancy public returns(bool){
        super._transfer(from, target, amount);

        emit TransferEvent(from, target, amount);
        return true;
    }

    function normal_burn(address from, uint256 amount) public returns(bool){
        super._burn(from, amount);

        emit BurnEvent(from, amount);
        return true;
    }

    function normal_mint(address account, uint256 amount) public NoReEntrancy returns(bool){
        super._mint(account, amount);

        emit MintEvent(account, amount);
        return true;
    }

    function via_transfer(address via, address owner, address to, uint256 amount) public returns(bool){
        super._approve(owner, via, amount);
        super._transfer(owner, to, amount);

        emit TransferEvent(owner, to, amount);
        return true;
    }
    function getBalance(address sender) public view returns(uint){
        return super.balanceOf(sender);
    }
}

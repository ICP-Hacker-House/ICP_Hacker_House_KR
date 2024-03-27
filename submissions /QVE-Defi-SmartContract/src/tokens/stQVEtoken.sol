// SPDX-License-Identifier: MIT
pragma solidity ^0.8.10;

import "@openzeppelin/contracts/token/ERC20/extensions/ERC20Burnable.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/utils/math/SafeMath.sol";
import "../util/Security.sol";

contract stQVEtoken is ERC20Burnable, Ownable, Security {
    using SafeMath for uint256;
    uint256 public initialSupply;

    // [------ Events ------] //
    event stQVEminted(address account, uint256 amount);
    event stQVEtransfered(address from, address target, uint256 amount);
    event stQVEburned(address from, uint256 amount, uint256 remainedTokenSupply);

    // [------ Init -------] //
    constructor() ERC20("stQVE", "stQVE") {
        initialSupply = 0;
        _mint(msg.sender, initialSupply.mul(1e18));
    }


    // [------ functions -------] //
    function normal_transfer(address from, address target, uint256 amount) NoReEntrancy public returns(bool){
        super._transfer(from, target, amount);

        emit stQVEtransfered(from, target, amount);
        return true;
    }

    function normal_mint(address account, uint256 amount) public returns(bool) {
        super._mint(account, amount);

        emit  stQVEminted(account, amount);
        return true;
    }

    function normal_burn(address account, uint256 amount) public returns(bool){
        super._burn(account, amount);
        
        emit stQVEburned(account, amount, super.totalSupply());
        return true;
    }

    function via_transfer(address via, address owner, address to, uint256 amount) public returns(bool){
        super._approve(owner, via, amount);
        super._transfer(owner, to, amount);

     
        return true;
    }

}

// SPDX-License-Identifier: MIT
pragma solidity ^0.8.10;

import "@openzeppelin/contracts/token/ERC20/extensions/ERC20Burnable.sol";
import "@openzeppelin/contracts/utils/Strings.sol";
import "./tokens/QVEtoken.sol";
import "./util/Security.sol";
import "@openzeppelin/contracts/utils/math/SafeMath.sol";


contract QVEescrow is ERC20Burnable, Security {
    using SafeMath for uint256;
    using Strings for *;
    QVEtoken qveToken;
    address public esQVEVestingAddress;

    // [------ Events ------] //
    event MintEvent(address receiver, uint256 mintAmount);

    // [------ Variables, Constants, Mappings ------] //
    uint256 private supply;
    uint256 constant private LOCK_UP_DAYS = 90 days;
    uint8 public constant LOCKUP_ESCROWRATIO = 30;

    struct escrowed{
        uint256 amount;
        uint256 at;
    }
    mapping (address => escrowed) private escrowedQVE;


    constructor(QVEtoken _qveToken) ERC20("esQVE", "esQVE") {
        supply = 0;
        qveToken = _qveToken;
        _mint(msg.sender, supply.mul(1e18));
    }

    // [------ Token functions -------] //
    function normal_transfer(address from, address target, uint256 amount) public NoReEntrancy returns(bool){
        _transfer(from, target, amount);
        return true;
    }

    function burnEsQVE(uint amount, address sender) public returns(bool){
        _burn(sender, amount);
        return true;
    }

    // [------ external functions ------] //
    function makeQVEescrow(address sender, uint256 QVEamount) external returns(bool){
        require(qveToken.normal_transfer(msg.sender, address(this), QVEamount), "qveToken transfer error");
        require(normal_mint(sender, QVEamount), "mint error");
        _inputEscrowVault(QVEamount);
        return true;
    }

    function setesQVEVesting(address _esQVEVestingAddress) external returns(bool){
        esQVEVestingAddress = _esQVEVestingAddress;
        return true;
    }

    // [------ Getters ------] // 
    function getEscrowedBalance_() external view returns(uint256){
        return escrowedQVE[msg.sender].amount;
    }

    // [------ Internal functions -------] //
    function normal_mint(address account, uint256 amount) public returns(bool){
        super._mint(account, amount);
        
        emit MintEvent(account, amount);
        return true;
    }

    function _inputEscrowVault(uint256 amount) internal{
        escrowedQVE[msg.sender].amount += amount;
        escrowedQVE[msg.sender].at = block.timestamp;
    }

    function mintForLockup(address sender, uint256 marginAmount) external returns(uint256){
        normal_mint(sender, marginAmount.mul(LOCKUP_ESCROWRATIO).div(100));
        return marginAmount.mul(LOCKUP_ESCROWRATIO).div(100);
    }

    // function _beforeTokenTransfer(address from, address to, uint256 amount) internal override  {
    //     super._beforeTokenTransfer(from, to, amount);
    //     require( from != address(0), "INVALID ADDRESS");
    //         if( from != esQVEVestingAddress || from != ){
    //             require(block.timestamp >= escrowedQVE[msg.sender].at + LOCK_UP_DAYS, string(abi.encodePacked(
    //             "token is still in lock period ", 
    //             Strings.toString( 
    //                 (escrowedQVE[msg.sender].at + LOCK_UP_DAYS - block.timestamp)/60/60/12), 
    //             "days left"
    //             )
    //             ));   
    //         }
    // }

}
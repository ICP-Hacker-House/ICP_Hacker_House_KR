// SPDX-License-Identifier: MIT
pragma solidity ^0.8.10;

import "@openzeppelin/contracts/utils/math/SafeMath.sol";
import "@openzeppelin/contracts/token/ERC20/utils/SafeERC20.sol";
import "@openzeppelin/contracts/token/ERC20/utils/TokenTimelock.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/utils/Counters.sol";
import "./tokens/QVEtoken.sol"; 
import "./QVEescrow.sol";
import "./util/Security.sol";
import "hardhat/console.sol";


contract QVEvesting is Security{
    using SafeMath for uint256;
    using Counters for Counters.Counter;
    Counters.Counter private VestingCounter;
    QVEtoken qveToken;
    QVEescrow qveEscrow;


    // [------ Warn messages ------] //
    string private constant INSUFFICIENT_BALANCE = "Insufficient balance";
    string private constant INVALID_VESTING_ID = "Invalid vesting id";
    string private constant VESTING_ALREADY_RELEASED = "Vesting already released";
    string private constant INVALID_BENEFICIARY = "Invalid beneficiary address";
    string private constant NOT_VESTED = "You don't have vested QVE";
    string private constant WARN_VESTING_PERIOD = "We have Token's vesting period";


    // [------ Variables, Mappings ------] //
    struct Vesting{
        uint256 vestedTime;
        uint256 amount;
        address beneficiary;
        bool released;
    }

    mapping(uint256 => Vesting) public vestings;
    mapping(address => uint256[]) public ownedVestings;

    uint256 constant public VESTING_PERIOD = 0 days;


    // [------ Events ------] //
    event TokenVestingReleased(uint256 indexed vesting, address indexed beneficiary, uint256 amount);
    event TokenVestingAdded(uint256 indexed vesting, address indexed beneficiary, uint256 amount);
    event TokenVestingRemoved(uint256 indexed vesting, address indexed beneficiary, uint256 amount);


    // [------ Variables ------] //    
    uint256 public tokensToVest = 0;
    

    // [------ Modifiers ------] //
    modifier HaveVesting(address sender){
        require(ownedVestings[sender].length > 0, NOT_VESTED);
        _;
    }

    // [------ Init ------] //
    constructor(QVEtoken _qveToken, QVEescrow _qveEscrow) {
        require(address(_qveToken) != address(0), INVALID_BENEFICIARY);
        qveToken = _qveToken;
        qveEscrow = _qveEscrow;
    }

    function token() public view returns(ERC20){
        return qveToken;
    }


    // [------ external, public Functions ------] // 
    function getBeneficiary(uint256 _vestingId) external view returns(address){
        return vestings[_vestingId].beneficiary;
    }

    function getVestedTime(uint256 _vestingId) external view returns(uint256){
        return vestings[_vestingId].vestedTime;
    }

    function getVestingAmount(uint256 _vestingId) external  view returns(uint256){
        return vestings[_vestingId].amount;
    }

    function addVesting(uint256 _amount, address sender) public NoReEntrancy returns(bool){
        require(qveEscrow.normal_transfer(sender, address(this), _amount));

        tokensToVest = tokensToVest.add(_amount);

        vestings[VestingCounter.current()] = Vesting({
            beneficiary : sender,
            vestedTime : block.timestamp,
            amount : _amount,
            released : false
        });

        ownedVestings[sender].push(VestingCounter.current());

        emit TokenVestingAdded(VestingCounter.current(), sender, _amount);

        VestingCounter.increment();

        return true;
    }

    function releaseVestedQVE(uint256 _vestingId, address sender) public NoReEntrancy{

        Vesting storage vesting = vestings[_vestingId];

        // Check for Vesting Period
        require(ownedVestings[sender].length > 0, NOT_VESTED);
        require(block.timestamp >= vesting.vestedTime, NOT_VESTED);
        require(block.timestamp - vesting.vestedTime >= VESTING_PERIOD, WARN_VESTING_PERIOD);
        require(!vesting.released , VESTING_ALREADY_RELEASED);
        
        claimForQVE(_vestingId, sender);        
        qveEscrow.burnEsQVE(vesting.amount, address(this));
        removeVesting(_vestingId, sender, true);
        emit TokenVestingReleased(_vestingId, vesting.beneficiary, vesting.amount);
    }


    // [------ internal Functions -------] //
    function getVestingId(address sender) internal view HaveVesting(sender) returns(uint256[] memory){
        return ownedVestings[sender];
    }

    function removeVesting(uint _vestingId, address sender, bool isReleased) internal HaveVesting(sender) returns(bool){
        uint256[] storage vestingIds = ownedVestings[sender];
        
        Vesting storage vesting = vestings[_vestingId];
        require(vesting.beneficiary != address(0), INVALID_BENEFICIARY);
        require(!vesting.released, VESTING_ALREADY_RELEASED);

        tokensToVest = tokensToVest.sub(vesting.amount);
        
        for(uint i = 0; i < vestingIds.length ; i++){
            if( vestingIds[i] == _vestingId ){
                vestingIds[i] = vestingIds[vestingIds.length-1];
                vestingIds.pop();
                vesting.released = isReleased;
                break;
            }
        }

        emit TokenVestingRemoved(_vestingId, vesting.beneficiary, vesting.amount);
        return true;
    }

    function claimForQVE(uint256 _vestingId, address sender) internal returns(bool) {
        require(qveToken.normal_mint(sender, getExpectedQVE(_vestingId, sender)));
        return true;
    } 

    function getExpectedQVE(uint256 _vestingId, address sender) public view returns(uint256){
        require(ownedVestings[sender].length > 0, NOT_VESTED);
        uint256 timeFlowed = block.timestamp.sub(vestings[_vestingId].vestedTime);
        return vestings[_vestingId].amount + timeFlowed.mul(vestings[_vestingId].amount.mul(3).div(100000));
    }
}
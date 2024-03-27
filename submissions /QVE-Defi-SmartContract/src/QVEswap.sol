// SPDX-License-Identifier: MIT
pragma solidity ^0.8.10;

import "./tokens/QVEtoken.sol";
import "./util/Security.sol";
import "@openzeppelin/contracts/utils/math/SafeMath.sol";
import "hardhat/console.sol";

contract QVEswap is Security {

    // [------ Variables, Mappings ------] //
    QVEtoken public qveToken;
    address public owner;
    using SafeMath for uint256;

    constructor(QVEtoken _qveToken) {
        qveToken = _qveToken;
        uint256 tokenInit = 100000000000000000000;
        require(qveToken.normal_mint(address(this), tokenInit.mul(1e18)), "Initialize failed");
        _addQVELiquidity(tokenInit.mul(1e18));
        owner = msg.sender;
    }

    // [------ Swap Logs ------] //
     struct SwapLogChunk{
        bool QVEtoETH;
        bool ETHtoQVE;
        uint256 swapAmount;
        
    }
    mapping (address => SwapLogChunk[]) SwapLogs;
    

    // [------ Pools ------] // 
    struct Pool{
        uint256 QVEbalance;
        uint256 ETHbalance;
    }

    // mapping(address => mapping(address => Pool)) public pools;  // 추후 (erc20) 여러 쌍을 넣을 수 있게 

    mapping(address => Pool) QVESwapPool; //  QVE - ETH - Pool


    // [------ Warnings ------] //
    string constant private WARNING_BALANCE_TOKEN = "Warning : Not Enough ERC20 Token";
    string constant private WARNING_BALANCE_ETHER = "Warning : Not Enough Ether";
    string constant private WARNING_SENTAMOUNT_MATCH = "Warning : Sent Ether doesn't match specified amount";
    string constant private WARNING_TRANSFER = "Warning : Transfer failed";

    // [------ Modifiers, Events ------] //
    modifier onlyOwner() {
        require(msg.sender == owner, "Not the contract owner");
        _;
    }

    event SwapQVEtoETH(uint256 QVEamount);
    event SwapETHtoQVE(uint256 ETHamount);

    // [------ Getters ------] //
    function getETHliquidity_() external view returns(uint){
        return QVESwapPool[address(qveToken)].ETHbalance;
    }

    function getQVEliquidity_() external view returns(uint){
        return QVESwapPool[address(qveToken)].QVEbalance;
    }

    // [------ Internals ------] //
    function _addQVELiquidity(uint256 qveAmount) internal returns(bool){
        Pool storage pool = QVESwapPool[address(qveToken)];
        pool.QVEbalance = pool.QVEbalance.add(qveAmount);
        return true;
    }

    function _addETHLiquidity(uint256 ethAmount) internal returns(bool){
        Pool storage pool = QVESwapPool[address(qveToken)];
        pool.ETHbalance = pool.ETHbalance.add(ethAmount);
        return true;
    }

    // [------ Functions ------- //
    function depositEther() external payable onlyOwner {}

    function swapQVEtoETH(uint256 tokenAmount, address sender) NoReEntrancy external {
        console.log(tokenAmount);
        console.log(qveToken.balanceOf(sender));
        console.log(address(this).balance);
        require(tokenAmount <= qveToken.balanceOf(sender), WARNING_BALANCE_TOKEN);
        uint256 etherAmount = address(this).balance;
        require(etherAmount >= tokenAmount, WARNING_BALANCE_ETHER);

        require(qveToken.normal_transfer(sender, address(this), tokenAmount), WARNING_TRANSFER);
        payable(sender).transfer(tokenAmount);


        Pool storage SwapPool =  QVESwapPool[address(qveToken)];
        SwapPool.ETHbalance = SwapPool.ETHbalance.sub(tokenAmount);
        SwapPool.QVEbalance = SwapPool.QVEbalance.add(tokenAmount);

        SwapLogChunk memory newSwapLog = SwapLogChunk({ETHtoQVE:false, QVEtoETH:true, swapAmount : tokenAmount});
        SwapLogs[sender].push(newSwapLog);


        emit SwapQVEtoETH(tokenAmount);
    }

    function swapETHtoQVE(uint256 ETHamount, address sender)  external payable {
        console.log(msg.value);
        console.log(ETHamount);
        require(msg.value == ETHamount, WARNING_SENTAMOUNT_MATCH);
        require(ETHamount <= qveToken.balanceOf(address(this)), WARNING_BALANCE_TOKEN);

        require(qveToken.transfer(sender, ETHamount), WARNING_TRANSFER);

        Pool storage SwapPool =  QVESwapPool[address(qveToken)];
        SwapPool.ETHbalance = SwapPool.ETHbalance.add(ETHamount);
        SwapPool.QVEbalance = SwapPool.QVEbalance.sub(ETHamount);

        SwapLogChunk memory newSwapLog = SwapLogChunk({ETHtoQVE:true, QVEtoETH:false, swapAmount : ETHamount});
        SwapLogs[sender].push(newSwapLog);

        emit SwapETHtoQVE(ETHamount);
    }
}

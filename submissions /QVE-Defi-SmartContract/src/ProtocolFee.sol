// SPDX-License-Identifier: MIT
pragma solidity ^0.8.10;
import "@openzeppelin/contracts/utils/math/SafeMath.sol";
import "@openzeppelin/contracts/utils/Counters.sol";
import "./QVEstaking.sol";
import "./QVEcore.sol";

contract ProtocolFee{
    using SafeMath for uint256;
    using Counters for Counters.Counter;
    QVEstaking qveStaking;
    QVEcore qveCore;
    Counters.Counter public totalSettle;

    constructor(QVEstaking _qveStaking, QVEcore _qveCore) {
        qveStaking = _qveStaking;
        qveCore = _qveCore;
    }

    // [------ Balances ------] // 
    uint256 private totalBalance; // Protocol Fee Contract 가 들고 있는 금액입니다.  

    struct StrategySettledAmount{
        uint256 amount; // 쌓인 금액
        uint256 at; // 마지막으로 정산받은 timestamp
    }

    mapping (address => StrategySettledAmount) StrategiesBalance;

    // [------ Settle From Stretegy ------] //
    function SettleFromStrategy(uint256 strategy) external payable returns(bool){ // 서버에서 호출하는 함수 ( msg.sender는 봇주소로 설정해주세요 )
        require(msg.sender == qveCore.getstrategyAddress(strategy), "Warn : invalid strategy Address");
        _SettleAfter(msg.value, msg.sender);

        return true;
    }


    // [------ Send To Distribute Contract ------] //
    function SendToUnstakeAccount() external returns(bool){    // 서버에서 호출하는 함수
        qveStaking.receiveSettledEth{value: totalBalance}(totalBalance);
        _sendAfter(totalBalance);
        return true;
    }
    
    function _sendAfter(uint256 sentAmount) internal returns(bool){
        totalBalance = totalBalance.sub(sentAmount);
        return true;
    }

    // [------ Internal ------] // 
    function _SettleAfter(uint256 receiveAmount, address sender) internal returns(bool){
        StrategiesBalance[sender].amount = StrategiesBalance[sender].amount.add(receiveAmount);
        totalSettle.increment();
        totalBalance = totalBalance.add(receiveAmount);
        return true;
    }
}

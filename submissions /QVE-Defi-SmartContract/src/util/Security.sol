// SPDX-License-Identifier: MIT
pragma solidity ^0.8.10;

contract Security{
    bool private locked;

    modifier NoReEntrancy(){
        require(!locked, "No ReEntrancy");
        locked = true;
        _;
        locked = false;
    }
}
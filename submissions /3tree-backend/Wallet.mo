import Ledger "mo:base/Ledger";
import Principal "mo:base/Principal";

actor Wallet {

    private var balance : Nat = 0;

    // Function to receive ICP. Assume this is a callback from a ledger transfer
    public func receiveICP(amount : Nat) : async () {
        balance += amount;
        // Logic to confirm receipt might go here
    }

    // Function to send ICP to another wallet
    public func sendICP(recipient : Principal, amount : Nat) : async Bool {
        if (balance < amount) {
            return false; // Insufficient funds
        }
        // Assuming 'Ledger' is an interface to the ICP ledger canister
        // This would involve calling a 'transfer' function on the ledger canister
        let result = await Ledger.transfer(recipient, amount);
        if (result) {
            balance -= amount;
        }
        return result;
    }

    // Function to check the balance of the wallet
    public func checkBalance() : async Nat {
        return balance;
    }
}

// Interface to the ICP ledger canister
// Note: You would need the actual API definition for the ledger canister
// This is just a placeholder to illustrate the concept
module Ledger {
    public func transfer(recipient : Principal, amount : Nat) : async Bool {
        // Logic to transfer ICP
    }
}
